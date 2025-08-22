package viteezy.db;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.payment.PaymentPlanStatus;

import java.util.List;
import java.util.UUID;

public interface PaymentPlanRepository {
    Try<PaymentPlan> find(Long id);

    Try<PaymentPlan> find(UUID externalReference);

    Try<PaymentPlan> find(String mollieSubscriptionId);

    Try<PaymentPlan> findByBlendId(Long blendId, PaymentPlanStatus paymentPlanStatus);

    Try<List<PaymentPlan>> findAllByCustomerId(Long customerId);

    Try<PaymentPlan> findByCustomerId(Long customerId, PaymentPlanStatus paymentPlanStatus);

    Try<List<PaymentPlan>> findAllByStatusAndNoPayment(Integer daysAgo, PaymentPlanStatus paymentPlanStatus);

    @Transactional(transactionManager = "transactionManager")
    Try<PaymentPlan> save(PaymentPlan paymentPlan);

    @Transactional(transactionManager = "transactionManager")
    Try<PaymentPlan> saveCopy(PaymentPlan paymentPlan);

    @Transactional(transactionManager = "transactionManager")
    Try<PaymentPlan> update(PaymentPlan paymentPlan);

    Try<Void> updatePaymentDateWithNextPaymentDate();

    Try<Void> updateDeliveryDateWithNextDeliveryDate();

    Try<Void> updatePaymentDate();

    Try<Void> updateDeliveryDate();

    Try<List<PaymentPlan>>  findByPaymentDate(Integer daysUntil, PaymentPlanStatus paymentPlanStatus);
}
