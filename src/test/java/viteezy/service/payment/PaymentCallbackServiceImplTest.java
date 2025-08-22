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
import viteezy.configuration.PaymentConfiguration;
import viteezy.db.PaymentPlanRepository;
import viteezy.domain.*;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendStatus;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.fulfilment.OrderStatus;
import viteezy.domain.payment.*;
import viteezy.domain.pricing.Referral;
import viteezy.domain.pricing.ReferralDiscount;
import viteezy.domain.pricing.ReferralStatus;
import viteezy.gateways.facebook.FacebookService;
import viteezy.gateways.googleanalytics.GoogleAnalyticsService;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.gateways.slack.SlackService;
import viteezy.service.CustomerService;
import viteezy.service.LoggingService;
import viteezy.service.fulfilment.OrderService;
import viteezy.service.mail.EmailService;
import viteezy.service.pricing.CouponService;
import viteezy.service.pricing.ReferralService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PaymentCallbackServiceImplTest {

    private static final Long QUIZ_ID = 2L;
    private static final Long BLEND_ID = 3L;
    private static final Long CUSTOMER_ID = 4L;
    private static final Long CUSTOMER_ID_TO_REFERRAL = 5L;
    private static final Long PAYMENT_PLAN_ID = 6L;
    private static final Long PAYMENT_ID = 7L;
    private static final Integer REFERRAL_ID = 8;
    private static final String MOLLIE_PAYMENT_ID = "molliePaymentId";
    private static final UUID BLEND_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final BigDecimal AMOUNT_VALUE = BigDecimal.valueOf(20L);
    private static final Amount AMOUNT = new Amount("currency", AMOUNT_VALUE);
    private static final Amount AMOUNT_ZERO = new Amount("currency", BigDecimal.valueOf(0.00));
    private static final String GA_ID = "gaId";
    private static final String USER_AGENT = "userAgent";
    private static final OffsetDateTime NOW = OffsetDateTime.now();
    private static final LocalDateTime NOW_LD = NOW.toLocalDateTime();
    private static final Customer CUSTOMER = new Customer(CUSTOMER_ID, "email", true, UUID.randomUUID(),
            "mollieCustomerId", null, null, "klaviyoProfileId", GA_ID, null, USER_AGENT, null, null, null, null, null, 1, null, null,null, "BE",
            null, NOW_LD, NOW_LD);
    private static final Customer CUSTOMER_TO_REFERRAL = new Customer(CUSTOMER_ID_TO_REFERRAL, "email", true, UUID.randomUUID(),
            "mollieCustomerId", null, null, null, GA_ID, null, USER_AGENT, null, "referralFirstName", null, null, null, null, null, null,null, "BE",
            null, NOW_LD, NOW_LD);
    private static final Blend BLEND = new Blend(BLEND_ID, BlendStatus.CREATED, BLEND_EXTERNAL_REFERENCE, CUSTOMER.getId(), QUIZ_ID, NOW_LD, NOW_LD);
    private static final PaymentPlan PAYMENT_PLAN = new PaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE, AMOUNT_VALUE, 1, CUSTOMER.getId(), BLEND.getId(), UUID.randomUUID(),
            NOW_LD, null, PaymentPlanStatus.PENDING, NOW_LD, null, NOW_LD,
            Optional.empty(), Optional.empty(), null);
    private static final String FACEBOOK_CLICK = "fb.1.1234567890123.AbCdEfGhIjKlMnOpQrStUvWxYz1234567890";
    private static final String GA_SESSION_ID = "GS1.1.1234567890.13.0.1234567890.60.0.0";
    private static final Map<String, Object> METADATA = Map.of(MollieMetadataKeys.PAYMENT_PLAN_ID, PAYMENT_PLAN.externalReference().toString(), MollieMetadataKeys.FACEBOOK_CLICK_ID, FACEBOOK_CLICK, MollieMetadataKeys.GA_SESSION_ID, GA_SESSION_ID);
    private static final Map<String, Object> METADATA_REFERRAL_ID = Map.of(MollieMetadataKeys.PAYMENT_PLAN_ID, PAYMENT_PLAN.externalReference().toString(), MollieMetadataKeys.REFERRAL_ID, REFERRAL_ID, MollieMetadataKeys.GA_SESSION_ID, GA_SESSION_ID);
    private static final Map<String, Object> EMPTY_METADATA = Collections.emptyMap();
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final IllegalStateException NONE_EXCEPTION = new IllegalStateException("Expected one element, but found none");
    private static final Referral REFERRAL = new Referral(REFERRAL_ID.longValue(), CUSTOMER_ID, CUSTOMER_ID_TO_REFERRAL, AMOUNT_VALUE, ReferralStatus.PENDING, NOW_LD, NOW_LD);
    private static final int DISCOUNT_AMOUNT = 23;
    private static final ReferralDiscount REFERRAL_DISCOUNT = new ReferralDiscount(DISCOUNT_AMOUNT, BigDecimal.TEN);

    private final MollieService mollieService = mock(MollieService.class);
    private final CustomerService customerService = mock(CustomerService.class);
    private final PaymentConfiguration paymentConfiguration = mock(PaymentConfiguration.class);
    private final PaymentPlanRepository paymentPlanRepository = mock(PaymentPlanRepository.class);
    private final EmailService emailService = Mockito.mock(EmailService.class);
    private final GoogleAnalyticsService googleAnalyticsService = Mockito.mock(GoogleAnalyticsService.class);
    private final FacebookService facebookService = Mockito.mock(FacebookService.class);
    private final CouponService couponService = Mockito.mock(CouponService.class);
    private final ReferralService referralService = Mockito.mock(ReferralService.class);
    private final PaymentService paymentService = Mockito.mock(PaymentService.class);
    private final SlackService slackService = Mockito.mock(SlackService.class);
    private final OrderService orderService = Mockito.mock(OrderService.class);
    private final LoggingService loggingService = Mockito.mock(LoggingService.class);
    private final KlaviyoService klaviyoService = Mockito.mock(KlaviyoService.class);

    private PaymentCallbackService paymentCallbackService;

    @BeforeEach
    void setUp() {
        when(referralService.getReferralDiscount()).thenReturn(REFERRAL_DISCOUNT);
        paymentCallbackService = new PaymentCallbackServiceImpl(mollieService, paymentPlanRepository, paymentService, googleAnalyticsService, facebookService, paymentConfiguration, emailService, couponService, referralService, customerService, slackService, orderService, loggingService, klaviyoService);
        when(paymentConfiguration.getApiKey()).thenReturn("test_APIKEY");
        when(paymentConfiguration.getDeliveryDateInDays()).thenReturn(9);
        verify(referralService).getReferralDiscount();
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
                mollieService,
                paymentPlanRepository,
                paymentService,
                googleAnalyticsService,
                facebookService,
                emailService,
                couponService,
                referralService,
                customerService,
                slackService,
                orderService,
                loggingService,
                klaviyoService);

        reset(
                mollieService,
                paymentPlanRepository,
                paymentService,
                googleAnalyticsService,
                facebookService,
                emailService,
                couponService,
                referralService,
                customerService,
                slackService,
                orderService,
                loggingService,
                klaviyoService);
    }

    @Test
    void processCallbackFirstPaymentPaidOk() {
        final PaymentResponse paymentResponse = mockFirstPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus.PAID);
        Payment payment = buildPayment(null, PaymentStatus.paid, SequenceType.FIRST);
        Order order = buildOrder(payment, buildPaymentPlan(PaymentPlanStatus.ACTIVE));

        when(mollieService.find(MOLLIE_PAYMENT_ID))
                .thenReturn(Try.success(paymentResponse));
        when(paymentPlanRepository.find(PAYMENT_PLAN.externalReference()))
                .thenReturn(Try.success(PAYMENT_PLAN));
        when(paymentService.buildPayment(PAYMENT_PLAN_ID, paymentResponse))
                .thenReturn(payment);
        when(paymentService.getByMolliePaymentId(paymentResponse.getId()))
                .thenReturn(Try.failure(NONE_EXCEPTION));
        when(paymentService.save(payment))
                .thenReturn(Try.success(payment));
        when(couponService.findCouponUsedByPaymentPlan(PAYMENT_PLAN_ID))
                .thenReturn(Try.success(Optional.empty()));
        when(paymentPlanRepository.update(buildPaymentPlan(PaymentPlanStatus.ACTIVE)))
                .thenReturn(Try.success(buildPaymentPlan(PaymentPlanStatus.ACTIVE)));
        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(klaviyoService.upsertFullProfile(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE)))
                .thenReturn(Try.success(CUSTOMER));
        when(orderService.save(payment, buildPaymentPlan(PaymentPlanStatus.ACTIVE), CUSTOMER))
                .thenReturn(Try.success(order));
        doNothing().when(klaviyoService).createPurchasePaidEvent(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), payment);

        Try<PaymentPlan> paymentPlan = paymentCallbackService.processCallback(MOLLIE_PAYMENT_ID);
        assertTrue(paymentPlan.isSuccess());
        assertEquals(buildPaymentPlan(PaymentPlanStatus.ACTIVE), paymentPlan.get());

        verify(mollieService).find(MOLLIE_PAYMENT_ID);
        verify(paymentPlanRepository).find(PAYMENT_PLAN.externalReference());
        verify(paymentService).buildPayment(PAYMENT_PLAN_ID, paymentResponse);
        verify(paymentService).getByMolliePaymentId(paymentResponse.getId());
        verify(paymentService).save(payment);

        verify(paymentPlanRepository).update(buildPaymentPlan(PaymentPlanStatus.ACTIVE));
        verify(couponService).findCouponUsedByPaymentPlan(PAYMENT_PLAN_ID);
        verify(customerService).find(CUSTOMER_ID);

        verify(orderService).save(payment, buildPaymentPlan(PaymentPlanStatus.ACTIVE), CUSTOMER);
        verify(emailService).createOrder(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE));
        verify(slackService).notify(buildPaymentPlan(PaymentPlanStatus.ACTIVE), false, true);
        verify(googleAnalyticsService).sendEcommerceTransaction(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), GA_SESSION_ID);
        verify(facebookService).sendPurchaseEvent(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), true, MOLLIE_PAYMENT_ID, FACEBOOK_CLICK);
        verify(klaviyoService).upsertFullProfile(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE));
        verify(klaviyoService).createPurchasePaidEvent(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), payment);
    }

    @Test
    void processCallbackFirstPaymentPaidOkCreateSubscriptionFailed() {
        final PaymentResponse paymentResponse = mockFirstPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus.PAID);
        Payment payment = buildPayment(null, PaymentStatus.paid, SequenceType.FIRST);

        when(mollieService.find(MOLLIE_PAYMENT_ID))
                .thenReturn(Try.success(paymentResponse));
        when(paymentPlanRepository.find(PAYMENT_PLAN.externalReference()))
                .thenReturn(Try.success(PAYMENT_PLAN));
        when(paymentService.buildPayment(PAYMENT_PLAN_ID, paymentResponse))
                .thenReturn(payment);
        when(paymentService.getByMolliePaymentId(paymentResponse.getId()))
                .thenReturn(Try.failure(NONE_EXCEPTION));
        when(paymentService.save(payment))
                .thenReturn(Try.success(payment));
        when(couponService.findCouponUsedByPaymentPlan(PAYMENT_PLAN_ID))
                .thenReturn(Try.success(Optional.empty()));
        when(paymentPlanRepository.update(buildPaymentPlan(PaymentPlanStatus.ACTIVE)))
                .thenReturn(Try.failure(EXCEPTION));

        Try<PaymentPlan> paymentPlan = paymentCallbackService.processCallback(MOLLIE_PAYMENT_ID);
        assertTrue(paymentPlan.isFailure());

        verify(mollieService).find(MOLLIE_PAYMENT_ID);
        verify(paymentPlanRepository).find(PAYMENT_PLAN.externalReference());
        verify(paymentService).buildPayment(PAYMENT_PLAN_ID, paymentResponse);
        verify(paymentService).getByMolliePaymentId(paymentResponse.getId());
        verify(paymentService).save(payment);
        verify(couponService).findCouponUsedByPaymentPlan(PAYMENT_PLAN_ID);
        verify(paymentPlanRepository).update(buildPaymentPlan(PaymentPlanStatus.ACTIVE));
    }

    @Test
    void processCallbackFirstPaymentPaidFailed() {
        final Payment payment = new Payment(1L, AMOUNT_VALUE, MOLLIE_PAYMENT_ID, null, PAYMENT_PLAN.id(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), PaymentStatus.failed, null, SequenceType.FIRST);
        final PaymentResponse paymentResponse = mockFirstPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus.FAILED);

        when(mollieService.find(MOLLIE_PAYMENT_ID))
                .thenReturn(Try.success(paymentResponse));
        when(paymentPlanRepository.find(PAYMENT_PLAN.externalReference()))
                .thenReturn(Try.success(PAYMENT_PLAN));
        when(paymentService.buildPayment(PAYMENT_PLAN_ID, paymentResponse))
                .thenReturn(payment);
        when(paymentService.getByMolliePaymentId(paymentResponse.getId()))
                .thenReturn(Try.failure(NONE_EXCEPTION));
        when(paymentService.save(payment))
                .thenReturn(Try.success(payment));



        when(paymentPlanRepository.update(buildPaymentPlan(PaymentPlanStatus.CANCELED)))
                .thenReturn(Try.success(PAYMENT_PLAN));

        when(customerService.find(PAYMENT_PLAN.customerId()))
                .thenReturn(Try.success(CUSTOMER));
        when(klaviyoService.upsertFullProfile(CUSTOMER, PAYMENT_PLAN))
                .thenReturn(Try.success(null));

        when(loggingService.create(PAYMENT_PLAN.customerId(), LoggingEvent.PAYMENT_PLAN_CANCELED, "Abonnement door systeem stopgezet vanwege betaling: " + payment.getStatus()))
                .thenReturn(Try.success(any(Logging.class)));

        Try<PaymentPlan> paymentPlan = paymentCallbackService.processCallback(MOLLIE_PAYMENT_ID);
        assertTrue(paymentPlan.isSuccess());
        assertEquals(PAYMENT_PLAN, paymentPlan.get());

        verify(mollieService).find(MOLLIE_PAYMENT_ID);
        verify(paymentPlanRepository).find(PAYMENT_PLAN.externalReference());
        verify(paymentService).buildPayment(PAYMENT_PLAN_ID, paymentResponse);
        verify(paymentService).getByMolliePaymentId(paymentResponse.getId());
        verify(paymentService).save(payment);
        verify(paymentPlanRepository).update(buildPaymentPlan(PaymentPlanStatus.CANCELED));
        verify(customerService).find(PAYMENT_PLAN.customerId());
        verify(klaviyoService).upsertFullProfile(CUSTOMER, PAYMENT_PLAN);
        verify(loggingService).create(PAYMENT_PLAN.customerId(), LoggingEvent.PAYMENT_PLAN_CANCELED, "Abonnement door systeem stopgezet vanwege betaling: " + payment.getStatus());
    }

    @Test
    void processCallbackReferralFirstPaymentPaidOk() {
        final PaymentResponse paymentResponse = mockReferralPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus.PAID);
        Payment payment = buildPayment(null, PaymentStatus.paid, SequenceType.FIRST);
        Order order = buildOrder(payment, buildPaymentPlan(PaymentPlanStatus.ACTIVE));

        when(mollieService.find(MOLLIE_PAYMENT_ID))
                .thenReturn(Try.success(paymentResponse));
        when(paymentPlanRepository.find(PAYMENT_PLAN.externalReference()))
                .thenReturn(Try.success(PAYMENT_PLAN));
        when(paymentService.buildPayment(PAYMENT_PLAN_ID, paymentResponse))
                .thenReturn(payment);
        when(paymentService.getByMolliePaymentId(paymentResponse.getId()))
                .thenReturn(Try.failure(NONE_EXCEPTION));
        when(paymentService.save(payment))
                .thenReturn(Try.success(payment));
        when(referralService.updateStatus(REFERRAL_ID.longValue(), ReferralStatus.PAID))
                .thenReturn(Try.success(REFERRAL));
        when(customerService.find(CUSTOMER_ID_TO_REFERRAL))
                .thenReturn(Try.success(CUSTOMER_TO_REFERRAL));
        when(couponService.findCouponUsedByPaymentPlan(PAYMENT_PLAN_ID))
                .thenReturn(Try.success(Optional.empty()));
        when(paymentPlanRepository.update(buildPaymentPlan(PaymentPlanStatus.ACTIVE)))
                .thenReturn(Try.success(buildPaymentPlan(PaymentPlanStatus.ACTIVE)));
        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(orderService.save(payment, buildPaymentPlan(PaymentPlanStatus.ACTIVE), CUSTOMER))
                .thenReturn(Try.success(order));
        when(klaviyoService.upsertFullProfile(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE)))
                .thenReturn(Try.success(CUSTOMER));
        doNothing().when(klaviyoService).createPurchasePaidEvent(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), payment);

        Try<PaymentPlan> paymentPlan = paymentCallbackService.processCallback(MOLLIE_PAYMENT_ID);
        assertTrue(paymentPlan.isSuccess());
        assertEquals(buildPaymentPlan(PaymentPlanStatus.ACTIVE), paymentPlan.get());

        verify(mollieService).find(MOLLIE_PAYMENT_ID);
        verify(paymentPlanRepository).find(PAYMENT_PLAN.externalReference());
        verify(paymentService).buildPayment(PAYMENT_PLAN_ID, paymentResponse);
        verify(paymentService).getByMolliePaymentId(paymentResponse.getId());
        verify(paymentService).save(payment);

        verify(referralService).updateStatus(REFERRAL_ID.longValue(), ReferralStatus.PAID);
        verify(customerService).find(CUSTOMER_ID_TO_REFERRAL);
        verify(emailService).sendReferralCodePaid(CUSTOMER_TO_REFERRAL.getFirstName(), CUSTOMER, REFERRAL_DISCOUNT);

        verify(couponService).findCouponUsedByPaymentPlan(PAYMENT_PLAN_ID);
        verify(paymentPlanRepository).update(buildPaymentPlan(PaymentPlanStatus.ACTIVE));
        verify(customerService, times(2)).find(CUSTOMER_ID);

        verify(orderService).save(payment, buildPaymentPlan(PaymentPlanStatus.ACTIVE), CUSTOMER);
        verify(emailService).createOrder(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE));
        verify(slackService).notify(buildPaymentPlan(PaymentPlanStatus.ACTIVE), false, true);
        verify(googleAnalyticsService).sendEcommerceTransaction(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), GA_SESSION_ID);
        verify(facebookService).sendPurchaseEvent(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), true, MOLLIE_PAYMENT_ID, null);
        verify(klaviyoService).upsertFullProfile(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE));
        verify(klaviyoService).createPurchasePaidEvent(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), payment);
    }


    @Test
    void processCallbackReferralFirstPaymentPaidRetriedOk() {
        final PaymentResponse paymentResponse = mockReferralPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus.PAID);
        Payment payment = buildPayment("retried", PaymentStatus.paid, SequenceType.FIRST);
        Order order = buildOrder(payment, buildPaymentPlan(PaymentPlanStatus.ACTIVE));

        when(mollieService.find(MOLLIE_PAYMENT_ID))
                .thenReturn(Try.success(paymentResponse));
        when(paymentPlanRepository.find(PAYMENT_PLAN.externalReference()))
                .thenReturn(Try.success(PAYMENT_PLAN));
        when(paymentService.buildPayment(PAYMENT_PLAN_ID, paymentResponse))
                .thenReturn(payment);
        when(paymentService.getByMolliePaymentId(paymentResponse.getId()))
                .thenReturn(Try.failure(NONE_EXCEPTION));
        when(paymentService.save(payment))
                .thenReturn(Try.success(payment));
        when(referralService.updateStatus(REFERRAL_ID.longValue(), ReferralStatus.PAID))
                .thenReturn(Try.success(REFERRAL));
        when(customerService.find(CUSTOMER_ID_TO_REFERRAL))
                .thenReturn(Try.success(CUSTOMER_TO_REFERRAL));
        when(couponService.findCouponUsedByPaymentPlan(PAYMENT_PLAN_ID))
                .thenReturn(Try.success(Optional.empty()));
        when(paymentPlanRepository.update(buildPaymentPlan(PaymentPlanStatus.ACTIVE)))
                .thenReturn(Try.success(buildPaymentPlan(PaymentPlanStatus.ACTIVE)));
        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(paymentService.getByMolliePaymentId("retried"))
                .thenReturn(Try.success(payment));
        when(orderService.save(payment, buildPaymentPlan(PaymentPlanStatus.ACTIVE), CUSTOMER))
                .thenReturn(Try.success(order));
        when(klaviyoService.upsertFullProfile(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE)))
                .thenReturn(Try.success(CUSTOMER));
        doNothing().when(klaviyoService).createPurchasePaidEvent(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), payment);

        Try<PaymentPlan> paymentPlan = paymentCallbackService.processCallback(MOLLIE_PAYMENT_ID);
        assertTrue(paymentPlan.isSuccess());
        assertEquals(buildPaymentPlan(PaymentPlanStatus.ACTIVE), paymentPlan.get());

        verify(mollieService).find(MOLLIE_PAYMENT_ID);
        verify(paymentPlanRepository).find(PAYMENT_PLAN.externalReference());
        verify(paymentService).buildPayment(PAYMENT_PLAN_ID, paymentResponse);
        verify(paymentService).getByMolliePaymentId(paymentResponse.getId());
        verify(paymentService).save(payment);

        verify(referralService).updateStatus(REFERRAL_ID.longValue(), ReferralStatus.PAID);
        verify(customerService).find(CUSTOMER_ID_TO_REFERRAL);
        verify(emailService).sendReferralCodePaid(CUSTOMER_TO_REFERRAL.getFirstName(), CUSTOMER, REFERRAL_DISCOUNT);

        verify(couponService).findCouponUsedByPaymentPlan(PAYMENT_PLAN_ID);
        verify(paymentPlanRepository).update(buildPaymentPlan(PaymentPlanStatus.ACTIVE));
        verify(customerService, times(2)).find(CUSTOMER_ID);
        verify(paymentService).getByMolliePaymentId("retried");

        verify(orderService).save(payment, buildPaymentPlan(PaymentPlanStatus.ACTIVE), CUSTOMER);
        verify(emailService).createOrder(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE));
        verify(slackService).notify(buildPaymentPlan(PaymentPlanStatus.ACTIVE), true, true);
        verify(googleAnalyticsService).sendEcommerceTransaction(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), GA_SESSION_ID);
        verify(facebookService).sendPurchaseEvent(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), true, MOLLIE_PAYMENT_ID, null);
        verify(klaviyoService).upsertFullProfile(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE));
        verify(klaviyoService).createPurchasePaidEvent(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.ACTIVE), payment);
    }

    @Test
    void processCallbackRecurringPaymentPaidOk() {
        final PaymentResponse paymentResponse = mockRecurringPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus.PAID);
        Payment payment = buildPayment(null, PaymentStatus.paid, SequenceType.RECURRING);
        Order order = buildOrder(payment, PAYMENT_PLAN);

        when(mollieService.find(MOLLIE_PAYMENT_ID))
                .thenReturn(Try.success(paymentResponse));
        when(paymentPlanRepository.find(PAYMENT_PLAN.externalReference()))
                .thenReturn(Try.success(PAYMENT_PLAN));
        when(paymentService.buildPayment(PAYMENT_PLAN_ID, paymentResponse))
                .thenReturn(payment);
        when(paymentService.getByMolliePaymentId(paymentResponse.getId()))
                .thenReturn(Try.failure(NONE_EXCEPTION));
        when(paymentService.save(payment))
                .thenReturn(Try.success(payment));
        when(paymentPlanRepository.update(buildPaymentPlanForFutureDelivery()))
                .thenReturn(Try.success(PAYMENT_PLAN));
        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(orderService.save(payment, PAYMENT_PLAN, CUSTOMER))
                .thenReturn(Try.success(order));
        when(klaviyoService.upsertFullProfile(CUSTOMER, PAYMENT_PLAN))
                .thenReturn(Try.success(CUSTOMER));
        doNothing().when(klaviyoService).createPurchasePaidEvent(CUSTOMER, PAYMENT_PLAN, payment);

        Try<PaymentPlan> paymentPlan = paymentCallbackService.processCallback(MOLLIE_PAYMENT_ID);
        assertTrue(paymentPlan.isSuccess());
        assertEquals(PAYMENT_PLAN, paymentPlan.get());

        verify(mollieService).find(MOLLIE_PAYMENT_ID);
        verify(paymentPlanRepository).find(PAYMENT_PLAN.externalReference());
        verify(paymentService).buildPayment(PAYMENT_PLAN_ID, paymentResponse);
        verify(paymentService).getByMolliePaymentId(paymentResponse.getId());
        verify(paymentService).save(payment);
        verify(paymentPlanRepository).update(any(PaymentPlan.class));
        verify(customerService).find(CUSTOMER_ID);
        verify(facebookService).sendPurchaseEvent(CUSTOMER, PAYMENT_PLAN, false, MOLLIE_PAYMENT_ID, null);
        verify(orderService).save(payment, PAYMENT_PLAN, CUSTOMER);
        verify(klaviyoService).upsertFullProfile(CUSTOMER, PAYMENT_PLAN);
        verify(klaviyoService).createPurchasePaidEvent(CUSTOMER, PAYMENT_PLAN, payment);
    }

    @Test
    void processCallbackRecurringPaymentAlreadyExists() {
        final PaymentResponse paymentResponse = mockRecurringPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus.PAID);
        Payment payment = buildPayment(null, PaymentStatus.paid, SequenceType.RECURRING);

        when(mollieService.find(MOLLIE_PAYMENT_ID))
                .thenReturn(Try.success(paymentResponse));
        when(paymentPlanRepository.find(PAYMENT_PLAN.externalReference()))
                .thenReturn(Try.success(PAYMENT_PLAN));
        when(paymentService.buildPayment(PAYMENT_PLAN_ID, paymentResponse))
                .thenReturn(payment);
        when(paymentService.getByMolliePaymentId(paymentResponse.getId()))
                .thenReturn(Try.success(payment));

        Try<PaymentPlan> paymentPlan = paymentCallbackService.processCallback(MOLLIE_PAYMENT_ID);
        assertTrue(paymentPlan.isSuccess());

        verify(mollieService).find(MOLLIE_PAYMENT_ID);
        verify(paymentPlanRepository).find(PAYMENT_PLAN.externalReference());
        verify(paymentService).buildPayment(PAYMENT_PLAN_ID, paymentResponse);
        verify(paymentService).getByMolliePaymentId(paymentResponse.getId());
    }

    @Test
    void processCallbackRecurringPaymentPaidFailed() {
        final PaymentResponse paymentResponse = mockRecurringPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus.FAILED);
        Payment payment = buildPayment(null, PaymentStatus.failed, SequenceType.RECURRING);

        when(mollieService.find(MOLLIE_PAYMENT_ID))
                .thenReturn(Try.success(paymentResponse));
        when(paymentPlanRepository.find(PAYMENT_PLAN.externalReference()))
                .thenReturn(Try.success(buildPaymentPlan(PaymentPlanStatus.ACTIVE)));
        when(paymentService.buildPayment(PAYMENT_PLAN_ID, paymentResponse))
                .thenReturn(payment);
        when(paymentService.getByMolliePaymentId(paymentResponse.getId()))
                .thenReturn(Try.failure(NONE_EXCEPTION));
        when(paymentService.save(payment))
                .thenReturn(Try.success(payment));
        when(paymentPlanRepository.update(buildPaymentPlan(PaymentPlanStatus.CANCELED)))
                .thenReturn(Try.success(buildPaymentPlan(PaymentPlanStatus.CANCELED)));
        when(customerService.find(PAYMENT_PLAN.customerId()))
                .thenReturn(Try.success(CUSTOMER));
        doNothing().when(emailService).sendPaymentMissing(CUSTOMER.getFirstName(), CUSTOMER.getEmail(), payment, PAYMENT_PLAN.externalReference(), 1);
        when(klaviyoService.upsertFullProfile(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.CANCELED)))
                .thenReturn(Try.success(null));

        Try<PaymentPlan> paymentPlan = paymentCallbackService.processCallback(MOLLIE_PAYMENT_ID);
        assertTrue(paymentPlan.isSuccess());
        assertEquals(buildPaymentPlan(PaymentPlanStatus.CANCELED), paymentPlan.get());

        verify(mollieService).find(MOLLIE_PAYMENT_ID);
        verify(paymentPlanRepository).find(PAYMENT_PLAN.externalReference());
        verify(paymentService).buildPayment(PAYMENT_PLAN_ID, paymentResponse);
        verify(paymentService).getByMolliePaymentId(paymentResponse.getId());
        verify(paymentService).save(payment);
        verify(paymentPlanRepository).update(buildPaymentPlan(PaymentPlanStatus.CANCELED));
        verify(customerService, Mockito.times(2)).find(PAYMENT_PLAN.customerId());
        verify(emailService).sendPaymentMissing(CUSTOMER.getFirstName(), CUSTOMER.getEmail(), payment, PAYMENT_PLAN.externalReference(), 1);
        verify(loggingService).create(PAYMENT_PLAN.customerId(), LoggingEvent.PAYMENT_PLAN_CANCELED, "Abonnement door systeem stopgezet vanwege betaling: " + payment.getStatus());
        verify(klaviyoService).upsertFullProfile(CUSTOMER, buildPaymentPlan(PaymentPlanStatus.CANCELED));
    }
    private PaymentResponse mockFirstPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus paymentStatus) {
        return buildPaymentResponse(paymentStatus, SequenceType.FIRST, METADATA);
    }

    private PaymentResponse mockReferralPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus paymentStatus) {
        return buildPaymentResponse(paymentStatus, SequenceType.FIRST, METADATA_REFERRAL_ID);
    }

    private PaymentResponse mockRecurringPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus paymentStatus) {
        return buildPaymentResponse(paymentStatus, SequenceType.RECURRING, EMPTY_METADATA);
    }

    private PaymentResponse buildPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus paymentStatus, SequenceType sequenceType, Map<String, Object> metadata) {
        return PaymentResponse.builder()
                .amount(AMOUNT)
                .amountRefunded(Optional.of(AMOUNT_ZERO))
                .createdAt(NOW)
                .customerId(Optional.of(CUSTOMER.getMollieCustomerId()))
                .paidAt(Optional.of(NOW))
                .description("Test dingen " + PAYMENT_PLAN.externalReference())
                .details(PaymentDetailsResponse.builder().bankReasonCode(Optional.empty()).build())
                .id(MOLLIE_PAYMENT_ID)
                .metadata(metadata)
                .status(paymentStatus)
                .sequenceType(sequenceType)
                .build();
    }

    private PaymentPlan buildPaymentPlan(PaymentPlanStatus status) {
        return new PaymentPlan(PAYMENT_PLAN_ID, PAYMENT_PLAN.firstAmount(), PAYMENT_PLAN.recurringAmount(),
                PAYMENT_PLAN.recurringMonths(), PAYMENT_PLAN.customerId(), PAYMENT_PLAN.blendId(), PAYMENT_PLAN.externalReference(),
                PAYMENT_PLAN.creationDate(), null, status, PAYMENT_PLAN.paymentDate(),
                null, PAYMENT_PLAN.deliveryDate(), Optional.empty(), Optional.empty(), null);
    }

    private PaymentPlan buildPaymentPlanForFutureDelivery() {
        final LocalDateTime deliveryDate = NOW.toLocalDateTime().plusDays(paymentConfiguration.getDeliveryDateInDays());
        return new PaymentPlan(PAYMENT_PLAN.id(), PAYMENT_PLAN.firstAmount(), PAYMENT_PLAN.recurringAmount(),
                PAYMENT_PLAN.recurringMonths(), PAYMENT_PLAN.customerId(),
                PAYMENT_PLAN.blendId(), PAYMENT_PLAN.externalReference(), PAYMENT_PLAN.creationDate(),
                null, PAYMENT_PLAN.status(), PAYMENT_PLAN.paymentDate(), PAYMENT_PLAN.stopReason(),
                deliveryDate, Optional.empty(), Optional.empty(), null);
    }
    
    private Payment buildPayment(String retried, PaymentStatus paymentStatus, SequenceType sequenceType) {
        final LocalDateTime paymentDate = retried == null ? NOW_LD : null;
        return new Payment(PAYMENT_ID, AMOUNT_VALUE, MOLLIE_PAYMENT_ID, retried, PAYMENT_PLAN.id(),
                LocalDateTime.now(), paymentDate, null, paymentStatus, null, sequenceType);
    }

    private Order buildOrder(Payment payment, PaymentPlan paymentPlan) {
        return new Order(null, UUID.randomUUID(), "orderNo", payment.getId(),
                payment.getSequenceType(), paymentPlan.id(), paymentPlan.blendId(), CUSTOMER.getId(),
                paymentPlan.recurringMonths(), CUSTOMER.getFirstName(), CUSTOMER.getLastName(), CUSTOMER.getStreet(),
                CUSTOMER.getHouseNumber().toString(), CUSTOMER.getHouseNumberAddition(), CUSTOMER.getPostcode(),
                CUSTOMER.getCity(), CUSTOMER.getCountry(), CUSTOMER.getPhoneNumber(), CUSTOMER.getEmail(),
                CUSTOMER.getReferralCode(), null, null, OrderStatus.CREATED, null, null, null);
    }
}