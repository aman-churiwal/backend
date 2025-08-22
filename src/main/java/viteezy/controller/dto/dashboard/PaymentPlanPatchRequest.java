package viteezy.controller.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.payment.PaymentPlanStatus;

import java.util.Objects;
import java.util.StringJoiner;

public class PaymentPlanPatchRequest {
    private final Long id;
    private final PaymentPlanStatus paymentPlanStatus;

    @JsonCreator
    public PaymentPlanPatchRequest(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "status", required = true) PaymentPlanStatus paymentPlanStatus
    ) {
        this.id = id;
        this.paymentPlanStatus = paymentPlanStatus;
    }

    public Long getId() {
        return id;
    }

    public PaymentPlanStatus getPaymentPlanStatus() {
        return paymentPlanStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentPlanPatchRequest that = (PaymentPlanPatchRequest) o;
        return Objects.equals(id, that.id) && paymentPlanStatus == that.paymentPlanStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentPlanStatus);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PaymentPlanPatchRequest.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("paymentPlanStatus=" + paymentPlanStatus)
                .toString();
    }
}
