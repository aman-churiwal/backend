package viteezy.service.payment;

import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.payment.PaymentDetailsResponse;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.data.payment.SequenceType;
import io.vavr.control.Try;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;
import viteezy.configuration.PaymentConfiguration;
import viteezy.controller.dto.CheckoutPostRequest;
import viteezy.db.PaymentPlanRepository;
import viteezy.db.PaymentRepository;
import viteezy.domain.*;
import viteezy.domain.blend.Blend;
import viteezy.domain.klaviyo.KlaviyoConstant;
import viteezy.domain.payment.*;
import viteezy.domain.pricing.Pricing;
import viteezy.domain.pricing.Referral;
import viteezy.domain.blend.BlendStatus;
import viteezy.domain.pricing.ReferralStatus;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.CustomerService;
import viteezy.service.blend.BlendService;
import viteezy.service.pricing.CouponService;
import viteezy.service.pricing.PricingService;
import viteezy.service.pricing.ReferralService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    private static final Long QUIZ_ID = 2L;
    private static final Long BLEND_ID = 3L;
    private static final Long CUSTOMER_ID = 4L;
    private static final Long PAYMENT_PLAN_ID = 5L;
    private static final long PAYMENT_PLAN_ID_3 = 6L;
    private static final Long PAYMENT_ID = 7L;
    private static final Long REFERRAL_ID = 9L;
    private static final UUID BLEND_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final Integer MONTHS_SUBSCRIBED = 1;
    private static final Integer MONTHS_SUBSCRIBED_3 = 3;
    private static final BigDecimal AMOUNT_VALUE = BigDecimal.valueOf(20L);
    private static final BigDecimal AMOUNT_VALUE_3 = AMOUNT_VALUE.multiply(new BigDecimal(MONTHS_SUBSCRIBED_3)); // 3 * 20
    private static final BigDecimal AMOUNT_VALUE_3_DISCOUNTED = BigDecimal.valueOf(51L); // 3 * 20 * 15% discount
    private static final BigDecimal AMOUNT_VALUE_COUPON = BigDecimal.valueOf(16L);
    private static final Amount AMOUNT = new Amount("currency", AMOUNT_VALUE);
    private static final Amount AMOUNT_3 = new Amount("currency", AMOUNT_VALUE_3);
    private static final Amount AMOUNT_COUPON = new Amount("currency", AMOUNT_VALUE_COUPON);
    private static final String REFERRAL_CODE = "referralCode";
    private static final Customer CUSTOMER = new Customer(CUSTOMER_ID, "email", true, UUID.randomUUID(),
            "mollieCustomerId", null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, "BE",
            REFERRAL_CODE, LocalDateTime.now(), LocalDateTime.now());
    private static final Blend BLEND = new Blend(BLEND_ID, BlendStatus.CREATED, BLEND_EXTERNAL_REFERENCE, CUSTOMER_ID, QUIZ_ID, LocalDateTime.now(), LocalDateTime.now());
    private static final Pricing PRICING = new Pricing(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
    private static final String COUPON_CODE = "couponCode";
    private static final CheckoutPostRequest CHECKOUT_POST_REQUEST = new CheckoutPostRequest(null, MONTHS_SUBSCRIBED, true,null, null);
    private static final CheckoutPostRequest CHECKOUT_POST_REQUEST_COUPON = new CheckoutPostRequest(COUPON_CODE, MONTHS_SUBSCRIBED, true, null, null);
    private static final CheckoutPostRequest CHECKOUT_POST_REQUEST_3 = new CheckoutPostRequest(null, MONTHS_SUBSCRIBED_3, true, null, null);
    private static final Referral REFERRAL = new Referral(REFERRAL_ID, CUSTOMER_ID, CUSTOMER_ID, BigDecimal.TEN, ReferralStatus.PENDING, LocalDateTime.now(), LocalDateTime.now());
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final String FACEBOOK_CLICK = "fb.1.1234567890123.AbCdEfGhIjKlMnOpQrStUvWxYz1234567890";
    private static final String GA_SESSION_ID = "GS1.1.1234567890.13.0.1234567890.60.0.0";
    private static final PaymentResponse PAYMENT_RESPONSE = mock(PaymentResponse.class);
    private static final UUID PAYMENT_PLAN_EXTERNALREFERENCE = UUID.randomUUID();
    private static final Map<String, Object> METADATA = Map.of(MollieMetadataKeys.PAYMENT_PLAN_ID, PAYMENT_PLAN_EXTERNALREFERENCE.toString(), MollieMetadataKeys.FACEBOOK_CLICK_ID, FACEBOOK_CLICK, MollieMetadataKeys.GA_SESSION_ID, GA_SESSION_ID);
    private static final OffsetDateTime NOW = OffsetDateTime.now();
    private static final LocalDateTime NOW_LD = NOW.toLocalDateTime();
    private static final String MOLLIE_PAYMENT_ID = "molliePaymentId";

    private final MollieService mollieService = mock(MollieService.class);
    private final BlendService blendService = mock(BlendService.class);
    private final CustomerService customerService = mock(CustomerService.class);
    private final PaymentConfiguration paymentConfiguration = mock(PaymentConfiguration.class);
    private final PaymentPlanRepository paymentPlanRepository = mock(PaymentPlanRepository.class);
    private final PaymentRepository paymentRepository = mock(PaymentRepository.class);
    private final CouponService couponService = mock(CouponService.class);
    private final KlaviyoService klaviyoService = Mockito.mock(KlaviyoService.class);
    private final PricingService pricingService = Mockito.mock(PricingService.class);
    private final ReferralService referralService = Mockito.mock(ReferralService.class);
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(
                mollieService,
                blendService,
                customerService,
                paymentConfiguration,
                paymentPlanRepository,
                paymentRepository,
                couponService,
                klaviyoService,
                pricingService,
                referralService);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
                mollieService,
                blendService,
                customerService,
                paymentPlanRepository,
                paymentRepository,
                couponService,
                klaviyoService,
                pricingService,
                referralService
        );

        reset(
                mollieService,
                blendService,
                customerService,
                paymentPlanRepository,
                paymentRepository,
                couponService,
                klaviyoService,
                pricingService,
                referralService
        );
    }

    @Test
    void createFailBlendThrowable() {
        when(blendService.find(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.failure(EXCEPTION));

        final Try<PaymentResponse> paymentResponse = paymentService.create(BLEND_EXTERNAL_REFERENCE, CHECKOUT_POST_REQUEST, FACEBOOK_CLICK, GA_SESSION_ID);

        assertTrue(paymentResponse.isFailure());
        assertTrue(paymentResponse.getCause() instanceof CloseException);
        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);
    }

    @Test
    void createFailPaymentPlanThrowable() {
        when(blendService.find(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND));
        when(paymentPlanRepository.findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(null));
        when(pricingService.getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST_3.getCouponCode(), CHECKOUT_POST_REQUEST_3.getMonthsSubscribed(), CHECKOUT_POST_REQUEST_3.getSubscription()))
                .thenReturn(Try.success(PRICING));
        when(paymentPlanRepository.save(any(PaymentPlan.class)))
                .thenReturn(Try.failure(EXCEPTION));

        final Try<PaymentResponse> paymentResponse = paymentService.create(BLEND_EXTERNAL_REFERENCE, CHECKOUT_POST_REQUEST_3, FACEBOOK_CLICK, GA_SESSION_ID);

        assertTrue(paymentResponse.isFailure());
        assertTrue(paymentResponse.getCause() instanceof CloseException);

        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);

        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);
        verify(paymentPlanRepository).findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE);
        verify(pricingService).getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST_3.getCouponCode(), CHECKOUT_POST_REQUEST_3.getMonthsSubscribed(), CHECKOUT_POST_REQUEST_3.getSubscription());
        verify(paymentPlanRepository).save(any(PaymentPlan.class));
    }

    @Test
    void createFailMollieThrowable() {
        PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE_3, MONTHS_SUBSCRIBED);

        when(blendService.find(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND));
        when(paymentPlanRepository.findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(null));
        when(pricingService.getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST_3.getCouponCode(), CHECKOUT_POST_REQUEST_3.getMonthsSubscribed(), CHECKOUT_POST_REQUEST_3.getSubscription()))
                .thenReturn(Try.success(PRICING));
        when(customerService.find(BLEND.getCustomerId()))
                .thenReturn(Try.success(CUSTOMER));
        when(paymentPlanRepository.save(any(PaymentPlan.class)))
                .thenReturn(Try.success(paymentPlan));
        when(referralService.createPendingReferral(CHECKOUT_POST_REQUEST_3.getCouponCode(), PRICING.getReferralDiscount(), BLEND.getCustomerId()))
                .thenReturn(Try.success(Optional.empty()));
        when(mollieService.createCustomerPayment(CUSTOMER, AMOUNT_VALUE_3,
                paymentConfiguration.getFirstDescription() + " " + paymentPlan.externalReference(),
                paymentPlan.externalReference(), Optional.empty(), null, FACEBOOK_CLICK, GA_SESSION_ID))
                .thenReturn(Try.failure(EXCEPTION));
        when(klaviyoService.upsertFullProfile(CUSTOMER, paymentPlan))
                .thenReturn(Try.success(CUSTOMER));
        doNothing().when(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.STARTED_CHECKOUT, null, Optional.of(BLEND.getId()));

        final Try<PaymentResponse> paymentResponse = paymentService.create(BLEND_EXTERNAL_REFERENCE, CHECKOUT_POST_REQUEST_3, FACEBOOK_CLICK, GA_SESSION_ID);

        assertTrue(paymentResponse.isFailure());
        assertTrue(paymentResponse.getCause() instanceof CloseException);

        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);
        verify(paymentPlanRepository).findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE);
        verify(pricingService).getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST_3.getCouponCode(), CHECKOUT_POST_REQUEST_3.getMonthsSubscribed(), CHECKOUT_POST_REQUEST_3.getSubscription());
        verify(paymentPlanRepository).save(any(PaymentPlan.class));
        verify(referralService).createPendingReferral(CHECKOUT_POST_REQUEST_3.getCouponCode(), PRICING.getReferralDiscount(), BLEND.getCustomerId());
        verify(customerService).find(BLEND.getCustomerId());
        verify(mollieService).createCustomerPayment(CUSTOMER, AMOUNT_VALUE_3,
                paymentConfiguration.getFirstDescription() + " " + paymentPlan.externalReference(),
                paymentPlan.externalReference(), Optional.empty(), null, FACEBOOK_CLICK, GA_SESSION_ID);
        verify(klaviyoService).upsertFullProfile(CUSTOMER, paymentPlan);
        verify(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.STARTED_CHECKOUT, null, Optional.of(BLEND.getId()));
    }

    @Test
    void createOk1() {
        final PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE, MONTHS_SUBSCRIBED);
        final PaymentResponse paymentResponse = buildPaymentResponse(AMOUNT, METADATA);

        when(blendService.find(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND));
        when(paymentPlanRepository.findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(null));
        when(pricingService.getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST.getCouponCode(), CHECKOUT_POST_REQUEST.getMonthsSubscribed(), CHECKOUT_POST_REQUEST.getSubscription()))
                .thenReturn(Try.success(PRICING));
        when(paymentPlanRepository.save(any(PaymentPlan.class)))
                .thenReturn(Try.success(paymentPlan));
        when(referralService.createPendingReferral(CHECKOUT_POST_REQUEST.getCouponCode(), PRICING.getReferralDiscount(), BLEND.getCustomerId()))
                .thenReturn(Try.success(Optional.empty()));
        when(customerService.find(BLEND.getCustomerId()))
                .thenReturn(Try.success(CUSTOMER));
        when(mollieService.createCustomerPayment(CUSTOMER, AMOUNT_VALUE,
                paymentConfiguration.getFirstDescription() + " " + paymentPlan.externalReference(),
                paymentPlan.externalReference(), Optional.empty(), null, FACEBOOK_CLICK, GA_SESSION_ID))
                .thenReturn(Try.success(paymentResponse));
        when(klaviyoService.upsertFullProfile(CUSTOMER, paymentPlan))
                .thenReturn(Try.success(CUSTOMER));
        doNothing().when(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.STARTED_CHECKOUT, null, Optional.of(BLEND.getId()));

        Try<PaymentResponse> paymentResponseTry = paymentService.create(BLEND_EXTERNAL_REFERENCE, CHECKOUT_POST_REQUEST, FACEBOOK_CLICK, GA_SESSION_ID);
        assertTrue(paymentResponseTry.isSuccess());

        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);
        verify(paymentPlanRepository).findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE);
        verify(pricingService).getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST.getCouponCode(), CHECKOUT_POST_REQUEST.getMonthsSubscribed(), CHECKOUT_POST_REQUEST.getSubscription());
        verify(paymentPlanRepository).save(any(PaymentPlan.class));
        verify(referralService).createPendingReferral(CHECKOUT_POST_REQUEST.getCouponCode(), PRICING.getReferralDiscount(), BLEND.getCustomerId());
        verify(customerService).find(BLEND.getCustomerId());
        verify(mollieService).createCustomerPayment(CUSTOMER, AMOUNT_VALUE,
                paymentConfiguration.getFirstDescription() + " " + paymentPlan.externalReference(),
                paymentPlan.externalReference(), Optional.empty(), null, FACEBOOK_CLICK, GA_SESSION_ID);
        verify(klaviyoService).upsertFullProfile(CUSTOMER, paymentPlan);
        verify(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.STARTED_CHECKOUT, null, Optional.of(BLEND.getId()));
    }

    @Test
    void createOk1Coupon() {
        final PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE_COUPON, MONTHS_SUBSCRIBED);
        final PaymentResponse paymentResponse = buildPaymentResponse(AMOUNT_COUPON, METADATA);

        when(blendService.find(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND));
        when(paymentPlanRepository.findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(null));
        when(pricingService.getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST_COUPON.getCouponCode(), CHECKOUT_POST_REQUEST_COUPON.getMonthsSubscribed(), CHECKOUT_POST_REQUEST_COUPON.getSubscription()))
                .thenReturn(Try.success(PRICING));
        when(paymentPlanRepository.save(any(PaymentPlan.class)))
                .thenReturn(Try.success(paymentPlan));
        when(couponService.setCouponUsedForCustomerPaymentPlan(COUPON_CODE, paymentPlan))
                .thenReturn(Try.success(null));
        when(referralService.createPendingReferral(CHECKOUT_POST_REQUEST_COUPON.getCouponCode(), PRICING.getReferralDiscount(), BLEND.getCustomerId()))
                .thenReturn(Try.success(Optional.empty()));
        when(customerService.find(BLEND.getCustomerId()))
                .thenReturn(Try.success(CUSTOMER));
        when(mollieService.createCustomerPayment(CUSTOMER, AMOUNT_VALUE_COUPON,
                paymentConfiguration.getFirstDescription() + " " + paymentPlan.externalReference(),
                paymentPlan.externalReference(), Optional.empty(), null, FACEBOOK_CLICK, GA_SESSION_ID))
                .thenReturn(Try.success(paymentResponse));
        when(klaviyoService.upsertFullProfile(CUSTOMER, paymentPlan))
                .thenReturn(Try.success(CUSTOMER));
        doNothing().when(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.STARTED_CHECKOUT, null, Optional.of(BLEND.getId()));

        Try<PaymentResponse> paymentResponseTry = paymentService.create(BLEND_EXTERNAL_REFERENCE, CHECKOUT_POST_REQUEST_COUPON, FACEBOOK_CLICK, GA_SESSION_ID);
        assertTrue(paymentResponseTry.isSuccess());

        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);
        verify(paymentPlanRepository).findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE);
        verify(pricingService).getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST_COUPON.getCouponCode(), CHECKOUT_POST_REQUEST_COUPON.getMonthsSubscribed(), CHECKOUT_POST_REQUEST_COUPON.getSubscription());
        verify(paymentPlanRepository).save(any(PaymentPlan.class));
        verify(couponService).setCouponUsedForCustomerPaymentPlan(COUPON_CODE, paymentPlan);
        verify(referralService).createPendingReferral(CHECKOUT_POST_REQUEST_COUPON.getCouponCode(), PRICING.getReferralDiscount(), BLEND.getCustomerId());
        verify(customerService).find(BLEND.getCustomerId());
        verify(mollieService).createCustomerPayment(CUSTOMER, AMOUNT_VALUE_COUPON,
                paymentConfiguration.getFirstDescription() + " " + paymentPlan.externalReference(),
                paymentPlan.externalReference(), Optional.empty(), null, FACEBOOK_CLICK, GA_SESSION_ID);
        verify(klaviyoService).upsertFullProfile(CUSTOMER, paymentPlan);
        verify(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.STARTED_CHECKOUT, null, Optional.of(BLEND.getId()));
    }

    @Test
    void createOk1Referral() {
        PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE_COUPON, MONTHS_SUBSCRIBED);
        final PaymentResponse paymentResponse = buildPaymentResponse(AMOUNT_COUPON, METADATA);

        when(blendService.find(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND));
        when(paymentPlanRepository.findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(null));
        when(pricingService.getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST_COUPON.getCouponCode(), CHECKOUT_POST_REQUEST_COUPON.getMonthsSubscribed(), CHECKOUT_POST_REQUEST_COUPON.getSubscription()))
                .thenReturn(Try.success(PRICING));
        when(customerService.find(BLEND.getCustomerId()))
                .thenReturn(Try.success(CUSTOMER));
        when(paymentPlanRepository.save(any(PaymentPlan.class)))
                .thenReturn(Try.success(paymentPlan));
        when(couponService.setCouponUsedForCustomerPaymentPlan(COUPON_CODE, paymentPlan))
                .thenReturn(Try.success(null));
        when(referralService.createPendingReferral(COUPON_CODE, PRICING.getReferralDiscount(), BLEND.getCustomerId()))
                .thenReturn(Try.success(Optional.of(REFERRAL.getId())));
        when(mollieService.createCustomerPayment(CUSTOMER, AMOUNT_VALUE_COUPON,
                paymentConfiguration.getFirstDescription() + " " + paymentPlan.externalReference(),
                paymentPlan.externalReference(), Optional.of(REFERRAL.getId()), null, FACEBOOK_CLICK, GA_SESSION_ID))
                .thenReturn(Try.success(paymentResponse));
        when(klaviyoService.upsertFullProfile(CUSTOMER, paymentPlan))
                .thenReturn(Try.success(CUSTOMER));
        doNothing().when(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.STARTED_CHECKOUT, null, Optional.of(BLEND.getId()));

        Try<PaymentResponse> paymentResponseTry = paymentService.create(BLEND_EXTERNAL_REFERENCE, CHECKOUT_POST_REQUEST_COUPON, FACEBOOK_CLICK, GA_SESSION_ID);
        assertTrue(paymentResponseTry.isSuccess());

        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);
        verify(paymentPlanRepository).findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE);
        verify(pricingService).getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST_COUPON.getCouponCode(), CHECKOUT_POST_REQUEST_COUPON.getMonthsSubscribed(), CHECKOUT_POST_REQUEST_COUPON.getSubscription());
        verify(customerService).find(BLEND.getCustomerId());
        verify(paymentPlanRepository).save(any(PaymentPlan.class));
        verify(couponService).setCouponUsedForCustomerPaymentPlan(COUPON_CODE, paymentPlan);
        verify(referralService).createPendingReferral(COUPON_CODE, PRICING.getReferralDiscount(), BLEND.getCustomerId());
        verify(mollieService).createCustomerPayment(CUSTOMER, AMOUNT_VALUE_COUPON,
                paymentConfiguration.getFirstDescription() + " " + paymentPlan.externalReference(),
                paymentPlan.externalReference(), Optional.of(REFERRAL.getId()), null, FACEBOOK_CLICK, GA_SESSION_ID);
        verify(klaviyoService).upsertFullProfile(CUSTOMER, paymentPlan);
        verify(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.STARTED_CHECKOUT, null, Optional.of(BLEND.getId()));
    }

    @Test
    void createOk3() {
        PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID_3, AMOUNT_VALUE_3_DISCOUNTED, MONTHS_SUBSCRIBED_3);
        final PaymentResponse paymentResponse = buildPaymentResponse(AMOUNT_3, METADATA);

        when(blendService.find(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND));
        when(paymentPlanRepository.findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(null));
        when(pricingService.getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST_3.getCouponCode(), CHECKOUT_POST_REQUEST_3.getMonthsSubscribed(), CHECKOUT_POST_REQUEST_3.getSubscription()))
                .thenReturn(Try.success(PRICING));
        when(customerService.find(BLEND.getCustomerId()))
                .thenReturn(Try.success(CUSTOMER));
        when(paymentPlanRepository.save(any(PaymentPlan.class)))
                .thenReturn(Try.success(paymentPlan));
        when(referralService.createPendingReferral(CHECKOUT_POST_REQUEST_3.getCouponCode(), PRICING.getReferralDiscount(), BLEND.getCustomerId()))
                .thenReturn(Try.success(Optional.empty()));
        when(mollieService.createCustomerPayment(CUSTOMER, AMOUNT_VALUE_3_DISCOUNTED,
                paymentConfiguration.getFirstDescription() + " " + paymentPlan.externalReference(),
                paymentPlan.externalReference(), Optional.empty(), null, FACEBOOK_CLICK, GA_SESSION_ID))
                .thenReturn(Try.success(paymentResponse));
        when(klaviyoService.upsertFullProfile(CUSTOMER, paymentPlan))
                .thenReturn(Try.success(CUSTOMER));
        doNothing().when(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.STARTED_CHECKOUT, null, Optional.of(BLEND.getId()));

        Try<PaymentResponse> paymentResponseTry = paymentService.create(BLEND_EXTERNAL_REFERENCE, CHECKOUT_POST_REQUEST_3, FACEBOOK_CLICK, GA_SESSION_ID);
        assertTrue(paymentResponseTry.isSuccess());

        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);
        verify(paymentPlanRepository).findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE);
        verify(pricingService).getBlendPricing(BLEND_EXTERNAL_REFERENCE, Optional.empty(), CHECKOUT_POST_REQUEST_3.getCouponCode(), CHECKOUT_POST_REQUEST_3.getMonthsSubscribed(), CHECKOUT_POST_REQUEST_3.getSubscription());
        verify(customerService).find(BLEND.getCustomerId());
        verify(paymentPlanRepository).save(any(PaymentPlan.class));
        verify(referralService).createPendingReferral(CHECKOUT_POST_REQUEST_3.getCouponCode(), PRICING.getReferralDiscount(), BLEND.getCustomerId());
        verify(mollieService).createCustomerPayment(CUSTOMER, AMOUNT_VALUE_3_DISCOUNTED,
                paymentConfiguration.getFirstDescription() + " " + paymentPlan.externalReference(),
                paymentPlan.externalReference(), Optional.empty(), null, FACEBOOK_CLICK, GA_SESSION_ID);
        verify(klaviyoService).upsertFullProfile(CUSTOMER, paymentPlan);
        verify(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.STARTED_CHECKOUT, null, Optional.of(BLEND.getId()));
    }

    @Test
    void createRetryFirstPaymentOk() {
        PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE, MONTHS_SUBSCRIBED);
        Payment payment = buildPayment(PAYMENT_ID, "retried", PaymentStatus.failed);

        when(paymentPlanRepository.find(paymentPlan.externalReference()))
                .thenReturn(Try.success(paymentPlan));
        when(paymentRepository.findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id()))
                .thenReturn(Try.success(List.of(payment)));
        when(paymentRepository.findByRetriedMolliePaymentId(payment.getMolliePaymentId()))
                .thenReturn(Try.success(List.of(payment)));
        when(customerService.find(paymentPlan.customerId()))
                .thenReturn(Try.success(CUSTOMER));
        when(mollieService.createRetryFirstPayment(CUSTOMER, paymentPlan, payment))
                .thenReturn(Try.success(PAYMENT_RESPONSE));

        final Try<PaymentResponse> response = paymentService.createRetryFirstPayment(paymentPlan.externalReference());
        assertTrue(response.isSuccess());
        assertEquals(PAYMENT_RESPONSE, response.get());

        verify(paymentPlanRepository).find(paymentPlan.externalReference());
        verify(paymentRepository).findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id());
        verify(paymentRepository).findByRetriedMolliePaymentId(payment.getMolliePaymentId());
        verify(customerService).find(paymentPlan.customerId());
        verify(mollieService).createRetryFirstPayment(CUSTOMER, paymentPlan, payment);
    }

    @Test
    void createRetryFirstPaymentAlreadyPaid() {
        PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE, MONTHS_SUBSCRIBED);
        Payment payment = buildPayment(PAYMENT_ID, "retried", PaymentStatus.failed);
        Payment paidPayment = buildPayment(PAYMENT_ID, null, PaymentStatus.paid);

        when(paymentPlanRepository.find(paymentPlan.externalReference()))
                .thenReturn(Try.success(paymentPlan));
        when(paymentRepository.findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id()))
                .thenReturn(Try.success(List.of(payment)));
        when(paymentRepository.findByRetriedMolliePaymentId(payment.getMolliePaymentId()))
                .thenReturn(Try.success(List.of(paidPayment)));

        final Try<PaymentResponse> response = paymentService.createRetryFirstPayment(paymentPlan.externalReference());
        assertTrue(response.isFailure());
        assertTrue(response.getCause() instanceof DuplicateKeyException);
        assertEquals("All retried payments are paid", response.getCause().getMessage());

        verify(paymentPlanRepository).find(paymentPlan.externalReference());
        verify(paymentRepository).findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id());
        verify(paymentRepository).findByRetriedMolliePaymentId(payment.getMolliePaymentId());
    }

    @Test
    void createRetryFirstPaymentAlreadyPaid2() {
        PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE, MONTHS_SUBSCRIBED);
        Payment payment = buildPayment(PAYMENT_ID, "retried", PaymentStatus.failed);
        Payment paidPayment = buildPayment(PAYMENT_ID, null, PaymentStatus.paid);

        when(paymentPlanRepository.find(paymentPlan.externalReference()))
                .thenReturn(Try.success(paymentPlan));
        when(paymentRepository.findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id()))
                .thenReturn(Try.success(List.of(payment)));
        when(paymentRepository.findByRetriedMolliePaymentId(payment.getMolliePaymentId()))
                .thenReturn(Try.success(List.of(payment, paidPayment)));

        final Try<PaymentResponse> response = paymentService.createRetryFirstPayment(paymentPlan.externalReference());
        assertTrue(response.isFailure());
        assertTrue(response.getCause() instanceof DuplicateKeyException);
        assertEquals("All retried payments are paid", response.getCause().getMessage());

        verify(paymentPlanRepository).find(paymentPlan.externalReference());
        verify(paymentRepository).findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id());
        verify(paymentRepository).findByRetriedMolliePaymentId(payment.getMolliePaymentId());
    }
    @Test
    void createRetryFirstPaymentAlreadyPaid3() {
        PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE, MONTHS_SUBSCRIBED);
        Payment payment = buildPayment(PAYMENT_ID, "retried", PaymentStatus.failed);
        Payment paidPayment = buildPayment(PAYMENT_ID, null, PaymentStatus.paid);

        when(paymentPlanRepository.find(paymentPlan.externalReference()))
                .thenReturn(Try.success(paymentPlan));
        when(paymentRepository.findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id()))
                .thenReturn(Try.success(List.of(payment, paidPayment)));
        when(paymentRepository.findByRetriedMolliePaymentId(payment.getMolliePaymentId()))
                .thenReturn(Try.success(List.of(payment, paidPayment)));

        final Try<PaymentResponse> response = paymentService.createRetryFirstPayment(paymentPlan.externalReference());
        assertTrue(response.isFailure());
        assertTrue(response.getCause() instanceof DuplicateKeyException);
        assertEquals("All retried payments are paid", response.getCause().getMessage());

        verify(paymentPlanRepository).find(paymentPlan.externalReference());
        verify(paymentRepository).findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id());
        verify(paymentRepository, times(2)).findByRetriedMolliePaymentId(payment.getMolliePaymentId());
    }

    @Test
    void createRetryFirstPaymentOk2() {
        PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE, MONTHS_SUBSCRIBED);
        Payment payment = buildPayment(PAYMENT_ID, "retried", PaymentStatus.failed);
        Payment paidPayment = buildPayment(PAYMENT_ID, null, PaymentStatus.paid);

        when(paymentPlanRepository.find(paymentPlan.externalReference()))
                .thenReturn(Try.success(paymentPlan));
        when(paymentRepository.findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id()))
                .thenReturn(Try.success(List.of(payment, paidPayment)));
        when(paymentRepository.findByRetriedMolliePaymentId(payment.getMolliePaymentId()))
                .thenReturn(Try.success(List.of(payment)));
        when(customerService.find(paymentPlan.customerId()))
                .thenReturn(Try.success(CUSTOMER));
        when(mollieService.createRetryFirstPayment(CUSTOMER, paymentPlan, payment))
                .thenReturn(Try.success(PAYMENT_RESPONSE));

        final Try<PaymentResponse> response = paymentService.createRetryFirstPayment(paymentPlan.externalReference());
        assertTrue(response.isSuccess());
        assertEquals(PAYMENT_RESPONSE, response.get());

        verify(paymentPlanRepository).find(paymentPlan.externalReference());
        verify(paymentRepository).findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id());
        verify(paymentRepository).findByRetriedMolliePaymentId(payment.getMolliePaymentId());
        verify(customerService).find(paymentPlan.customerId());
        verify(mollieService).createRetryFirstPayment(CUSTOMER, paymentPlan, payment);
    }

    @Test
    void createRetryFirstPaymentOk3() {
        PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE, MONTHS_SUBSCRIBED);
        Payment payment = buildPayment(PAYMENT_ID, "retried", PaymentStatus.failed);
        Payment paidPayment = buildPayment(PAYMENT_ID, null, PaymentStatus.paid);

        when(paymentPlanRepository.find(paymentPlan.externalReference()))
                .thenReturn(Try.success(paymentPlan));
        when(paymentRepository.findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id()))
                .thenReturn(Try.success(List.of(paidPayment, payment)));
        when(paymentRepository.findByRetriedMolliePaymentId(payment.getMolliePaymentId()))
                .thenReturn(Try.success(List.of(payment)));
        when(customerService.find(paymentPlan.customerId()))
                .thenReturn(Try.success(CUSTOMER));
        when(mollieService.createRetryFirstPayment(CUSTOMER, paymentPlan, paidPayment))
                .thenReturn(Try.success(PAYMENT_RESPONSE));

        final Try<PaymentResponse> response = paymentService.createRetryFirstPayment(paymentPlan.externalReference());
        assertTrue(response.isSuccess());
        assertEquals(PAYMENT_RESPONSE, response.get());

        verify(paymentPlanRepository).find(paymentPlan.externalReference());
        verify(paymentRepository).findFailedChargebackRecurringByPaymentPlanId(paymentPlan.id());
        verify(paymentRepository).findByRetriedMolliePaymentId(payment.getMolliePaymentId());
        verify(customerService).find(paymentPlan.customerId());
        verify(mollieService).createRetryFirstPayment(CUSTOMER, paymentPlan, paidPayment);
    }

    @Test
    void saveOk() {
        Payment payment = buildPayment(null, null, PaymentStatus.paid);
        final PaymentResponse paymentResponse = buildPaymentResponse(AMOUNT, METADATA);

        when(paymentRepository.save(payment))
                .thenReturn(Try.success(payment));

        final Try<Payment> paymentTry = paymentService.savePayment(PAYMENT_PLAN_ID, paymentResponse);
        assertTrue(paymentTry.isSuccess());

        verify(paymentRepository).save(payment);
    }

    @Test
    void saveRefundOk() {
        Payment paymentInput = buildPayment(null, null, PaymentStatus.paid);
        Payment paymentResult = buildPayment(PAYMENT_ID, null, PaymentStatus.refund);
        final PaymentResponse paymentResponse = buildPaymentResponse(AMOUNT, METADATA);

        when(paymentRepository.save(paymentInput))
                .thenReturn(Try.success(paymentResult));

        final Try<Payment> paymentTry = paymentService.savePayment(PAYMENT_PLAN_ID, paymentResponse);
        assertTrue(paymentTry.isSuccess());

        verify(paymentRepository).save(paymentInput);
    }

    private PaymentResponse buildPaymentResponse(Amount amount, Map<String, Object> metadata) {
        return getPaymentResponseBuilder()
                .amount(amount)
                .metadata(metadata)
                .build();
    }

    private static PaymentResponse.PaymentResponseBuilder getPaymentResponseBuilder() {
        return PaymentResponse.builder()
                .amountRefunded(Optional.empty())
                .createdAt(NOW)
                .paidAt(Optional.of(NOW))
                .description(" ")
                .details(PaymentDetailsResponse.builder().bankReasonCode(Optional.empty()).build())
                .id(MOLLIE_PAYMENT_ID)
                .status(be.woutschoovaerts.mollie.data.payment.PaymentStatus.PAID)
                .sequenceType(SequenceType.FIRST);
    }

    private PaymentPlan buildPaymentPlan(Long paymentPlanId, BigDecimal amountValue, int recurringMonths) {
        return new PaymentPlan(paymentPlanId, amountValue, amountValue, recurringMonths, CUSTOMER_ID, BLEND_ID, UUID.randomUUID(),
                NOW_LD, NOW_LD, PaymentPlanStatus.PENDING, NOW_LD, null, NOW_LD, Optional.empty(), Optional.empty(), null);
    }

    private Payment buildPayment(Long paymentId, String retried, PaymentStatus status) {
        final LocalDateTime paymentDate = retried == null ? NOW_LD : null;
        return new Payment(paymentId, AMOUNT_VALUE, MOLLIE_PAYMENT_ID, retried, PAYMENT_PLAN_ID,
                NOW_LD, paymentDate, null, status, null, SequenceType.FIRST);
    }
}