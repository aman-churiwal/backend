package viteezy.service.payment;

import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.payment.PaymentDetailsResponse;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.data.payment.PaymentStatus;
import be.woutschoovaerts.mollie.data.payment.SequenceType;
import be.woutschoovaerts.mollie.exception.MollieException;
import com.google.common.base.Throwables;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.configuration.PaymentConfiguration;
import viteezy.db.PaymentPlanRepository;
import viteezy.domain.*;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.pricing.*;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.service.CustomerService;
import viteezy.service.LoggingService;
import viteezy.service.mail.EmailService;
import viteezy.service.pricing.CouponService;
import viteezy.service.pricing.IncentiveService;
import viteezy.service.pricing.ReferralService;
import viteezy.traits.EnforcePresenceTrait;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaymentRecurringServiceImpl implements PaymentRecurringService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentRecurringService.class);

    private final PaymentService paymentService;
    private final PaymentPlanService paymentPlanService;
    private final PaymentConfiguration paymentConfiguration;
    private final CustomerService customerService;
    private final MollieService mollieService;
    private final EmailService emailService;
    private final CouponService couponService;
    private final ReferralService referralService;
    private final IncentiveService incentiveService;
    private final PaymentPlanRepository paymentPlanRepository;
    private final LoggingService loggingService;

    protected PaymentRecurringServiceImpl(PaymentService paymentService, PaymentPlanService paymentPlanService,
                                          PaymentConfiguration paymentConfiguration, CustomerService customerService,
                                          MollieService mollieService, EmailService emailService,
                                          CouponService couponService, ReferralService referralService,
                                          IncentiveService incentiveService,
                                          PaymentPlanRepository paymentPlanRepository, LoggingService loggingService) {
        this.paymentService = paymentService;
        this.paymentPlanService = paymentPlanService;
        this.paymentConfiguration = paymentConfiguration;
        this.customerService = customerService;
        this.mollieService = mollieService;
        this.emailService = emailService;
        this.couponService = couponService;
        this.referralService = referralService;
        this.incentiveService = incentiveService;
        this.paymentPlanRepository = paymentPlanRepository;
        this.loggingService = loggingService;
    }

    @Override
    public Try<Seq<Long>> subscriptionPayments(Integer daysAgo) {
        return paymentPlanService.findActivePaymentPlansWithNoPayment(daysAgo)
                .flatMap(this::handleSubscriptionPayments)
                .onFailure(peekException());
    }

    private Try<Seq<Long>> handleSubscriptionPayments(List<PaymentPlan> paymentPlans) {
        final List<Try<Long>> eitherList = paymentPlans.stream()
                .map(paymentPlan -> customerService.find(paymentPlan.customerId())
                        .flatMap(customer -> handleSubscriptionPayment(customer, paymentPlan)))
                .collect(Collectors.toList());
        return Try.sequence(eitherList);
    }

    private Try<Long> handleSubscriptionPayment(Customer customer, PaymentPlan paymentPlan) {
        final String description = paymentConfiguration.getRecurringDescription() + " " + paymentPlan.externalReference();

        final Try<CouponDiscount> couponDiscountTry = couponService.findDiscountByPaymentPlan(paymentPlan.id(), CouponDiscountStatus.VALID)
                .onFailure(peekException())
                .flatMap(optionalToNarrowedTry());

        final Try<Referral> referralTry = referralService.findFromCustomer(customer.getId(), ReferralStatus.PAID)
                .onFailure(peekException())
                .flatMap(optionalToNarrowedTry());

        final Try<Incentive> incentiveTry = incentiveService.findFromCustomer(customer.getId(), IncentiveStatus.PENDING, IncentiveType.DISCOUNT)
                .onFailure(peekException())
                .flatMap(optionalToNarrowedTry());

        final BigDecimal recurringAmount = couponDiscountTry.map(couponDiscount -> paymentPlan.recurringAmount().subtract(getDiscountedAmount(paymentPlan.recurringAmount(), couponDiscount)))
                .getOrElse(() -> referralTry.map(referral -> paymentPlan.recurringAmount().subtract(referral.getAmount()))
                .getOrElse(() -> incentiveTry.map(incentive -> paymentPlan.recurringAmount().subtract(paymentPlan.recurringAmount().multiply(incentive.getAmount())))
                .getOrElse(paymentPlan.recurringAmount())))
                .setScale(2, RoundingMode.DOWN);

        return mollieService.createRecurringPayment(customer, recurringAmount, description, paymentPlan.externalReference())
                .flatMap(paymentResponse -> paymentService.savePayment(paymentPlan.id(), paymentResponse))
                .recoverWith(MollieException.class, e -> tryToRecoverFromException(e, paymentPlan, customer))
                .map(Payment::getId)
                .flatMap(checkAndUpdateReferralIncentiveCompleted(couponDiscountTry, referralTry, incentiveTry))
                // TODO payment pending mail should be sent before creating mollie recurring payment
                // .peek(__ -> sendPaymentPendingMail(customer, paymentPlan))
                .onFailure(peekException());
    }

    private  BigDecimal getDiscountedAmount(BigDecimal amount, CouponDiscount couponDiscount) {
        return couponService.find(couponDiscount.getCouponId())
                .filter(Coupon::getPercentage)
                .map(coupon -> getMaximumDiscountedAmount(amount.multiply(couponDiscount.getAmount()), coupon.getMaximumAmount()))
                .getOrElse(couponDiscount::getAmount);
    }

    private BigDecimal getMaximumDiscountedAmount(BigDecimal discount, BigDecimal maximumAmount) {
        if (isDiscountExceedingMaximum(maximumAmount, discount)) {
            return maximumAmount;
        } else {
            return discount;
        }
    }

    private boolean isDiscountExceedingMaximum(BigDecimal maximumAmount, BigDecimal discount) {
        return maximumAmount.compareTo(BigDecimal.ZERO) > 0 && maximumAmount.compareTo(discount) < 0;
    }

    private Try<Payment> tryToRecoverFromException(MollieException e, PaymentPlan paymentPlan, Customer customer) {
        if (Integer.valueOf(422).equals(e.getDetails().get("status"))) {
            final String detail = (String) e.getDetails().get("detail");
            final PaymentResponse paymentResponse = PaymentResponse.builder()
                    .amount(new Amount(MollieServiceImpl.EUR, paymentPlan.recurringAmount()))
                    .amountRefunded(Optional.empty())
                    .status(PaymentStatus.FAILED)
                    .sequenceType(SequenceType.RECURRING)
                    .details(PaymentDetailsResponse.builder().bankReasonCode(Optional.ofNullable(detail)).build())
                    .paidAt(Optional.empty())
                    .build();
            return paymentPlanRepository.update(buildPaymentPlanCanceled(paymentPlan))
                    .peek(canceledPaymentPlan -> loggingService.create(canceledPaymentPlan.customerId(), LoggingEvent.PAYMENT_PLAN_CANCELED, "Abonnement door systeem stopgezet vanwege Mollie: " + detail))
                    .flatMap(canceledPaymentPlan -> paymentService.savePayment(canceledPaymentPlan.id(), paymentResponse))
                    .peek(payment -> notifyCustomerMissingPayment(paymentPlan, payment, customer));
        } else {
            return Try.failure(e);
        }
    }

    private void notifyCustomerMissingPayment(PaymentPlan paymentPlan, Payment payment, Customer customer) {
        emailService.sendPaymentMissing(customer.getFirstName(), customer.getEmail(), payment, paymentPlan.externalReference(), 1);
    }

    private PaymentPlan buildPaymentPlanCanceled(PaymentPlan paymentPlan) {
        return new PaymentPlan(paymentPlan.id(), paymentPlan.firstAmount(), paymentPlan.recurringAmount(),
                paymentPlan.recurringMonths(), paymentPlan.customerId(), paymentPlan.blendId(), paymentPlan.externalReference(),
                paymentPlan.creationDate(), null, PaymentPlanStatus.CANCELED, paymentPlan.paymentDate(),
                null, paymentPlan.deliveryDate(), paymentPlan.nextPaymentDate(), paymentPlan.nextDeliveryDate(), paymentPlan.paymentMethod());
    }

    private Function<Long, Try<Long>> checkAndUpdateReferralIncentiveCompleted(Try<CouponDiscount> couponDiscountTry, Try<Referral> referralTry, Try<Incentive> incentiveTry) {
        return paymentId -> couponDiscountTry.flatMap(couponDiscount -> couponService.update(buildCouponDiscount(couponDiscount, CouponDiscountStatus.COMPLETED)).map(__ -> paymentId))
                .orElse(() -> referralTry.flatMap(referral -> referralService.updateStatus(referral.getId(), ReferralStatus.COMPLETED).map(__ -> paymentId)))
                .orElse(() -> incentiveTry.flatMap(incentive -> incentiveService.updateStatus(incentive.getId(), IncentiveStatus.COMPLETED).map(__ -> paymentId)))
                .orElse(() -> Try.success(paymentId));
    }

    private CouponDiscount buildCouponDiscount(CouponDiscount couponDiscount, CouponDiscountStatus status) {
        return new CouponDiscount(couponDiscount.getId(), couponDiscount.getCouponId(), couponDiscount.getPaymentPlanId(), couponDiscount.getMonth(), couponDiscount.getAmount(), status, couponDiscount.getCreationTimestamp(), LocalDateTime.now());
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
