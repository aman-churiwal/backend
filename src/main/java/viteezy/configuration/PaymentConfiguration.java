package viteezy.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Objects;
import java.util.StringJoiner;

public class PaymentConfiguration {
    private final Integer recurringMonths;
    private final String firstDescription;
    private final String recurringDescription;
    private final String apiKey;
    private final URI redirectUrl;
    private final URI requestRedirectUrl;
    private final URI webhookUrl;
    private final BigDecimal shippingCosts;
    private final BigDecimal shippingCostsThreshold;
    private final Integer startDateInDays;
    private final Integer deliveryDateInDays;
    private final Integer paymentProcessingInDays;

    @JsonCreator
    public PaymentConfiguration(
            @JsonProperty(value = "recurring_months", required = true) Integer recurringMonths,
            @JsonProperty(value = "first_description", required = true) String firstDescription,
            @JsonProperty(value = "recurring_description", required = true) String recurringDescription,
            @JsonProperty(value = "api_key", required = true) String apiKey,
            @JsonProperty(value = "redirect_url", required = true) URI redirectUrl,
            @JsonProperty(value = "request_redirect_url", required = true) URI requestRedirectUrl,
            @JsonProperty(value = "webhook_url", required = true) URI webhookUrl,
            @JsonProperty(value = "shipping_costs", required = true) BigDecimal shippingCosts,
            @JsonProperty(value = "shipping_costs_threshold", required = true) BigDecimal shippingCostsThreshold,
            @JsonProperty(value = "start_date_in_days", required = true) Integer startDateInDays,
            @JsonProperty(value = "delivery_date_in_days", required = true) Integer deliveryDateInDays,
            @JsonProperty(value = "payment_processing_in_days", required = true) Integer paymentProcessingInDays) {
        this.recurringMonths = recurringMonths;
        this.firstDescription = firstDescription;
        this.recurringDescription = recurringDescription;
        this.apiKey = apiKey;
        this.redirectUrl = redirectUrl;
        this.requestRedirectUrl = requestRedirectUrl;
        this.webhookUrl = webhookUrl;
        this.shippingCosts = shippingCosts;
        this.shippingCostsThreshold = shippingCostsThreshold;
        this.startDateInDays = startDateInDays;
        this.deliveryDateInDays = deliveryDateInDays;
        this.paymentProcessingInDays = paymentProcessingInDays;
    }

    public Integer getRecurringMonths() {
        return recurringMonths;
    }

    public String getFirstDescription() {
        return firstDescription;
    }

    public String getRecurringDescription() {
        return recurringDescription;
    }

    public String getApiKey() {
        return apiKey;
    }

    public URI getRedirectUrl() {
        return redirectUrl;
    }

    public URI getRequestRedirectUrl() {
        return requestRedirectUrl;
    }

    public URI getWebhookUrl() {
        return webhookUrl;
    }

    public BigDecimal getShippingCosts() {
        return shippingCosts;
    }

    public BigDecimal getShippingCostsThreshold() {
        return shippingCostsThreshold;
    }

    public Integer getStartDateInDays() {
        return startDateInDays;
    }

    public Integer getDeliveryDateInDays() {
        return deliveryDateInDays;
    }

    public Integer getPaymentProcessingInDays() {
        return paymentProcessingInDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentConfiguration that = (PaymentConfiguration) o;
        return Objects.equals(recurringMonths, that.recurringMonths) && Objects.equals(firstDescription, that.firstDescription) && Objects.equals(recurringDescription, that.recurringDescription) && Objects.equals(apiKey, that.apiKey) && Objects.equals(redirectUrl, that.redirectUrl) && Objects.equals(requestRedirectUrl, that.requestRedirectUrl) && Objects.equals(webhookUrl, that.webhookUrl) && Objects.equals(shippingCosts, that.shippingCosts) && Objects.equals(shippingCostsThreshold, that.shippingCostsThreshold) && Objects.equals(startDateInDays, that.startDateInDays) && Objects.equals(deliveryDateInDays, that.deliveryDateInDays) && Objects.equals(paymentProcessingInDays, that.paymentProcessingInDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recurringMonths, firstDescription, recurringDescription, apiKey, redirectUrl, requestRedirectUrl, webhookUrl, shippingCosts, shippingCostsThreshold, startDateInDays, deliveryDateInDays, paymentProcessingInDays);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PaymentConfiguration.class.getSimpleName() + "[", "]")
                .add("recurringMonths=" + recurringMonths)
                .add("firstDescription='" + firstDescription + "'")
                .add("recurringDescription='" + recurringDescription + "'")
                .add("apiKey='" + apiKey + "'")
                .add("redirectUrl=" + redirectUrl)
                .add("requestRedirectUrl=" + requestRedirectUrl)
                .add("webhookUrl=" + webhookUrl)
                .add("shippingCosts=" + shippingCosts)
                .add("shippingCostsThreshold=" + shippingCostsThreshold)
                .add("startDateInDays=" + startDateInDays)
                .add("deliveryDateInDays=" + deliveryDateInDays)
                .add("paymentProcessingInDays=" + paymentProcessingInDays)
                .toString();
    }
}
