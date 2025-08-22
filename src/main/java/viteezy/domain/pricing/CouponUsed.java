package viteezy.domain.pricing;

import java.util.Objects;
import java.util.StringJoiner;

public class CouponUsed {
    private final Long id;
    private final Long couponId;
    private final Long customerId;
    private final Long paymentPlanId;
    private final CouponUsedStatus status;

    public CouponUsed(Long id, Long couponId, Long customerId, Long paymentPlanId, CouponUsedStatus status) {
        this.id = id;
        this.couponId = couponId;
        this.customerId = customerId;
        this.paymentPlanId = paymentPlanId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getPaymentPlanId() {
        return paymentPlanId;
    }

    public CouponUsedStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouponUsed that = (CouponUsed) o;
        return Objects.equals(id, that.id) && Objects.equals(couponId, that.couponId) && Objects.equals(customerId, that.customerId) && Objects.equals(paymentPlanId, that.paymentPlanId) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, couponId, customerId, paymentPlanId, status);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CouponUsed.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("couponId=" + couponId)
                .add("customerId=" + customerId)
                .add("paymentPlanId=" + paymentPlanId)
                .add("status=" + status)
                .toString();
    }
}
