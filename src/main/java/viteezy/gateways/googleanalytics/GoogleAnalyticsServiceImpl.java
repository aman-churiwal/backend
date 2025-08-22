package viteezy.gateways.googleanalytics;

import com.brsanthu.googleanalytics.GoogleAnalytics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.google.common.base.Throwables;
import io.vavr.control.Try;
import jakarta.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.configuration.googleanalytics.GoogleAnalyticsConfiguration;
import viteezy.domain.Customer;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.google.MeasurementProtocol;
import viteezy.domain.google.MeasurementProtocolEvent;
import viteezy.domain.google.MeasurementProtocolEventItem;
import viteezy.domain.google.MeasurementProtocolEventParam;
import viteezy.domain.ingredient.IngredientPrice;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.pricing.IngredientPriceService;
import viteezy.service.pricing.ShippingService;
import viteezy.traits.EnforcePresenceTrait;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GoogleAnalyticsServiceImpl implements GoogleAnalyticsService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleAnalyticsService.class);
    private static final String PURCHASE = "purchase";
    private static final String CURRENCY = "EUR";
    private static final BigDecimal VAT_NINE_PERCENTAGE_AS_RATIO = new BigDecimal("1.09");

    private final GoogleAnalytics googleAnalytics;
    private final URI uri;
    private final BlendIngredientService blendIngredientService;
    private final IngredientPriceService ingredientPriceService;
    private final ShippingService shippingService;
    private final ObjectWriter writer;
    private final HttpClient httpClient;
    private final int debugMode;

    protected GoogleAnalyticsServiceImpl(GoogleAnalytics googleAnalytics, GoogleAnalyticsConfiguration configuration, BlendIngredientService blendIngredientService,
                                         IngredientPriceService ingredientPriceService, ShippingService shippingService, boolean debugMode) {
        this.googleAnalytics = googleAnalytics;
        this.uri = UriBuilder.fromUri("https://www.google-analytics.com/mp/collect")
                .queryParam("measurement_id", configuration.getMeasurementId())
                .queryParam("api_secret", configuration.getApiSecret())
                .build();
        this.blendIngredientService = blendIngredientService;
        this.ingredientPriceService = ingredientPriceService;
        this.shippingService = shippingService;
        this.debugMode = debugMode ? 1 : 0;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.writer = mapper.writer();
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public void sendEcommerceTransaction(Customer customer,
                                         PaymentPlan paymentPlan, String gaSessionId) {
        final BigDecimal shippingCost = shippingService.getShippingCostFromFirstAmount(paymentPlan.firstAmount());
        final BigDecimal revenue = paymentPlan.firstAmount().setScale(2, RoundingMode.HALF_UP);
        final BigDecimal subTotal = revenue.divide(VAT_NINE_PERCENTAGE_AS_RATIO, 2, RoundingMode.HALF_UP); // 9% VAT
        final BigDecimal taxTotal = revenue.subtract(subTotal).setScale(2, RoundingMode.HALF_DOWN);
        long timestampMicros = paymentPlan.creationDate().toInstant(ZoneOffset.UTC).toEpochMilli() * 1000;

        googleAnalytics.transaction()
                .clientId(customer.getGaId())
                .userAgent(customer.getUserAgent())
                .userIp(customer.getUserIpAddress())
                .txId(paymentPlan.externalReference().toString())
                .currencyCode(CURRENCY)
                .txRevenue(subTotal.doubleValue())
                .txTax(taxTotal.doubleValue())
                .txShipping(shippingCost.doubleValue())
                .sendAsync();

        getBlendIngredients(paymentPlan)
                .map(items -> new MeasurementProtocolEventParam(
                        paymentPlan.externalReference(),
                        CURRENCY,
                        subTotal,
                        taxTotal,
                        items,
                        debugMode,
                        gaSessionId))
                .map(params -> new MeasurementProtocolEvent(PURCHASE, params))
                .map(event -> new MeasurementProtocol(customer.getGaId(), timestampMicros, List.of(event)))
                .flatMap(measurementProtocol -> Try.of(() -> HttpRequest.newBuilder()
                        .uri(uri)
                        .POST(HttpRequest.BodyPublishers.ofString(writer.writeValueAsString(measurementProtocol)))
                        .setHeader("Content-Type", "application/json")
                        .build()))
                .flatMap(httpRequest -> Try.of(() -> httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())))
                .onFailure(peekException());
    }

    private Try<List<MeasurementProtocolEventItem>> getBlendIngredients(PaymentPlan paymentPlan) {
        final String itemVariant = paymentPlan.recurringMonths().equals(1) ? "1 maand" : paymentPlan.recurringMonths() + " maanden";
        return ingredientPriceService.findAllActive()
                .map(ingredientPrices -> ingredientPrices.stream()
                        .collect(Collectors.toMap(IngredientPrice::getIngredientId, IngredientPrice::getPrice)))
                        .flatMap(priceByIngredientMap -> blendIngredientService.getIngredients(paymentPlan.blendId())
                                .map(ingredients -> ingredients.toStream().map(ingredient ->
                                        new MeasurementProtocolEventItem(
                                                ingredient.getName(),
                                                ingredient.getId(),
                                                itemVariant,
                                                priceByIngredientMap.get(ingredient.getId()),
                                                1))
                                        .collect(Collectors.toList())));
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
