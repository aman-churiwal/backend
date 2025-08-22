package viteezy.service.payment;

import com.google.common.base.Throwables;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import viteezy.configuration.PaymentConfiguration;
import viteezy.controller.dto.CheckoutPatchRequest;
import viteezy.controller.dto.dashboard.PaymentPlanPatchRequest;
import viteezy.db.PaymentPlanRepository;
import viteezy.db.PaymentRepository;
import viteezy.domain.*;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.ingredient.UnitCode;
import viteezy.domain.klaviyo.KlaviyoConstant;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.pricing.Coupon;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.domain.payment.PaymentStatus;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.CustomerService;
import viteezy.service.LoggingService;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.blend.BlendService;
import viteezy.service.mail.EmailService;
import viteezy.service.pricing.CouponService;
import viteezy.service.pricing.PricingService;
import viteezy.traits.EnforcePresenceTrait;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class PaymentPlanServiceImpl implements PaymentPlanService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentPlanService.class);

    private static final String EUR = "EUR";

    private final BlendService blendService;
    private final BlendIngredientService blendIngredientService;
    private final CustomerService customerService;
    private final PaymentConfiguration paymentConfiguration;
    private final PaymentPlanRepository paymentPlanRepository;
    private final PaymentRepository paymentRepository;
    private final CouponService couponService;
    private final EmailService emailService;
    private final KlaviyoService klaviyoService;
    private final PricingService pricingService;
    private final LoggingService loggingService;

    protected PaymentPlanServiceImpl(
            BlendService blendService, BlendIngredientService blendIngredientService, CustomerService customerService, PaymentConfiguration paymentConfiguration,
            PaymentPlanRepository paymentPlanRepository, PaymentRepository paymentRepository,
            CouponService couponService, EmailService emailService, KlaviyoService klaviyoService,
            PricingService pricingService, LoggingService loggingService) {
        this.blendService = blendService;
        this.blendIngredientService = blendIngredientService;
        this.customerService = customerService;
        this.paymentConfiguration = paymentConfiguration;
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentRepository = paymentRepository;
        this.couponService = couponService;
        this.emailService = emailService;
        this.klaviyoService = klaviyoService;
        this.pricingService = pricingService;
        this.loggingService = loggingService;
    }

    @Override
    public Try<PaymentPlan> getPaymentPlan(UUID planExternalReference) {
        return paymentPlanRepository.find(planExternalReference)
                .onFailure(peekException());
    }

    @Override
    public Try<PaymentPlan> findActivePaymentPlanByBlendExternalReference(UUID blendExternalReference) {
        return blendService.find(blendExternalReference)
                .flatMap(blend -> findActivePaymentPlanByBlendId(blend.getId()));
    }

    @Override
    public Try<PaymentPlan> findActivePaymentPlanByBlendId(Long blendId) {
        return paymentPlanRepository.findByBlendId(blendId, PaymentPlanStatus.ACTIVE);
    }

    @Override
    public Try<PaymentPlan> findPaymentPlanByStatusAndCustomerExternalReference(UUID customerExternalReference, PaymentPlanStatus paymentPlanStatus) {
        return customerService.find(customerExternalReference)
                .flatMap(customer -> paymentPlanRepository.findByCustomerId(customer.getId(), paymentPlanStatus));
    }

    @Override
    public Try<List<PaymentPlan>> findActivePaymentPlansWithNoPayment(Integer daysAgo) {
        return paymentPlanRepository.findAllByStatusAndNoPayment(daysAgo, PaymentPlanStatus.ACTIVE);
    }

    @Override
    public Try<List<PaymentPlan>> findPaymentPlans(UUID customerExternalReference) {
        return customerService.find(customerExternalReference)
                .flatMap(customer -> paymentPlanRepository.findAllByCustomerId(customer.getId()));
    }

    @Override
    public Try<PaymentPlan> changeDeliveryDate(UUID planExternalReference, LocalDateTime deliveryDate) {
        return paymentPlanRepository.find(planExternalReference)
                .filter(paymentPlan -> paymentPlan.status().equals(PaymentPlanStatus.ACTIVE), () -> new IllegalArgumentException("PaymentPlan not active"))
                .filter(paymentPlan -> checkDeliveryDate(deliveryDate), () -> new IllegalArgumentException(deliveryDate.toString()))
                .filter(this::checkFirstDeliveryDate, () -> new DuplicateKeyException("first delivery"))
                .flatMap(paymentPlan -> pauseSubscription(paymentPlan, deliveryDate))
                .peek(pausedPaymentPlan -> customerService.find(pausedPaymentPlan.customerId())
                        .peek(customer -> emailService.sendChangeDeliveryDateConfirmation(customer.getEmail(), customer.getFirstName(), deliveryDate))
                        .peek(customer -> loggingService.create(customer.getId(), LoggingEvent.PAYMENT_PLAN_PAUSED, MessageFormat.format("Abonnement gepauzeerd incasso datum {0}, levering datum {1}, volgende incasso datum {2}, volgende levering datum {3}", pausedPaymentPlan.paymentDate(), pausedPaymentPlan.deliveryDate(), pausedPaymentPlan.nextPaymentDate().orElse(null), pausedPaymentPlan.nextDeliveryDate().orElse(null))))
                        .peek(customer -> klaviyoService.upsertFullProfile(customer, pausedPaymentPlan)
                                .peek(updatedCustomer -> klaviyoService.createEvent(updatedCustomer, KlaviyoConstant.PAYMENT_PLAN_PAUSED, null, Optional.of(pausedPaymentPlan.blendId())))))
                .onFailure(peekException());
    }

    @Override
    public Try<PaymentPlan> reactivate(UUID planExternalReference) {
        return paymentPlanRepository.find(planExternalReference)
                .filter(paymentPlan -> paymentPlan.status().equals(PaymentPlanStatus.STOPPED), () -> new DuplicateKeyException("PaymentPlan not activatable"))
                .filter(this::anyPaidPayment, () -> new NoSuchElementException("No paid payment found"))
                .flatMap(this::activateSubscription)
                .peek(activatedPaymentPlan -> customerService.find(activatedPaymentPlan.customerId())
                        .peek(customer -> emailService.sendReactivationConfirmation(customer.getEmail(), customer.getFirstName(), activatedPaymentPlan.deliveryDate()))
                        .peek(customer -> loggingService.create(customer.getId(), LoggingEvent.PAYMENT_PLAN_REACTIVATED, "Abonnement geheractiveerd"))
                        .peek(customer -> klaviyoService.upsertFullProfile(customer, activatedPaymentPlan)
                                .peek(updatedCustomer -> klaviyoService.createEvent(updatedCustomer, KlaviyoConstant.PAYMENT_PLAN_REACTIVATED, null, Optional.of(activatedPaymentPlan.blendId())))))
                .onFailure(peekException());
    }

    @Override
    public Try<PaymentPlan> stop(UUID planExternalReference, String stopReason) {
        return paymentPlanRepository.find(planExternalReference)
                .filter(paymentPlan -> paymentPlan.status().equals(PaymentPlanStatus.ACTIVE), () -> new IllegalArgumentException("PaymentPlan not active"))
                .flatMap(paymentPlan -> customerService.find(paymentPlan.customerId())
                        .flatMap(customer -> paymentPlanRepository.update(buildStoppedPaymentPlan(paymentPlan, stopReason))
                                .peek(stoppedPaymentPlan -> emailService.sendStopConfirmation(customer.getEmail(), customer.getFirstName()))
                                .peek(stoppedPaymentPlan -> loggingService.create(customer.getId(), LoggingEvent.PAYMENT_PLAN_STOPPED, "Abonnement stopgezet met reden: " + stopReason))
                                .peek(stoppedPaymentPlan -> klaviyoService.upsertFullProfile(customer, stoppedPaymentPlan)
                                        .peek(updatedCustomer -> klaviyoService.createEvent(updatedCustomer, KlaviyoConstant.PAYMENT_PLAN_STOPPED, stopReason, Optional.of(paymentPlan.blendId()))))))
                .onFailure(peekException());
    }

    @Override
    public Try<PaymentPlan> updateByBlend(UUID blendExternalReference, CheckoutPatchRequest checkoutPatchRequest) {
        return blendService.find(blendExternalReference)
                .filter(blend -> isNotActiveWithinPaymentDate(blend.getId()), () -> new DuplicateKeyException("Payment plan is active and within payment date and delivery date"))
                .flatMap(blend -> findActivePaymentPlanByBlendId(blend.getId())
                        .orElse(() -> paymentPlanRepository.findByCustomerId(blend.getCustomerId(), PaymentPlanStatus.ACTIVE))
                        .map(paymentPlan -> new Tuple2<>(blend, paymentPlan)))
                .flatMap(tuple2 -> updateSubscription(tuple2._1, tuple2._2, checkoutPatchRequest.getMonthsSubscribed()))
                .peek(updatedPaymentPlan -> customerService.find(updatedPaymentPlan.customerId())
                        .peek(customer -> emailService.sendReactivationConfirmation(customer.getEmail(), customer.getFirstName(), updatedPaymentPlan.deliveryDate()))
                        .peek(customer -> loggingService.create(customer.getId(), LoggingEvent.PAYMENT_PLAN_UPDATED, MessageFormat.format("Abonnement geupdate naar aantal maanden: {0} terugkerend bedrag: {1}", updatedPaymentPlan.recurringMonths(), updatedPaymentPlan.recurringAmount())))
                        .peek(customer -> klaviyoService.upsertFullProfile(customer, updatedPaymentPlan)))
                .onFailure(peekException());
    }

    @Override
    public Try<PaymentPlan> updateSubscription(Blend blend, PaymentPlan paymentPlan, Integer monthsSubscribed) {
        final Integer recurringMonths = Optional.ofNullable(monthsSubscribed).orElse(paymentPlan.recurringMonths());
        final String couponCode = couponService.findCouponUsedByPaymentPlan(paymentPlan.id())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(couponUsed -> couponService.find(couponUsed.getCouponId()))
                .map(Coupon::getCouponCode)
                .getOrElse(() -> null);

        return pricingService.getBlendPricing(blend.getExternalReference(), Optional.empty(), couponCode, recurringMonths, true)
                .flatMap(pricing -> conditionallyUpdateOrCreateNewPaymentPlan(buildPaymentPlan(paymentPlan, pricing.getRecurringAmount(), recurringMonths), blend.getId()))
                .peek(updatedPaymentPlan -> customerService.find(updatedPaymentPlan.customerId())
                        .peek(customer -> klaviyoService.upsertFullProfile(customer, updatedPaymentPlan))
                );
    }

    @Override
    public Try<PaymentPlan> updateStatus(PaymentPlanPatchRequest paymentPlanPatchRequest) {
        return paymentPlanRepository.find(paymentPlanPatchRequest.getId())
                .flatMap(paymentPlan -> paymentPlanRepository.update(buildPaymentPlanWithStatus(paymentPlan, paymentPlanPatchRequest.getPaymentPlanStatus())));
    }

    @Override
    public Try<PaymentPlan> applyRecurringCoupon(UUID planExternalReference, String couponCode) {
        return paymentPlanRepository.find(planExternalReference)
                .flatMap(paymentPlan -> couponService.findValid(couponCode, paymentPlan.recurringMonths(), paymentPlan.recurringAmount())
                        .flatMap(coupon -> paymentPlanRepository.update(buildPaymentPlan(paymentPlan, paymentPlan.recurringAmount().subtract(getCouponRecurringDiscount(paymentPlan, coupon)), paymentPlan.recurringMonths()))));
    }

    @Override
    public Try<Void> updatePaymentDateWithNextPaymentDate() {
        return paymentPlanRepository.updatePaymentDateWithNextPaymentDate()
                .onFailure(peekException());
    }

    @Override
    public Try<Void> updateDeliveryDateWithNextDeliveryDate() {
        return paymentPlanRepository.updateDeliveryDateWithNextDeliveryDate()
                .onFailure(peekException());
    }

    @Override
    public Try<Void> updatePaymentDate() {
        return paymentPlanRepository.updatePaymentDate()
                .onFailure(peekException());
    }

    @Override
    public Try<Void> updateDeliveryDate() {
        return paymentPlanRepository.updateDeliveryDate()
                .onFailure(peekException());
    }

    private boolean anyPaidPayment(PaymentPlan paymentPlan) {
        return paymentRepository.findByPaymentPlanId(paymentPlan.id())
                .map(payments -> payments.stream().anyMatch(payment -> payment.getStatus().equals(PaymentStatus.paid)))
                .getOrElse(false);
    }

    private Try<PaymentPlan> activateSubscription(PaymentPlan paymentPlan) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime finalPaymentDate;

        if (paymentPlan.paymentDate().isBefore(now)) {
            finalPaymentDate = now;
        } else {
            finalPaymentDate = paymentPlan.paymentDate();
        }
        final LocalDateTime deliveryDate = finalPaymentDate.plusDays(paymentConfiguration.getPaymentProcessingInDays() + paymentConfiguration.getDeliveryDateInDays());
        return paymentPlanRepository.update(buildActivePaymentPlan(paymentPlan, finalPaymentDate, deliveryDate,
                paymentPlan.nextPaymentDate(), paymentPlan.nextDeliveryDate()));
    }

    private Try<PaymentPlan> pauseSubscription(PaymentPlan paymentPlan, LocalDateTime deliveryDate) {
        final LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(paymentPlan.paymentDate()) && now.isBefore(paymentPlan.deliveryDate())) {
            // Mollie payment has already been created, update next payment/delivery date
            final LocalDateTime paymentDate = deliveryDate.minusDays(paymentConfiguration.getPaymentProcessingInDays() + paymentConfiguration.getDeliveryDateInDays());
            return paymentPlanRepository.update(buildActivePaymentPlan(paymentPlan, paymentPlan.paymentDate(), paymentPlan.deliveryDate(), Optional.of(paymentDate), Optional.of(deliveryDate)));
        } else {
            final LocalDateTime paymentDate = deliveryDate.minusDays(paymentConfiguration.getPaymentProcessingInDays() + paymentConfiguration.getDeliveryDateInDays());
            return paymentPlanRepository.update(buildActivePaymentPlan(paymentPlan, paymentDate, deliveryDate, Optional.empty(), Optional.empty()));
        }

    }

    private boolean isNotActiveWithinPaymentDate(Long blendId) {
        final LocalDateTime now = LocalDateTime.now();
        return findActivePaymentPlanByBlendId(blendId)
                .map(paymentPlan -> !(now.isAfter(paymentPlan.paymentDate().plusDays(1)) && now.isBefore(paymentPlan.deliveryDate())))
                .getOrElse(true);
    }

    private Boolean checkDeliveryDate(LocalDateTime deliveryDate) {
        final LocalDateTime now = LocalDateTime.now();
        return deliveryDate.isAfter(now)
                && deliveryDate.isBefore(now.plusYears(1))
                && deliveryDate.isAfter(now.plusDays(paymentConfiguration.getPaymentProcessingInDays() + paymentConfiguration.getDeliveryDateInDays()));
    }

    private Boolean checkFirstDeliveryDate(PaymentPlan paymentPlan) {
        final LocalDateTime firstDeliveryDate = paymentPlan.creationDate().plusDays(paymentConfiguration.getPaymentProcessingInDays() + paymentConfiguration.getDeliveryDateInDays());
        return LocalDateTime.now().isAfter(firstDeliveryDate);
    }

    private Try<PaymentPlan> conditionallyUpdateOrCreateNewPaymentPlan(PaymentPlan paymentPlan, Long blendId) {
        if (paymentPlan.blendId().equals(blendId)) {
            return paymentPlanRepository.update(buildPaymentPlan(paymentPlan, blendId, paymentPlan.recurringAmount(), PaymentPlanStatus.ACTIVE, paymentPlan.externalReference()));
        } else {
            return paymentPlanRepository.update(buildPaymentPlan(paymentPlan, paymentPlan.blendId(), paymentPlan.recurringAmount(), PaymentPlanStatus.COMPLETED, paymentPlan.externalReference()))
                    .flatMap(__ -> paymentPlanRepository.saveCopy(buildPaymentPlan(paymentPlan, blendId, paymentPlan.recurringAmount(), PaymentPlanStatus.ACTIVE, UUID.randomUUID())));
        }
    }

    private BigDecimal getCouponRecurringDiscount(PaymentPlan paymentPlan, Coupon coupon) {
        if (coupon.getIngredientId().isPresent()) {
            return blendIngredientService.save(buildBlendIngredient(paymentPlan.blendId(), coupon.getIngredientId().get()))
                    .map(BlendIngredient::getPrice)
                    .getOrElse(() -> BigDecimal.ZERO);
        } else if (coupon.getPercentage()) {
            return getPercentageDiscountedAmount(paymentPlan.recurringAmount(), coupon);
        } else {
            return coupon.getAmount();
        }
    }

    private BigDecimal getPercentageDiscountedAmount(BigDecimal amount, Coupon coupon) {
        BigDecimal discount = amount.multiply(coupon.getAmount());
        if (isDiscountExceedingMaximum(coupon, discount)) {
            return coupon.getMaximumAmount();
        } else {
            return discount;
        }
    }

    private boolean isDiscountExceedingMaximum(Coupon coupon, BigDecimal discount) {
        return coupon.getMaximumAmount().compareTo(BigDecimal.ZERO) > 0
                && coupon.getMaximumAmount().compareTo(discount) < 0;
    }

    private PaymentPlan buildPaymentPlan(PaymentPlan paymentPlan, Long blendId, BigDecimal recurringAmount,
                                         PaymentPlanStatus paymentPlanStatus, UUID paymentPlanExternalReference) {
        return new PaymentPlan(paymentPlan.id(), paymentPlan.firstAmount(), recurringAmount,
                paymentPlan.recurringMonths(), paymentPlan.customerId(), blendId, paymentPlanExternalReference,
                paymentPlan.creationDate(), null, paymentPlanStatus, paymentPlan.paymentDate(),
                null, paymentPlan.deliveryDate(), paymentPlan.nextPaymentDate(), paymentPlan.nextDeliveryDate(), paymentPlan.paymentMethod());
    }

    private PaymentPlan buildPaymentPlan(PaymentPlan paymentPlan, BigDecimal recurringAmount, Integer recurringMonths) {
        return new PaymentPlan(paymentPlan.id(), paymentPlan.firstAmount(), recurringAmount,
                recurringMonths, paymentPlan.customerId(), paymentPlan.blendId(),
                paymentPlan.externalReference(), paymentPlan.creationDate(), paymentPlan.lastModified(),
                paymentPlan .status(), paymentPlan.paymentDate(), paymentPlan.stopReason(),
                paymentPlan.deliveryDate(), paymentPlan.nextPaymentDate(), paymentPlan.nextDeliveryDate(), paymentPlan.paymentMethod());
    }

    private PaymentPlan buildStoppedPaymentPlan(PaymentPlan paymentPlan, String reason) {
        return new PaymentPlan(paymentPlan.id(), paymentPlan.firstAmount(), paymentPlan.recurringAmount(),
                paymentPlan.recurringMonths(), paymentPlan.customerId(), paymentPlan.blendId(), paymentPlan.externalReference(),
                paymentPlan.creationDate(), null, PaymentPlanStatus.STOPPED, paymentPlan.paymentDate(),
                reason, paymentPlan.deliveryDate(), Optional.empty(), Optional.empty(), paymentPlan.paymentMethod());
    }

    private PaymentPlan buildActivePaymentPlan(PaymentPlan paymentPlan, LocalDateTime paymentDate, LocalDateTime deliveryDate,
                                               Optional<LocalDateTime> nextPaymentDate, Optional<LocalDateTime> nextDeliveryDate) {
        return new PaymentPlan(paymentPlan.id(), paymentPlan.firstAmount(), paymentPlan.recurringAmount(),
                paymentPlan.recurringMonths(), paymentPlan.customerId(), paymentPlan.blendId(),
                paymentPlan.externalReference(), paymentPlan.creationDate(), null,
                PaymentPlanStatus.ACTIVE, paymentDate, null, deliveryDate, nextPaymentDate, nextDeliveryDate, paymentPlan.paymentMethod());
    }

    private PaymentPlan buildPaymentPlanWithStatus(PaymentPlan paymentPlan, PaymentPlanStatus paymentStatus) {
        return new PaymentPlan(paymentPlan.id(), paymentPlan.firstAmount(), paymentPlan.recurringAmount(),
                paymentPlan.recurringMonths(), paymentPlan.customerId(), paymentPlan.blendId(),
                paymentPlan.externalReference(), paymentPlan.creationDate(), null, paymentStatus,
                paymentPlan.paymentDate(), paymentPlan.stopReason(), paymentPlan.deliveryDate(),
                paymentPlan.nextPaymentDate(), paymentPlan.nextDeliveryDate(), paymentPlan.paymentMethod());
    }

    private BlendIngredient buildBlendIngredient(Long blendId, Long ingredientId) {
        return BlendIngredient.addPrice(BlendIngredient.buildV2(ingredientId, blendId, BigDecimal.TEN, UnitCode.G, null, false, null), BigDecimal.ZERO, EUR);
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
