package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.payment.PaymentPlan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class PaymentPlanGetResponse {
    private final BigDecimal firstAmount;
    private final BigDecimal recurringAmount;
    private final Integer recurringMonths;
    private final String status;
    private final UUID externalReference;
    private final LocalDateTime creationDate;
    private final LocalDateTime startDate;
    private final LocalDateTime deliveryDate;
    private final LocalDateTime nextPaymentDate;
    private final LocalDateTime nextDeliveryDate;

    @JsonCreator
    public PaymentPlanGetResponse(
            @JsonProperty(value = "firstAmount", required = true) BigDecimal firstAmount,
            @JsonProperty(value = "recurringAmount", required = true) BigDecimal recurringAmount,
            @JsonProperty(value = "recurringMonths", required = true) Integer recurringMonths,
            @JsonProperty(value = "status", required = true) String status,
            @JsonProperty(value = "externalReference", required = true) UUID externalReference,
            @JsonProperty(value = "creationDate", required = true) LocalDateTime creationDate,
            @JsonProperty(value = "startDate", required = true) LocalDateTime startDate,
            @JsonProperty(value = "deliveryDate", required = true) LocalDateTime deliveryDate,
            @JsonProperty(value = "nextPaymentDate", required = false) LocalDateTime nextPaymentDate,
            @JsonProperty(value = "nextDeliveryDate", required = false) LocalDateTime nextDeliveryDate) {
        this.firstAmount = firstAmount;
        this.recurringAmount = recurringAmount;
        this.recurringMonths = recurringMonths;
        this.status = status;
        this.externalReference = externalReference;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.deliveryDate = deliveryDate;
        this.nextPaymentDate = nextPaymentDate;
        this.nextDeliveryDate = nextDeliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getFirstAmount() {
        return firstAmount;
    }

    public BigDecimal getRecurringAmount() {
        return recurringAmount;
    }

    public Integer getRecurringMonths() {
        return recurringMonths;
    }

    public UUID getExternalReference() {
        return externalReference;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public LocalDateTime getNextPaymentDate() {
        return nextPaymentDate;
    }

    public LocalDateTime getNextDeliveryDate() {
        return nextDeliveryDate;
    }

    public static PaymentPlanGetResponse from(PaymentPlan paymentPlan) {
        return new PaymentPlanGetResponse(
                paymentPlan.firstAmount(),
                paymentPlan.recurringAmount(),
                paymentPlan.recurringMonths(),
                paymentPlan.status().getJsonValue(),
                paymentPlan.externalReference(),
                paymentPlan.creationDate(),
                paymentPlan.paymentDate(),
                paymentPlan.deliveryDate(),
                paymentPlan.nextPaymentDate().orElse(null),
                paymentPlan.nextDeliveryDate().orElse(null));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentPlanGetResponse)) return false;
        PaymentPlanGetResponse that = (PaymentPlanGetResponse) o;
        return Objects.equals(firstAmount, that.firstAmount) && Objects.equals(recurringAmount, that.recurringAmount) && Objects.equals(recurringMonths, that.recurringMonths) && Objects.equals(status, that.status) && Objects.equals(externalReference, that.externalReference) && Objects.equals(creationDate, that.creationDate) && Objects.equals(startDate, that.startDate) && Objects.equals(deliveryDate, that.deliveryDate) && Objects.equals(nextPaymentDate, that.nextPaymentDate) && Objects.equals(nextDeliveryDate, that.nextDeliveryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstAmount, recurringAmount, recurringMonths, status, externalReference, creationDate, startDate, deliveryDate, nextPaymentDate, nextDeliveryDate);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PaymentPlanGetResponse.class.getSimpleName() + "[", "]")
                .add("firstAmount=" + firstAmount)
                .add("recurringAmount=" + recurringAmount)
                .add("recurringMonths=" + recurringMonths)
                .add("status='" + status + "'")
                .add("externalReference=" + externalReference)
                .add("creationDate=" + creationDate)
                .add("startDate=" + startDate)
                .add("deliveryDate=" + deliveryDate)
                .add("nextPaymentDate=" + nextPaymentDate)
                .add("nextDeliveryDate=" + nextDeliveryDate)
                .toString();
    }
}
