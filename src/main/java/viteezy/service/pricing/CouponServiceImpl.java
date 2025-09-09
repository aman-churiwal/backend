package viteezy.service.pricing;

import io.vavr.Lazy;
import io.vavr.control.Try;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.CouponRepository;
import viteezy.domain.pricing.*;
import viteezy.domain.Customer;
import viteezy.domain.payment.PaymentPlan;
import viteezy.traits.EnforcePresenceTrait;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class CouponServiceImpl implements CouponService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouponService.class);
    private final CouponRepository couponRepository;
    private final ReferralService referralService;

    protected CouponServiceImpl(CouponRepository couponRepository, ReferralService referralService) {
        this.couponRepository = couponRepository;
        this.referralService = referralService;
    }

    @Override
    public Try<Coupon> find(Long id) {
        return couponRepository.find(id);
    }

    @Override
    public Try<Coupon> find(String couponCode) {
        return couponRepository.find(couponCode);
    }

    @Override
    public Try<Coupon> findValid(String couponCode, Integer recurringMonths, BigDecimal amount) {
        return find(couponCode)
                .flatMap(coupon -> checkCoupon(coupon, recurringMonths, amount));
    }

    @Override
    public Try<Optional<CouponUsed>> findCouponUsedByPaymentPlan(Long paymentPlanId) {
        return couponRepository.findCouponUsedByPaymentPlan(paymentPlanId);
    }

    @Override
    public Try<Coupon> updateCouponUsed(CouponUsed couponUsed) {
        return couponRepository.updateCouponUsedStatus(couponUsed)
                .flatMap(updatedCouponUsed -> find(updatedCouponUsed.getCouponId())
                        .flatMap(couponRepository::used));
    }

    @Override
    public Try<Optional<CouponDiscount>> findDiscountByPaymentPlan(Long paymentPlanId, CouponDiscountStatus status) {
        return couponRepository.findDiscountByPaymentPlan(paymentPlanId, status);
    }

    @Override
    public Try<CouponDiscount> save(CouponDiscount couponDiscount) {
        return couponRepository.save(couponDiscount);
    }

    @Override
    public Try<CouponDiscount> update(CouponDiscount couponDiscount) {
        return couponRepository.updateCouponDiscountStatus(couponDiscount);
    }

    @Override
    public Try<Void> setCouponUsedForCustomerPaymentPlan(String couponCode, PaymentPlan paymentPlan) {
        Try<Optional<Customer>> customerTry = referralService.findCustomerByReferralCode(couponCode);
        if (customerTry.isSuccess() && customerTry.get().isEmpty()) {
            return find(couponCode)
                    .flatMap(coupon -> couponRepository.insertCouponUsed(coupon, paymentPlan, CouponUsedStatus.PAYMENT_CREATED))
                    .map(__ -> null);
        } else {
            return customerTry.map(__ -> null);
        }
    }

    private Try<Coupon> checkCoupon(Coupon coupon, Integer recurringMonths, BigDecimal amount) {
        final Lazy<Boolean> isValidUsedLazy = Lazy.of(() -> isValidUsed(coupon));
        final Lazy<Boolean> isValidAmountLazy = Lazy.of(() -> (amount == null || isValidAmount(coupon, amount)));
        final Lazy<Boolean> isValidDateLazy = Lazy.of(() -> isValidDate(coupon));
        final Lazy<Boolean> isValidRecurringMonthsLazy = Lazy.of(() -> (recurringMonths == null || isValidRecurringMonths(coupon, recurringMonths)));
        final Lazy<Boolean> isActiveLazy = Lazy.of(() -> isActive(coupon));
        if (isValidUsedLazy.get() && isValidAmountLazy.get() && isValidDateLazy.get() && isValidRecurringMonthsLazy.get() && isActiveLazy.get()) {
            return Try.success(coupon);
        } else {
            final String message = "Failed coupon validation." +
                    " validUsed=" + isValidUsedLazy.get() +
                    " validAmount=" + isValidAmountLazy.get() +
                    " validDate=" + isValidDateLazy.get() +
                    " validRecurringMonths=" + isValidRecurringMonthsLazy.get() +
                    " isActive=" + isActiveLazy.get() +
                    " amount=" + amount +
                    " coupon=" + coupon;
            return Try.failure(new ValidationException(message));
        }
    }

    private boolean isValidRecurringMonths(Coupon coupon, Integer recurringMonths) {
        return coupon.getRecurringMonths().isEmpty() || (coupon.getRecurringMonths().isPresent() && recurringMonths.equals(coupon.getRecurringMonths().get()));
    }

    private boolean isValidDate(Coupon coupon) {
        final LocalDateTime now = LocalDateTime.now();
        return coupon.getStartDate().isBefore(now)
                && coupon.getEndDate().isAfter(now);
    }

    private boolean isValidAmount(Coupon coupon, BigDecimal amount) {
        return coupon.getMinimumAmount().compareTo(amount) < 0;
    }

    private boolean isValidUsed(Coupon coupon) {
        return coupon.getMaxUses() == 0 || coupon.getUsed() < coupon.getMaxUses();
    }

    private boolean isActive(Coupon coupon) {
        return coupon.getIsActive();
    }
}
