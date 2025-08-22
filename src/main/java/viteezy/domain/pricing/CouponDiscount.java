package viteezy.domain.pricing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class CouponDiscount {

    private final Long id;
    private final Long couponId;
    private final Long paymentPlanId;
    private final Integer month;
    private final BigDecimal amount;
    private final CouponDiscountStatus status;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public CouponDiscount(Long id, Long couponId, Long paymentPlanId, Integer month, BigDecimal amount,
                          CouponDiscountStatus status, LocalDateTime creationTimestamp,
                          LocalDateTime modificationTimestamp) {
        this.id = id;
        this.couponId = couponId;
        this.paymentPlanId = paymentPlanId;
        this.month = month;
        this.amount = amount;
        this.status = status;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getPaymentPlanId() {
        return paymentPlanId;
    }

    public Integer getMonth() {
        return month;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CouponDiscountStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouponDiscount that = (CouponDiscount) o;
        return Objects.equals(id, that.id) && Objects.equals(couponId, that.couponId) && Objects.equals(paymentPlanId, that.paymentPlanId) && Objects.equals(month, that.month) && Objects.equals(amount, that.amount) && status == that.status && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, couponId, paymentPlanId, month, amount, status, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CouponDiscount.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("couponId=" + couponId)
                .add("paymentPlanId=" + paymentPlanId)
                .add("month=" + month)
                .add("amount=" + amount)
                .add("status=" + status)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
