package viteezy.service.payment;

import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.payment.PaymentDetailsResponse;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.data.payment.SequenceType;
import be.woutschoovaerts.mollie.exception.MollieException;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.configuration.PaymentConfiguration;
import viteezy.db.PaymentPlanRepository;
import viteezy.domain.*;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendStatus;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.domain.payment.PaymentStatus;
import viteezy.domain.pricing.*;
import viteezy.service.CustomerService;
import viteezy.service.LoggingService;
import viteezy.service.mail.EmailService;
import viteezy.service.pricing.CouponService;
import viteezy.service.pricing.IncentiveService;
import viteezy.service.pricing.ReferralService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PaymentRecurringServiceImplTest {

    private static final int DAYS_AGO = 1;

    private static final Long QUIZ_ID = 2L;
    private static final Long BLEND_ID = 3L;
    private static final Long CUSTOMER_ID = 4L;
    private static final Long PAYMENT_PLAN_ID = 6L;
    private static final Long PAYMENT_ID = 7L;
    private static final Long REFERRAL_ID = 8L;
    private static final Long INCENTIVE_ID = 9L;
    private static final BigDecimal AMOUNT_VALUE = new BigDecimal("20.00");
    private static final Amount AMOUNT = new Amount("currency", AMOUNT_VALUE);
    private static final Amount AMOUNT_ZERO = new Amount("currency", new BigDecimal("0.00"));
    private static final String MOLLIE_PAYMENT_ID = "molliePaymentId";
    private static final OffsetDateTime NOW_O = OffsetDateTime.now();
    private static final LocalDateTime NOW = NOW_O.toLocalDateTime();
    private static final Customer CUSTOMER = new Customer(CUSTOMER_ID, "email", true, UUID.randomUUID(),
            "mollieCustomerId", null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, "BE",
            null, NOW, NOW);
    private static final Blend BLEND = new Blend(BLEND_ID, BlendStatus.CREATED, UUID.randomUUID(), CUSTOMER.getId(), QUIZ_ID, NOW, NOW);

    private static final UUID PP_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final PaymentPlan PAYMENT_PLAN = new PaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE, AMOUNT_VALUE, 1, CUSTOMER.getId(), BLEND.getId(), PP_EXTERNAL_REFERENCE,
            NOW, null, PaymentPlanStatus.PENDING, NOW, null, NOW,
            Optional.empty(), Optional.empty(), null);
    private static final PaymentPlan PAYMENT_PLAN_CANCELED = new PaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE, AMOUNT_VALUE, 1, CUSTOMER.getId(), BLEND.getId(), PP_EXTERNAL_REFERENCE,
            NOW, null, PaymentPlanStatus.CANCELED, NOW, null, NOW,
            Optional.empty(), Optional.empty(), null);
    private static final BigDecimal REFERRAL_DISCOUNT = new BigDecimal("10.00");
    private static final Referral REFERRAL = new Referral(REFERRAL_ID, CUSTOMER_ID, null, REFERRAL_DISCOUNT, ReferralStatus.PENDING, NOW, NOW);
    private static final Incentive INCENTIVE_DISCOUNT = new Incentive(INCENTIVE_ID, CUSTOMER_ID, new BigDecimal("0.10"), IncentiveStatus.PENDING, IncentiveType.DISCOUNT, NOW, NOW);
    private static final BigDecimal AMOUNT_AFTER_DISCOUNT = new BigDecimal("18.00");
    private static final PaymentResponse PAYMENT_RESPONSE = mock(PaymentResponse.class);
    private static final Map<String, Object> EMPTY_METADATA = Collections.emptyMap();
    private static final Payment PAYMENT = new Payment(PAYMENT_ID, AMOUNT_VALUE, MOLLIE_PAYMENT_ID, null, PAYMENT_PLAN_ID, NOW, NOW, NOW, PaymentStatus.pending, null, SequenceType.RECURRING);
    private static final Throwable EXCEPTION = new Exception("TEST");
    private static final String MOLLIE_EXCEPTION_DETAIL = "MOLLIE_EXCEPTION_DETAIL";

    private final PaymentService paymentService = mock(PaymentService.class);
    private final PaymentPlanService paymentPlanService = mock(PaymentPlanService.class);
    private final PaymentConfiguration paymentConfiguration = mock(PaymentConfiguration.class);
    private final CustomerService customerService = mock(CustomerService.class);
    private final MollieService mollieService = mock(MollieService.class);
    private final EmailService emailService = mock(EmailService.class);
    private final CouponService couponService = mock(CouponService.class);
    private final ReferralService referralService = mock(ReferralService.class);
    private final IncentiveService incentiveService = mock(IncentiveService.class);
    private final PaymentPlanRepository paymentPlanRepository = mock(PaymentPlanRepository.class);
    private final LoggingService loggingService = Mockito.mock(LoggingService.class);

    private PaymentRecurringService paymentRecurringService;

    @BeforeEach
    void setUp() {
        when(paymentConfiguration.getRecurringDescription()).thenReturn("desc");
        paymentRecurringService = new PaymentRecurringServiceImpl(
                paymentService,
                paymentPlanService,
                paymentConfiguration,
                customerService,
                mollieService,
                emailService,
                couponService,
                referralService,
                incentiveService,
                paymentPlanRepository,
                loggingService);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
                paymentService,
                paymentPlanService,
                customerService,
                mollieService,
                emailService,
                couponService,
                referralService,
                incentiveService,
                paymentPlanRepository,
                loggingService
        );

        reset(
                paymentService,
                paymentPlanService,
                customerService,
                mollieService,
                emailService,
                couponService,
                referralService,
                incentiveService,
                paymentPlanRepository,
                loggingService
        );
    }

    @Test
    void subscriptionPaymentsOk() {
        final String description = paymentConfiguration.getRecurringDescription() + " " + PP_EXTERNAL_REFERENCE;
        mockRecurringPaymentResponsePaid();

        when(paymentPlanService.findActivePaymentPlansWithNoPayment(DAYS_AGO))
                .thenReturn(Try.success(List.of(PAYMENT_PLAN)));
        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(couponService.findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID))
                .thenReturn(Try.failure(EXCEPTION));
        when(referralService.findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID))
                .thenReturn(Try.failure(EXCEPTION));
        when(incentiveService.findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT))
                .thenReturn(Try.failure(EXCEPTION));
        when(mollieService.createRecurringPayment(CUSTOMER, AMOUNT_VALUE, description, PP_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(PAYMENT_RESPONSE));
        when(paymentService.savePayment(PAYMENT_PLAN_ID, PAYMENT_RESPONSE))
                .thenReturn(Try.success(PAYMENT));

        final Try<Seq<Long>> subscriptionPayments = paymentRecurringService.subscriptionPayments(DAYS_AGO);
        assertTrue(subscriptionPayments.isSuccess());
        assertEquals(subscriptionPayments.get().size(), 1);

        verify(paymentPlanService).findActivePaymentPlansWithNoPayment(DAYS_AGO);
        verify(customerService).find(CUSTOMER_ID);
        verify(couponService).findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID);
        verify(referralService).findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID);
        verify(incentiveService).findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT);
        verify(mollieService).createRecurringPayment(CUSTOMER, AMOUNT_VALUE, description, PP_EXTERNAL_REFERENCE);
        verify(paymentService).savePayment(PAYMENT_PLAN_ID, PAYMENT_RESPONSE);
    }

    @Test
    void subscriptionPaymentsReferralOk() {
        final String description = paymentConfiguration.getRecurringDescription() + " " + PP_EXTERNAL_REFERENCE;
        mockRecurringPaymentResponsePaid();

        when(paymentPlanService.findActivePaymentPlansWithNoPayment(DAYS_AGO))
                .thenReturn(Try.success(List.of(PAYMENT_PLAN)));
        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(couponService.findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID))
                .thenReturn(Try.failure(EXCEPTION));
        when(referralService.findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID))
                .thenReturn(Try.success(Optional.of(REFERRAL)));
        when(incentiveService.findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT))
                .thenReturn(Try.success(Optional.of(INCENTIVE_DISCOUNT)));
        when(mollieService.createRecurringPayment(CUSTOMER, REFERRAL_DISCOUNT, description, PP_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(PAYMENT_RESPONSE));
        when(paymentService.savePayment(PAYMENT_PLAN_ID, PAYMENT_RESPONSE))
                .thenReturn(Try.success(PAYMENT));
        when(referralService.updateStatus(REFERRAL_ID, ReferralStatus.COMPLETED))
                .thenReturn(Try.success(REFERRAL));

        final Try<Seq<Long>> subscriptionPayments = paymentRecurringService.subscriptionPayments(DAYS_AGO);
        assertTrue(subscriptionPayments.isSuccess());
        assertEquals(subscriptionPayments.get().size(), 1);

        verify(paymentPlanService).findActivePaymentPlansWithNoPayment(DAYS_AGO);
        verify(customerService).find(CUSTOMER_ID);
        verify(couponService).findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID);
        verify(referralService).findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID);
        verify(incentiveService).findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT);
        verify(mollieService).createRecurringPayment(CUSTOMER, REFERRAL_DISCOUNT, description, PP_EXTERNAL_REFERENCE);
        verify(paymentService).savePayment(PAYMENT_PLAN_ID, PAYMENT_RESPONSE);
        verify(referralService).updateStatus(REFERRAL_ID, ReferralStatus.COMPLETED);
    }


    @Test
    void subscriptionPaymentsReferralOkIncentiveFail() {
        final String description = paymentConfiguration.getRecurringDescription() + " " + PP_EXTERNAL_REFERENCE;
        mockRecurringPaymentResponsePaid();

        when(paymentPlanService.findActivePaymentPlansWithNoPayment(DAYS_AGO))
                .thenReturn(Try.success(List.of(PAYMENT_PLAN)));
        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(couponService.findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID))
                .thenReturn(Try.failure(EXCEPTION));
        when(referralService.findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID))
                .thenReturn(Try.success(Optional.of(REFERRAL)));
        when(incentiveService.findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT))
                .thenReturn(Try.failure(EXCEPTION));
        when(mollieService.createRecurringPayment(CUSTOMER, REFERRAL_DISCOUNT, description, PP_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(PAYMENT_RESPONSE));
        when(paymentService.savePayment(PAYMENT_PLAN_ID, PAYMENT_RESPONSE))
                .thenReturn(Try.success(PAYMENT));
        when(referralService.updateStatus(REFERRAL_ID, ReferralStatus.COMPLETED))
                .thenReturn(Try.success(REFERRAL));

        final Try<Seq<Long>> subscriptionPayments = paymentRecurringService.subscriptionPayments(DAYS_AGO);
        assertTrue(subscriptionPayments.isSuccess());
        assertEquals(subscriptionPayments.get().size(), 1);

        verify(paymentPlanService).findActivePaymentPlansWithNoPayment(DAYS_AGO);
        verify(customerService).find(CUSTOMER_ID);
        verify(couponService).findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID);
        verify(referralService).findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID);
        verify(incentiveService).findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT);
        verify(mollieService).createRecurringPayment(CUSTOMER, REFERRAL_DISCOUNT, description, PP_EXTERNAL_REFERENCE);
        verify(paymentService).savePayment(PAYMENT_PLAN_ID, PAYMENT_RESPONSE);
        verify(referralService).updateStatus(REFERRAL_ID, ReferralStatus.COMPLETED);
    }


    @Test
    void subscriptionPaymentsDiscountOk() {
        final String description = paymentConfiguration.getRecurringDescription() + " " + PP_EXTERNAL_REFERENCE;
        mockRecurringPaymentResponsePaid();

        when(paymentPlanService.findActivePaymentPlansWithNoPayment(DAYS_AGO))
                .thenReturn(Try.success(List.of(PAYMENT_PLAN)));
        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(couponService.findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID))
                .thenReturn(Try.failure(EXCEPTION));
        when(referralService.findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID))
                .thenReturn(Try.failure(EXCEPTION));
        when(incentiveService.findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT))
                .thenReturn(Try.success(Optional.of(INCENTIVE_DISCOUNT)));
        when(mollieService.createRecurringPayment(CUSTOMER, AMOUNT_AFTER_DISCOUNT, description, PP_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(PAYMENT_RESPONSE));
        when(paymentService.savePayment(PAYMENT_PLAN_ID, PAYMENT_RESPONSE))
                .thenReturn(Try.success(PAYMENT));
        when(incentiveService.updateStatus(INCENTIVE_ID, IncentiveStatus.COMPLETED))
                .thenReturn(Try.success(INCENTIVE_DISCOUNT));

        final Try<Seq<Long>> subscriptionPayments = paymentRecurringService.subscriptionPayments(DAYS_AGO);
        assertTrue(subscriptionPayments.isSuccess());
        assertEquals(subscriptionPayments.get().size(), 1);

        verify(paymentPlanService).findActivePaymentPlansWithNoPayment(DAYS_AGO);
        verify(customerService).find(CUSTOMER_ID);
        verify(couponService).findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID);
        verify(referralService).findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID);
        verify(incentiveService).findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT);
        verify(mollieService).createRecurringPayment(CUSTOMER, AMOUNT_AFTER_DISCOUNT, description, PP_EXTERNAL_REFERENCE);
        verify(paymentService).savePayment(PAYMENT_PLAN_ID, PAYMENT_RESPONSE);
        verify(incentiveService).updateStatus(INCENTIVE_ID, IncentiveStatus.COMPLETED);
    }

    @Test
    void subscriptionPaymentsMollieExceptionOk() {
        final String description = paymentConfiguration.getRecurringDescription() + " " + PP_EXTERNAL_REFERENCE;
        final PaymentResponse paymentResponse = PaymentResponse.builder()
                .amount(new Amount(MollieServiceImpl.EUR, AMOUNT_VALUE))
                .amountRefunded(Optional.empty())
                .status(be.woutschoovaerts.mollie.data.payment.PaymentStatus.FAILED)
                .sequenceType(SequenceType.RECURRING)
                .details(PaymentDetailsResponse.builder().bankReasonCode(Optional.of(MOLLIE_EXCEPTION_DETAIL)).build())
                .paidAt(Optional.empty())
                .build();

        when(paymentPlanService.findActivePaymentPlansWithNoPayment(DAYS_AGO))
                .thenReturn(Try.success(List.of(PAYMENT_PLAN)));
        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(couponService.findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID))
                .thenReturn(Try.failure(EXCEPTION));
        when(referralService.findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID))
                .thenReturn(Try.failure(EXCEPTION));
        when(incentiveService.findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT))
                .thenReturn(Try.failure(EXCEPTION));
        when(mollieService.createRecurringPayment(CUSTOMER, AMOUNT_VALUE, description, PP_EXTERNAL_REFERENCE))
                .thenReturn(Try.failure(new MollieException("MollieExceptionTest", Map.of("status", 422, "detail", MOLLIE_EXCEPTION_DETAIL))));
        when(paymentPlanRepository.update(PAYMENT_PLAN_CANCELED))
                .thenReturn(Try.success(PAYMENT_PLAN_CANCELED));
        when(paymentService.savePayment(PAYMENT_PLAN_ID, paymentResponse))
                .thenReturn(Try.success(PAYMENT));

        final Try<Seq<Long>> subscriptionPayments = paymentRecurringService.subscriptionPayments(DAYS_AGO);
        assertTrue(subscriptionPayments.isSuccess());
        assertEquals(subscriptionPayments.get().size(), 1);

        verify(paymentPlanService).findActivePaymentPlansWithNoPayment(DAYS_AGO);
        verify(customerService).find(CUSTOMER_ID);
        verify(couponService).findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID);
        verify(referralService).findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID);
        verify(incentiveService).findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT);
        verify(mollieService).createRecurringPayment(CUSTOMER, AMOUNT_VALUE, description, PP_EXTERNAL_REFERENCE);
        verify(paymentPlanRepository).update(PAYMENT_PLAN_CANCELED);
        verify(loggingService).create(CUSTOMER_ID, LoggingEvent.PAYMENT_PLAN_CANCELED, "Abonnement door systeem stopgezet vanwege Mollie: " + MOLLIE_EXCEPTION_DETAIL);
        verify(paymentService).savePayment(PAYMENT_PLAN_ID, paymentResponse);
        verify(emailService).sendPaymentMissing(CUSTOMER.getFirstName(), CUSTOMER.getEmail(), PAYMENT, PAYMENT_PLAN_CANCELED.externalReference(), 1);
    }

    @Test
    void subscriptionPaymentsMollieExceptionFail() {
        final String description = paymentConfiguration.getRecurringDescription() + " " + PP_EXTERNAL_REFERENCE;

        when(paymentPlanService.findActivePaymentPlansWithNoPayment(DAYS_AGO))
                .thenReturn(Try.success(List.of(PAYMENT_PLAN)));
        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(couponService.findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID))
                .thenReturn(Try.failure(EXCEPTION));
        when(referralService.findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID))
                .thenReturn(Try.failure(EXCEPTION));
        when(incentiveService.findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT))
                .thenReturn(Try.failure(EXCEPTION));
        when(mollieService.createRecurringPayment(CUSTOMER, AMOUNT_VALUE, description, PP_EXTERNAL_REFERENCE))
                .thenReturn(Try.failure(new MollieException("MollieExceptionTest", Map.of("status", 503))));

        final Try<Seq<Long>> subscriptionPayments = paymentRecurringService.subscriptionPayments(DAYS_AGO);
        assertTrue(subscriptionPayments.isFailure());
        assertTrue(subscriptionPayments.getCause() instanceof MollieException);

        verify(paymentPlanService).findActivePaymentPlansWithNoPayment(DAYS_AGO);
        verify(customerService).find(CUSTOMER_ID);
        verify(couponService).findDiscountByPaymentPlan(PAYMENT_PLAN.id(), CouponDiscountStatus.VALID);
        verify(referralService).findFromCustomer(CUSTOMER_ID, ReferralStatus.PAID);
        verify(incentiveService).findFromCustomer(CUSTOMER_ID, IncentiveStatus.PENDING, IncentiveType.DISCOUNT);
        verify(mollieService).createRecurringPayment(CUSTOMER, AMOUNT_VALUE, description, PP_EXTERNAL_REFERENCE);
    }

    private void mockRecurringPaymentResponsePaid() {
        mockInnerPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus.PAID, SequenceType.RECURRING, EMPTY_METADATA);
    }

    private void mockInnerPaymentResponse(be.woutschoovaerts.mollie.data.payment.PaymentStatus paymentStatus, SequenceType sequenceType, Map<String, Object> metadata) {
        // TODO: use PaymentResponseBuilder
        when(PAYMENT_RESPONSE.getId())
                .thenReturn(MOLLIE_PAYMENT_ID);
        when(PAYMENT_RESPONSE.getMetadata())
                .thenReturn(metadata);
        when(PAYMENT_RESPONSE.getAmount())
                .thenReturn(AMOUNT);
        when(PAYMENT_RESPONSE.getAmountRefunded())
                .thenReturn(Optional.of(AMOUNT_ZERO));
        when(PAYMENT_RESPONSE.getStatus())
                .thenReturn(paymentStatus);
        when(PAYMENT_RESPONSE.getSequenceType())
                .thenReturn(sequenceType);
        when(PAYMENT_RESPONSE.getCreatedAt())
                .thenReturn(NOW_O);
        when(PAYMENT_RESPONSE.getPaidAt())
                .thenReturn(Optional.of(NOW_O));
    }
}