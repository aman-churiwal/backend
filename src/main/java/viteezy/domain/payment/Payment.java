package viteezy.domain.payment;

import be.woutschoovaerts.mollie.data.payment.SequenceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Payment {
    private final Long id;
    private final BigDecimal amount;
    private final String molliePaymentId;
    private final String retriedMolliePaymentId;
    private final Long paymentPlanId;
    private final LocalDateTime creationDate;
    private final LocalDateTime paymentDate;
    private final LocalDateTime lastModified;
    private final PaymentStatus status;
    private final String reason;
    private final SequenceType sequenceType;

    public Payment(Long id, BigDecimal amount, String molliePaymentId, String retriedMolliePaymentId, Long paymentPlanId, LocalDateTime creationDate, LocalDateTime paymentDate, LocalDateTime lastModified, PaymentStatus status, String reason, SequenceType sequenceType) {
        this.id = id;
        this.amount = amount;
        this.molliePaymentId = molliePaymentId;
        this.retriedMolliePaymentId = retriedMolliePaymentId;
        this.paymentPlanId = paymentPlanId;
        this.creationDate = creationDate;
        this.paymentDate = paymentDate;
        this.lastModified = lastModified;
        this.status = status;
        this.reason = reason;
        this.sequenceType = sequenceType;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMolliePaymentId() {
        return molliePaymentId;
    }

    public String getRetriedMolliePaymentId() {
        return retriedMolliePaymentId;
    }

    public Long getPaymentPlanId() {
        return paymentPlanId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public SequenceType getSequenceType() {
        return sequenceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) &&
                Objects.equals(amount, payment.amount) &&
                Objects.equals(molliePaymentId, payment.molliePaymentId) &&
                Objects.equals(retriedMolliePaymentId, payment.retriedMolliePaymentId) &&
                Objects.equals(paymentPlanId, payment.paymentPlanId) &&
                Objects.equals(creationDate, payment.creationDate) &&
                Objects.equals(paymentDate, payment.paymentDate) &&
                Objects.equals(lastModified, payment.lastModified) &&
                status == payment.status &&
                Objects.equals(reason, payment.reason) &&
                sequenceType == payment.sequenceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, molliePaymentId, retriedMolliePaymentId, paymentPlanId, creationDate, paymentDate,
                lastModified, status, reason, sequenceType);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Payment.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("amount=" + amount)
                .add("molliePaymentId='" + molliePaymentId + "'")
                .add("retriedMolliePaymentId='" + retriedMolliePaymentId + "'")
                .add("paymentPlanId=" + paymentPlanId)
                .add("creationDate=" + creationDate)
                .add("paymentDate=" + paymentDate)
                .add("lastModified=" + lastModified)
                .add("status=" + status)
                .add("reason='" + reason + "'")
                .add("sequenceType=" + sequenceType)
                .toString();
    }
}
