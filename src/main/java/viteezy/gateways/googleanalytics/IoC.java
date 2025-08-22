package viteezy.gateways.googleanalytics;

import com.brsanthu.googleanalytics.GoogleAnalytics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.configuration.googleanalytics.GoogleAnalyticsConfiguration;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.pricing.IngredientPriceService;
import viteezy.service.pricing.ShippingService;

@Configuration("googleAnalyticsGatewayIoC")
public class IoC {

    private final GoogleAnalyticsConfiguration googleAnalyticsConfiguration;
    private final boolean debugMode;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(
            ViteezyConfiguration viteezyConfiguration
    ) {
        this.googleAnalyticsConfiguration = viteezyConfiguration.getGoogleAnalyticsConfiguration();
        this.debugMode = viteezyConfiguration.getPaymentConfiguration().getApiKey().startsWith("test_");
    }

    @Bean("googleAnalyticsService")
    public GoogleAnalyticsService googleAnalyticsService(BlendIngredientService blendIngredientService,
                                                        IngredientPriceService ingredientPriceService,
                                                        ShippingService shippingService) {
        String trackingId = googleAnalyticsConfiguration.getTrackingId();
        GoogleAnalytics googleAnalytics = GoogleAnalytics.builder().withTrackingId(trackingId).build();
        return new GoogleAnalyticsServiceImpl(googleAnalytics,
                googleAnalyticsConfiguration,
                blendIngredientService,
                ingredientPriceService,
                shippingService,
                debugMode);
    }
}
