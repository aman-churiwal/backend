package viteezy.service.payment;

import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.method.MethodResponse;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.data.payment.SequenceType;
import com.google.common.base.Throwables;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import viteezy.configuration.PaymentConfiguration;
import viteezy.controller.dto.CheckoutPostRequest;
import viteezy.controller.dto.dashboard.PaymentPatchRequest;
import viteezy.db.PaymentPlanRepository;
import viteezy.db.PaymentRepository;
import viteezy.domain.*;
import viteezy.domain.blend.Blend;
import viteezy.domain.klaviyo.KlaviyoConstant;
import viteezy.domain.payment.*;
import viteezy.domain.pricing.Pricing;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.CustomerService;
import viteezy.service.blend.BlendService;
import viteezy.service.pricing.CouponService;
import viteezy.service.pricing.PricingService;
import viteezy.service.pricing.ReferralService;
import viteezy.traits.EnforcePresenceTrait;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PaymentServiceImpl implements PaymentService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    private final MollieService mollieService;
    private final BlendService blendService;
    private final CustomerService customerService;
    private final PaymentConfiguration paymentConfiguration;
    private final PaymentPlanRepository paymentPlanRepository;
    private final PaymentRepository paymentRepository;
    private final CouponService couponService;
    private final KlaviyoService klaviyoService;
    private final PricingService pricingService;
    private final ReferralService referralService;

    protected PaymentServiceImpl(
            MollieService mollieService, BlendService blendService, CustomerService customerService,
            PaymentConfiguration paymentConfiguration, PaymentPlanRepository paymentPlanRepository,
            PaymentRepository paymentRepository, CouponService couponService,
            KlaviyoService klaviyoService, PricingService pricingService, ReferralService referralService) {
        this.mollieService = mollieService;
        this.blendService = blendService;
        this.customerService = customerService;
        this.paymentConfiguration = paymentConfiguration;
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentRepository = paymentRepository;
        this.couponService = couponService;
        this.klaviyoService = klaviyoService;
        this.pricingService = pricingService;
        this.referralService = referralService;
    }

    @Override
    public Payment buildPayment(Long paymentPlanId, PaymentResponse paymentResponse) {
        final PaymentStatus status;
        final Optional<BigDecimal> refund = getRefund(paymentResponse);
        final BigDecimal amount = refund.map(refundAmount -> paymentResponse.getAmount().getValue().subtract(refundAmount)).orElse(paymentResponse.getAmount().getValue());
        if (be.woutschoovaerts.mollie.data.payment.PaymentStatus.PAID.equals(paymentResponse.getStatus()) && paymentResponse.getLinks() != null
                && paymentResponse.getLinks().getChargebacks().isPresent()) {
            status = PaymentStatus.chargeback;
        } else {
            status = refund.map(__ -> PaymentStatus.refund).orElse(PaymentStatus.valueOf(paymentResponse.getStatus().getJsonValue()));;
        }
        final String reason = Optional.ofNullable(paymentResponse.getDetails()).map(details -> details.getBankReasonCode().orElse(refund.map(amountValue -> "Refund " + amountValue).orElse(null))).orElse(null);
        final String retriedMolliePaymentId;
        if (paymentResponse.getSequenceType().equals(SequenceType.FIRST) && paymentResponse.getMetadata().containsKey(MollieMetadataKeys.PAYMENT_ID)) {
            retriedMolliePaymentId = (String) paymentResponse.getMetadata().get(MollieMetadataKeys.PAYMENT_ID);
        } else {
            retriedMolliePaymentId = null;
        }

        return buildPayment(amount, paymentPlanId, paymentResponse, status, reason, retriedMolliePaymentId);
    }

    @Override
    public Try<Payment> getByMolliePaymentId(String molliePaymentId) {
        return paymentRepository.findByMolliePaymentId(molliePaymentId);
    }

    @Override
    public Try<Payment> getRetryPayment(UUID planExternalReference) {
        return paymentPlanRepository.find(planExternalReference)
                .flatMap(paymentPlan -> paymentRepository.findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id()))
                .map(this::filterPaidRetryPayments)
                .filter(Optional::isPresent, () -> new DuplicateKeyException("All retried payments are paid"))
                .flatMap(optionalToNarrowedTry());
    }

    @Override
    public Try<List<Payment>> getPayments(UUID planExternalReference) {
        return paymentPlanRepository.find(planExternalReference)
                .flatMap(paymentPlan -> paymentRepository.findByPaymentPlanId(paymentPlan.id()));
    }

    @Override
    public Try<List<Payment>> getByCustomerExternalReference(UUID customerExternalReference) {
        return customerService.find(customerExternalReference)
                .flatMap(customer -> paymentRepository.findByCustomerId(customer.getId()));
    }

    @Override
    public Try<List<Payment>> getByBlendExternalReference(UUID blendExternalReference) {
        return blendService.find(blendExternalReference)
                .flatMap(blend -> paymentRepository.findByCustomerId(blend.getCustomerId()));
    }

    @Override
    public Try<List<MethodResponse>> getPaymentMethods(String country) {
        return mollieService.getPaymentMethods(country);
    }

    @Override
    public Try<PaymentResponse> create(UUID blendExternalReference, CheckoutPostRequest checkoutPostRequest, String facebookClick, String gaCookie) {
        return blendService.find(blendExternalReference)
                .filter(this::filterActivePaymentPlan, () -> new DuplicateKeyException("Customer has active payment plan"))
                .flatMap(blend -> pricingService.getBlendPricing(blend.getExternalReference(), Optional.empty(), checkoutPostRequest.getCouponCode(), checkoutPostRequest.getMonthsSubscribed(), checkoutPostRequest.getSubscription())
                        .flatMap(pricing -> paymentPlanRepository.save(buildFirstPaymentPlan(blend, checkoutPostRequest, pricing))
                                .flatMap(saveCouponUsed(checkoutPostRequest.getCouponCode()))
                                .flatMap(paymentPlan -> referralService.createPendingReferral(checkoutPostRequest.getCouponCode(), pricing.getReferralDiscount(), blend.getCustomerId()).map(aLong -> new Tuple2<>(paymentPlan, aLong)))
                                .flatMap(paymentPlanReferralId -> customerService.find(paymentPlanReferralId._1.customerId())
                                        .flatMap(customer -> klaviyoService.upsertFullProfile(customer, paymentPlanReferralId._1))
                                        .peek(customer -> klaviyoService.createEvent(customer, KlaviyoConstant.STARTED_CHECKOUT, null, Optional.of(blend.getId())))
                                        .flatMap(customer -> createFirstPayment(customer, paymentPlanReferralId._1, paymentPlanReferralId._2, checkoutPostRequest, facebookClick, gaCookie))
                                )
                .onFailure(peekException())));
    }

    @Override
    public Try<PaymentResponse> createRetryFirstPayment(UUID planExternalReference) {
        return paymentPlanRepository.find(planExternalReference)
                .flatMap(paymentPlan -> paymentRepository.findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id())
                        .map(this::filterPaidRetryPayments)
                        .filter(Optional::isPresent, () -> new DuplicateKeyException("All retried payments are paid"))
                        .flatMap(payment -> customerService.find(paymentPlan.customerId()).map(customer -> new Tuple2<>(payment.get(), customer)))
                        .flatMap(paymentCustomerTuple -> mollieService.createRetryFirstPayment(paymentCustomerTuple._2, paymentPlan, paymentCustomerTuple._1)));
    }

    @Override
    public Try<Payment> save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Try<Payment> savePayment(Long paymentPlanId, PaymentResponse paymentResponse) {
        return paymentRepository.save(buildPayment(paymentPlanId, paymentResponse));
    }

    @Override
    public Try<Payment> updateStatus(PaymentPatchRequest paymentPatchRequest) {
        return paymentRepository.find(paymentPatchRequest.getId())
                .flatMap(payment -> paymentRepository.save(buildPaymentWithStatus(payment, paymentPatchRequest.getStatus())));
    }

    private boolean filterActivePaymentPlan(Blend blend) {
        return paymentPlanRepository.findByBlendId(blend.getId(), PaymentPlanStatus.ACTIVE)
                .map(paymentPlan -> !PaymentPlanStatus.ACTIVE.equals(paymentPlan.status()))
                .getOrElse(true);
    }

    private Optional<Payment> filterPaidRetryPayments(List<Payment> payments) {
        for (Payment payment: payments) {
            Boolean retriedPaymentPaid = paymentRepository.findByRetriedMolliePaymentId(payment.getMolliePaymentId())
                    .map(payments1 -> payments1.stream().anyMatch(payment1 -> payment1.getStatus().equals(PaymentStatus.paid)))
                    .getOrElse(false);
            if (!retriedPaymentPaid) {
                return Optional.of(payment);
            }
        }
        return Optional.empty();
    }

    private Function<PaymentPlan, Try<PaymentPlan>> saveCouponUsed(String couponCode) {
        return paymentPlan -> {
            if (StringUtils.isNotBlank(couponCode)) {
                return couponService.setCouponUsedForCustomerPaymentPlan(couponCode, paymentPlan)
                        .map(__ -> paymentPlan);
            } else {
                return Try.success(paymentPlan);
            }
        };
    }

    private Try<PaymentResponse> createFirstPayment(Customer customer, PaymentPlan paymentPlan, Optional<Long> referralId,
                                                    CheckoutPostRequest checkoutPostRequest, String facebookClick, String gaCookie) {
        final BigDecimal amount = paymentPlan.firstAmount();
        final UUID externalReference = paymentPlan.externalReference();
        final String firstDescription = paymentConfiguration.getFirstDescription() + " " + externalReference;
        final String method = checkoutPostRequest.getMethod();
        return mollieService.createCustomerPayment(customer, amount, firstDescription, externalReference, referralId, method, facebookClick, gaCookie);
    }

    private Optional<BigDecimal> getRefund(PaymentResponse paymentResponse) {
        return paymentResponse.getAmountRefunded()
                .filter(amount -> amount.getValue() != null && amount.getValue().compareTo(BigDecimal.ZERO) > 0)
                .map(Amount::getValue);
    }

    private Payment buildPayment(BigDecimal amount, Long paymentPlanId, PaymentResponse paymentResponse, PaymentStatus status,
                                 String reason, String retriedMolliePaymentId) {
        final String molliePaymentId = paymentResponse.getId();
        final SequenceType sequenceType = paymentResponse.getSequenceType();
        final LocalDateTime creationLocalDateTime = paymentResponse.getCreatedAt() != null ? paymentResponse.getCreatedAt().toLocalDateTime() : LocalDateTime.now();
        final LocalDateTime paymentDate = paymentResponse.getPaidAt().map(OffsetDateTime::toLocalDateTime).orElse(null);
        return new Payment(null, amount, molliePaymentId, retriedMolliePaymentId, paymentPlanId, creationLocalDateTime, paymentDate, null, status, reason, sequenceType);
    }

    private Payment buildPaymentWithStatus(Payment payment, PaymentStatus paymentStatus) {
        return new Payment(payment.getId(), payment.getAmount(), payment.getMolliePaymentId(),
                payment.getRetriedMolliePaymentId(), payment.getPaymentPlanId(), payment.getCreationDate(),
                payment.getPaymentDate(), LocalDateTime.now(), paymentStatus, payment.getReason(),
                payment.getSequenceType());
    }

    private PaymentPlan buildFirstPaymentPlan(Blend blend, CheckoutPostRequest checkoutPostRequest, Pricing pricing) {
        final int recurringMonths = checkoutPostRequest.getMonthsSubscribed() > 1 ? checkoutPostRequest.getMonthsSubscribed() : 1;
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime deliveryDate = now.plusDays(paymentConfiguration.getDeliveryDateInDays());
        final LocalDateTime paymentDate = now.plusMonths(recurringMonths - 1).plusDays(paymentConfiguration.getStartDateInDays());
        final BigDecimal firstAmount = pricing.getFirstAmount();
        final BigDecimal recurringAmount = pricing.getRecurringAmount();
        final PaymentPlanStatus paymentPlanStatus = checkoutPostRequest.getSubscription() != null && !checkoutPostRequest.getSubscription() ? PaymentPlanStatus.PENDING_SINGLE_BUY : PaymentPlanStatus.PENDING;
        return new PaymentPlan(null, firstAmount, recurringAmount, recurringMonths, blend.getCustomerId(), blend.getId(),
                UUID.randomUUID(), now, now, paymentPlanStatus, paymentDate, null, deliveryDate, Optional.empty(), Optional.empty(), checkoutPostRequest.getMethod());
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
