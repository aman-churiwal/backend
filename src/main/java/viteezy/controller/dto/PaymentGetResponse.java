package viteezy.controller.dto;

import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.payment.PaymentStatus;

import java.net.URI;

public class PaymentGetResponse {
    private final PaymentStatus status;
    private final URI checkoutUrl;

    @JsonCreator
    public PaymentGetResponse(
            @JsonProperty(value = "status", required = true) PaymentStatus status,
            @JsonProperty(value = "checkoutUrl", required = true) URI checkoutUrl
    ) {
        this.status = status;
        this.checkoutUrl = checkoutUrl;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public URI getCheckoutUrl() {
        return checkoutUrl;
    }

    public static PaymentGetResponse from(PaymentResponse paymentResponse) {
        return new PaymentGetResponse(PaymentStatus.valueOf(paymentResponse.getStatus().getJsonValue()), URI.create(paymentResponse.getLinks().getCheckout().getHref()));
    }
}
