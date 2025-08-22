package viteezy.db;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.payment.Payment;

import java.util.List;

public interface PaymentRepository {
    Try<Payment> find(Long id);

    Try<List<Payment>> findByPaymentPlanId(Long paymentPlanId);

    Try<List<Payment>> findByCustomerId(Long customerId);

    @Transactional(transactionManager = "transactionManager")
    Try<Payment> save(Payment payment);

    Try<List<Payment>> findByChargebackDate(Integer daysAgo, Integer daysUntil);

    Try<Payment> findByMolliePaymentId(String molliePaymentId);

    Try<List<Payment>> findByRetriedMolliePaymentId(String molliePaymentId);

    Try<List<Payment>> findFailedChargebackRecurringByPaymentPlanId(Long paymentPlanId);
}
