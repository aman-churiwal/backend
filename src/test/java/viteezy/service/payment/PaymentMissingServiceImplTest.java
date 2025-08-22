package viteezy.service.payment;

import be.woutschoovaerts.mollie.data.payment.SequenceType;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viteezy.db.PaymentPlanRepository;
import viteezy.db.PaymentRepository;
import viteezy.domain.Customer;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.domain.payment.PaymentStatus;
import viteezy.service.CustomerService;
import viteezy.service.mail.EmailService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PaymentMissingServiceImplTest {

    private static final Integer DAYS_AGO = 14;
    private static final Integer DAYS_UNTIL = 15;
    private static final Integer DAYS_AGO_3 = 28;
    private static final Integer DAYS_UNTIL_3 = 28+30;

    private static final Long ID = 1L;
    private static final Long PAYMENT_PLAN_ID = 2L;
    private static final Long CUSTOMER_ID = 3L;
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final Payment PAYMENT_PAID = new Payment(ID, BigDecimal.TEN, "molliePaymentId",
            null, PAYMENT_PLAN_ID, NOW, NOW, NOW,
            PaymentStatus.paid, null, SequenceType.FIRST);

    private static final Payment PAYMENT_PAID_FAILED = new Payment(ID, BigDecimal.TEN, "failedMolliePaymentId",
            null, PAYMENT_PLAN_ID, NOW, NOW, NOW,
            PaymentStatus.failed, null, SequenceType.FIRST);

    private static final Customer CUSTOMER = new Customer(CUSTOMER_ID, "email", true, UUID.randomUUID(),
            "mollieCustomerId", null, null, null, null, null, null, null, null, null, null, null, null, null, null,null, "BE",
            null, NOW, NOW);

    private static final PaymentPlan PAYMENT_PLAN = new PaymentPlan(PAYMENT_PLAN_ID, null, null, null, CUSTOMER_ID, null, UUID.randomUUID(),
            NOW, NOW, PaymentPlanStatus.PENDING, NOW, null, NOW, Optional.empty(), Optional.empty(), null);

    private final PaymentRepository paymentRepository = mock(PaymentRepository.class);
    private final PaymentPlanRepository paymentPlanRepository = mock(PaymentPlanRepository.class);
    private final CustomerService customerService = mock(CustomerService.class);
    private final EmailService emailService = mock(EmailService.class);

    private PaymentMissingService paymentMissingService;

    @BeforeEach
    void setUp() {
        paymentMissingService = new PaymentMissingServiceImpl(
                paymentRepository,
                paymentPlanRepository,
                customerService,
                emailService);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
                paymentRepository,
                paymentPlanRepository,
                customerService,
                emailService);

        reset(
                paymentRepository,
                paymentPlanRepository,
                customerService,
                emailService);
    }

    @Test
    void empty() {
        when(paymentRepository.findByChargebackDate(DAYS_AGO, DAYS_UNTIL))
                .thenReturn(Try.success(Collections.emptyList()));

        final Try<Seq<Long>> result = paymentMissingService.missingPaymentsByChargebackDate(2);

        assertTrue(result.isSuccess());
        assertEquals(result.get().size(), 0);

        verify(paymentRepository).findByChargebackDate(DAYS_AGO, DAYS_UNTIL);
    }

    @Test
    void empty3() {
        when(paymentRepository.findByChargebackDate(DAYS_AGO_3, DAYS_UNTIL_3))
                .thenReturn(Try.success(Collections.emptyList()));

        final Try<Seq<Long>> result = paymentMissingService.missingPaymentsByChargebackDate(3);

        assertTrue(result.isSuccess());
        assertEquals(result.get().size(), 0);

        verify(paymentRepository).findByChargebackDate(DAYS_AGO_3, DAYS_UNTIL_3);
    }

    @Test
    void ok2() {
        when(paymentRepository.findByChargebackDate(DAYS_AGO, DAYS_UNTIL))
                .thenReturn(Try.success(List.of(PAYMENT_PAID)));

        when(paymentRepository.findByRetriedMolliePaymentId(PAYMENT_PAID.getMolliePaymentId()))
                .thenReturn(Try.success(Collections.emptyList()));

        when(paymentPlanRepository.find(PAYMENT_PAID.getPaymentPlanId()))
                .thenReturn(Try.success(PAYMENT_PLAN));

        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));

        final Try<Seq<Long>> result = paymentMissingService.missingPaymentsByChargebackDate(2);

        assertTrue(result.isSuccess());
        assertEquals(result.get().size(), 1);

        verify(paymentRepository).findByChargebackDate(DAYS_AGO, DAYS_UNTIL);
        verify(paymentRepository).findByRetriedMolliePaymentId(PAYMENT_PAID.getMolliePaymentId());
        verify(paymentPlanRepository).find(PAYMENT_PAID.getPaymentPlanId());
        verify(customerService).find(CUSTOMER_ID);
        verify(emailService).sendPaymentMissing(CUSTOMER.getFirstName(), CUSTOMER.getEmail(), PAYMENT_PAID, PAYMENT_PLAN.externalReference(), 2);
    }

    @Test
    void ok3() {
        when(paymentRepository.findByChargebackDate(DAYS_AGO_3, DAYS_UNTIL_3))
                .thenReturn(Try.success(List.of(PAYMENT_PAID)));

        when(paymentRepository.findByRetriedMolliePaymentId(PAYMENT_PAID.getMolliePaymentId()))
                .thenReturn(Try.success(Collections.emptyList()));

        when(paymentPlanRepository.find(PAYMENT_PAID.getPaymentPlanId()))
                .thenReturn(Try.success(PAYMENT_PLAN));

        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));

        final Try<Seq<Long>> result = paymentMissingService.missingPaymentsByChargebackDate(3);

        assertTrue(result.isSuccess());
        assertEquals(result.get().size(), 1);

        verify(paymentRepository).findByChargebackDate(DAYS_AGO_3, DAYS_UNTIL_3);
        verify(paymentRepository).findByRetriedMolliePaymentId(PAYMENT_PAID.getMolliePaymentId());
        verify(paymentPlanRepository).find(PAYMENT_PAID.getPaymentPlanId());
        verify(customerService).find(CUSTOMER_ID);
        verify(emailService).sendPaymentMissing(CUSTOMER.getFirstName(), CUSTOMER.getEmail(), PAYMENT_PAID, PAYMENT_PLAN.externalReference(), 3);
    }

    @Test
    void retried_paid() {
        when(paymentRepository.findByChargebackDate(DAYS_AGO, DAYS_UNTIL))
                .thenReturn(Try.success(List.of(PAYMENT_PAID_FAILED)));

        when(paymentRepository.findByRetriedMolliePaymentId(PAYMENT_PAID_FAILED.getMolliePaymentId()))
                .thenReturn(Try.success(List.of(PAYMENT_PAID)));

        final Try<Seq<Long>> result = paymentMissingService.missingPaymentsByChargebackDate(2);

        assertTrue(result.isSuccess());
        assertEquals(result.get().size(), 0);

        verify(paymentRepository).findByChargebackDate(DAYS_AGO, DAYS_UNTIL);
        verify(paymentRepository).findByRetriedMolliePaymentId(PAYMENT_PAID_FAILED.getMolliePaymentId());
    }


    @Test
    void unPaid() {
        when(paymentRepository.findByChargebackDate(DAYS_AGO, DAYS_UNTIL))
                .thenReturn(Try.success(List.of(PAYMENT_PAID_FAILED)));

        when(paymentRepository.findByRetriedMolliePaymentId(PAYMENT_PAID_FAILED.getMolliePaymentId()))
                .thenReturn(Try.success(List.of(PAYMENT_PAID_FAILED)));

        when(paymentPlanRepository.find(PAYMENT_PAID_FAILED.getPaymentPlanId()))
                .thenReturn(Try.success(PAYMENT_PLAN));

        when(customerService.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));

        final Try<Seq<Long>> result = paymentMissingService.missingPaymentsByChargebackDate(2);

        assertTrue(result.isSuccess());
        assertEquals(result.get().size(), 1);

        verify(paymentRepository).findByChargebackDate(DAYS_AGO, DAYS_UNTIL);
        verify(paymentRepository).findByRetriedMolliePaymentId(PAYMENT_PAID_FAILED.getMolliePaymentId());
        verify(paymentPlanRepository).find(PAYMENT_PAID_FAILED.getPaymentPlanId());
        verify(customerService).find(CUSTOMER_ID);
        verify(emailService).sendPaymentMissing(CUSTOMER.getFirstName(), CUSTOMER.getEmail(), PAYMENT_PAID_FAILED, PAYMENT_PLAN.externalReference(), 2);
    }

}