package viteezy.service.pricing;

import io.vavr.control.Try;
import org.jdbi.v3.core.mapper.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.pricing.Coupon;
import viteezy.domain.pricing.CouponDiscount;
import viteezy.domain.pricing.CouponDiscountStatus;
import viteezy.domain.pricing.CouponUsed;
import viteezy.domain.payment.PaymentPlan;

import java.math.BigDecimal;
import java.util.Optional;

public interface CouponService {

    Try<Coupon> find(Long id);

    Try<Coupon> find(String couponCode);

    Try<Coupon> findValid(String couponCode, Integer recurringMonths, BigDecimal amount);

    Try<Optional<CouponUsed>> findCouponUsedByPaymentPlan(Long paymentPlanId);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Void> setCouponUsedForCustomerPaymentPlan(String couponCode, PaymentPlan paymentPlan);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Coupon> updateCouponUsed(CouponUsed couponUsed);

    Try<Optional<CouponDiscount>> findDiscountByPaymentPlan(Long paymentPlanId, CouponDiscountStatus status);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<CouponDiscount> save(CouponDiscount couponDiscount);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<CouponDiscount> update(CouponDiscount couponDiscount);
}
