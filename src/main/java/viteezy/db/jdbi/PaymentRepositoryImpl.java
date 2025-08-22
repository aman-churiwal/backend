package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.PaymentRepository;
import viteezy.domain.payment.Payment;

import java.util.List;

public class PaymentRepositoryImpl implements PaymentRepository {
    private static final String SELECT_ALL_BASE_QUERY = "SELECT * FROM payments p ";
    private static final String UPSERT_QUERY = "" +
            "INSERT INTO " +
            "payments (amount,mollie_payment_id,retried_mollie_payment_id,payment_plan_id,creation_date,payment_date,status,reason,sequence_type) " +
            "VALUES (:amount,:molliePaymentId,:retriedMolliePaymentId,:paymentPlanId,:creationDate,:paymentDate,:status,:reason,lower(:sequenceType)) " +
            "ON DUPLICATE KEY UPDATE status = :status, reason = :reason, creation_date = :creationDate, payment_date = :paymentDate, id = LAST_INSERT_ID(id)";

    private static final String ORDER_BY_CREATION_DATE_DESC = "ORDER BY p.creation_date DESC";

    private final Jdbi jdbi;

    public PaymentRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<Payment> find(Long id) {
        final HandleCallback<Payment, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Payment.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Payment>> findByPaymentPlanId(Long paymentPlanId) {
        final HandleCallback<List<Payment>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE payment_plan_id = :paymentPlanId " + ORDER_BY_CREATION_DATE_DESC)
                .bind("paymentPlanId", paymentPlanId)
                .mapTo(Payment.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Payment>> findByCustomerId(Long customerId) {
        final HandleCallback<List<Payment>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "JOIN payment_plans pp ON pp.id=p.payment_plan_id WHERE pp.customer_id = :customerId " + ORDER_BY_CREATION_DATE_DESC)
                .bind("customerId", customerId)
                .mapTo(Payment.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Payment> save(Payment payment) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPSERT_QUERY)
                .bindBean(payment)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }

    @Override
    public Try<List<Payment>> findByChargebackDate(Integer daysAgo, Integer daysUntil) {
        final HandleCallback<List<Payment>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "JOIN payment_plans pp ON p.payment_plan_id = pp.id WHERE pp.status in ('CANCELED','STOPPED','SUSPENDED') AND p.status in ('chargeback','failed') AND p.sequence_type='recurring' AND p.last_modified between DATE_SUB(curdate(),INTERVAL :daysUntil DAY) and DATE_SUB(curdate(),INTERVAL :daysAgo DAY)")
                .bind("daysAgo", daysAgo)
                .bind("daysUntil", daysUntil)
                .mapTo(Payment.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Payment> findByMolliePaymentId(String molliePaymentId) {
        final HandleCallback<Payment, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE mollie_payment_id = :molliePaymentId")
                .bind("molliePaymentId", molliePaymentId)
                .mapTo(Payment.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Payment>> findByRetriedMolliePaymentId(String molliePaymentId) {
        final HandleCallback<List<Payment>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE retried_mollie_payment_id = :molliePaymentId")
                .bind("molliePaymentId", molliePaymentId)
                .mapTo(Payment.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Payment>> findFailedChargebackRecurringByPaymentPlanId(Long paymentPlanId) {
        final HandleCallback<List<Payment>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE p.status IN ('chargeback','failed') AND p.payment_plan_id = :paymentPlanId AND p.sequence_type='recurring' " + ORDER_BY_CREATION_DATE_DESC)
                .bind("paymentPlanId", paymentPlanId)
                .mapTo(Payment.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }
}
