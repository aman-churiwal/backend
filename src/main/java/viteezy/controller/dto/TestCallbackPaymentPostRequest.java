package viteezy.controller.dto;

import be.woutschoovaerts.mollie.data.payment.PaymentStatus;
import be.woutschoovaerts.mollie.data.payment.SequenceType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class TestCallbackPaymentPostRequest {

    private final String molliePaymentId;
    private final String retriedMolliePaymentId;
    private final String paymentPlanExternalReference;
    private final String currency;
    private final BigDecimal amount;
    private final PaymentStatus status;
    private final Boolean isPaid;
    private final SequenceType sequenceType;
    private final String reason;
    private final String description;
    private final Long referralId;

    @JsonCreator
    public TestCallbackPaymentPostRequest(
            @JsonProperty(value = "molliePaymentId", required = true) String molliePaymentId,
            @JsonProperty(value = "retriedMolliePaymentId", required = false) String retriedMolliePaymentId,
            @JsonProperty(value = "paymentPlanExternalReference", required = true) String paymentPlanExternalReference,
            @JsonProperty(value = "currency", required = true) String currency,
            @JsonProperty(value = "amount", required = true) BigDecimal amount,
            @JsonProperty(value = "status", required = true) PaymentStatus status,
            @JsonProperty(value = "isPaid", required = true) Boolean isPaid,
            @JsonProperty(value = "sequenceType", required = true) SequenceType sequenceType,
            @JsonProperty(value = "reason", required = false) String reason,
            @JsonProperty(value = "description", required = true) String description,
            @JsonProperty(value = "referralId", required = false) Long referralId) {
        this.molliePaymentId = molliePaymentId;
        this.retriedMolliePaymentId = retriedMolliePaymentId;
        this.paymentPlanExternalReference = paymentPlanExternalReference;
        this.currency = currency;
        this.amount = amount;
        this.status = status;
        this.isPaid = isPaid;
        this.sequenceType = sequenceType;
        this.reason = reason;
        this.description = description;
        this.referralId = referralId;
    }

    public String getMolliePaymentId() {
        return molliePaymentId;
    }

    public String getRetriedMolliePaymentId() {
        return retriedMolliePaymentId;
    }

    public String getPaymentPlanExternalReference() {
        return paymentPlanExternalReference;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public SequenceType getSequenceType() {
        return sequenceType;
    }

    public String getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }

    public Long getReferralId() {
        return referralId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCallbackPaymentPostRequest that = (TestCallbackPaymentPostRequest) o;
        return Objects.equals(molliePaymentId, that.molliePaymentId) && Objects.equals(retriedMolliePaymentId, that.retriedMolliePaymentId) && Objects.equals(paymentPlanExternalReference, that.paymentPlanExternalReference) && Objects.equals(currency, that.currency) && Objects.equals(amount, that.amount) && status == that.status && Objects.equals(isPaid, that.isPaid) && sequenceType == that.sequenceType && Objects.equals(reason, that.reason) && Objects.equals(description, that.description) && Objects.equals(referralId, that.referralId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(molliePaymentId, retriedMolliePaymentId, paymentPlanExternalReference, currency, amount, status, isPaid, sequenceType, reason, description, referralId);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TestCallbackPaymentPostRequest.class.getSimpleName() + "[", "]")
                .add("molliePaymentId='" + molliePaymentId + "'")
                .add("retriedMolliePaymentId='" + retriedMolliePaymentId + "'")
                .add("paymentPlanExternalReference='" + paymentPlanExternalReference + "'")
                .add("currency='" + currency + "'")
                .add("amount=" + amount)
                .add("status=" + status)
                .add("isPaid=" + isPaid)
                .add("sequenceType=" + sequenceType)
                .add("reason='" + reason + "'")
                .add("description='" + description + "'")
                .add("referralId=" + referralId)
                .toString();
    }
}
