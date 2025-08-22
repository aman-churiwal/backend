package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.pricing.*;
import viteezy.domain.payment.PaymentPlan;

import java.util.Optional;

public interface CouponRepository {
    Try<Coupon> find(Long id);

    Try<Coupon> find(String couponCode);

    Try<Coupon> used(Coupon coupon);

    Try<CouponUsed> findCouponUsed(Long id);

    Try<CouponDiscount> findDiscount(Long id);

    Try<Optional<CouponDiscount>> findDiscountByPaymentPlan(Long paymentPlanId, CouponDiscountStatus status);

    Try<Optional<CouponUsed>> findCouponUsedByPaymentPlan(Long paymentPlanId);

    Try<Integer> insertCouponUsed(Coupon coupon, PaymentPlan paymentPlan, CouponUsedStatus couponUsedStatus);

    Try<CouponUsed> updateCouponUsedStatus(CouponUsed couponUsed);

    Try<CouponDiscount> save(CouponDiscount couponDiscount);

    Try<CouponDiscount> updateCouponDiscountStatus(CouponDiscount couponDiscount);
}
