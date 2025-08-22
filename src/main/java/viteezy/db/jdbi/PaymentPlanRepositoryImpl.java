package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.PaymentPlanRepository;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.payment.PaymentPlanStatus;

import java.util.List;
import java.util.UUID;

public class PaymentPlanRepositoryImpl implements PaymentPlanRepository {
    private static final String SELECT_ALL_BASE_QUERY = "SELECT * FROM payment_plans ";
    private static final String ORDER_BY_CREATION_DATE_DESC = "ORDER BY creation_date DESC";
    private static final String INSERT_QUERY = "" +
            "INSERT INTO " +
            "payment_plans (first_amount,recurring_amount,recurring_months,customer_id,blend_id,external_reference,status,payment_date,next_payment_date,delivery_date,next_delivery_date,payment_method) " +
            "VALUES (:firstAmount,:recurringAmount,:recurringMonths,:customerId,:blendId,:externalReference,:status,:paymentDate,:nextPaymentDate,:deliveryDate,:nextDeliveryDate,:paymentMethod)";
    private static final String INSERT_QUERY_COPY = "" +
            "INSERT INTO " +
            "payment_plans (first_amount,recurring_amount,recurring_months,customer_id,blend_id,external_reference,creation_date,status,payment_date,next_payment_date,delivery_date,next_delivery_date,payment_method) " +
            "VALUES (:firstAmount,:recurringAmount,:recurringMonths,:customerId,:blendId,:externalReference,:creationDate,:status,:paymentDate,:nextPaymentDate,:deliveryDate,:nextDeliveryDate,:paymentMethod)";
    private static final String UPDATE_QUERY = "" +
            "UPDATE payment_plans SET blend_id = :blendId, recurring_months = :recurringMonths, recurring_amount = :recurringAmount, " +
            "last_modified=NOW(), status = :status, stop_reason = :stopReason, " +
            "payment_date = :paymentDate, delivery_date = :deliveryDate, " +
            "next_payment_date = :nextPaymentDate, next_delivery_date = :nextDeliveryDate";

    private static final String UPDATE_PAYMENT_DATE_WITH_NEXT_PAYMENT_DATE = "" +
            "UPDATE payment_plans " +
            "SET payment_date = next_payment_date, next_payment_date = NULL " +
            "WHERE next_payment_date IS NOT NULL AND delivery_date < NOW() " +
            "AND status = 'ACTIVE'";
    private static final String UPDATE_DELIVERY_DATE_WITH_NEXT_DELIVERY_DATE = "" +
            "UPDATE payment_plans " +
            "SET delivery_date = next_delivery_date, next_delivery_date = NULL " +
            "WHERE next_delivery_date IS NOT NULL " +
            "AND delivery_date < NOW() " +
            "AND status = 'ACTIVE'";
    private static final String UPDATE_PAYMENT_DATE = "" +
            "UPDATE payment_plans " +
            "SET payment_date = DATE_ADD(payment_date, INTERVAL recurring_months MONTH) " +
            "WHERE delivery_date < NOW() AND payment_date < NOW() " +
            "AND status = 'ACTIVE'";
    private static final String UPDATE_DELIVERY_DATE = "" +
            "UPDATE payment_plans " +
            "SET delivery_date = DATE_ADD(delivery_date, INTERVAL recurring_months MONTH) " +
            "WHERE delivery_date < NOW() " +
            "AND status = 'ACTIVE'";
    private final Jdbi jdbi;

    public PaymentPlanRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<PaymentPlan> find(Long id) {
        final HandleCallback<PaymentPlan, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE id = :id")
                .bind("id", id)
                .mapTo(PaymentPlan.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<PaymentPlan> find(UUID externalReference) {
        final HandleCallback<PaymentPlan, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE external_reference = :externalReference")
                .bind("externalReference", externalReference)
                .mapTo(PaymentPlan.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<PaymentPlan> find(String mollieSubscriptionId) {
        final HandleCallback<PaymentPlan, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE mollie_subscription_id = :mollieSubscriptionId")
                .bind("mollieSubscriptionId", mollieSubscriptionId)
                .mapTo(PaymentPlan.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<PaymentPlan> findByBlendId(Long blendId, PaymentPlanStatus paymentPlanStatus) {
        final HandleCallback<PaymentPlan, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE blend_id = :blendId AND status = :paymentPlanStatus ORDER BY last_modified DESC LIMIT 1")
                .bind("blendId", blendId)
                .bind("paymentPlanStatus", paymentPlanStatus)
                .mapTo(PaymentPlan.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<PaymentPlan>> findAllByCustomerId(Long customerId) {
        final HandleCallback<List<PaymentPlan>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE customer_id = :customerId " + ORDER_BY_CREATION_DATE_DESC )
                .bind("customerId", customerId)
                .mapTo(PaymentPlan.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<PaymentPlan> findByCustomerId(Long customerId, PaymentPlanStatus paymentPlanStatus) {
        final HandleCallback<PaymentPlan, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE customer_id = :customerId AND status = :paymentPlanStatus ORDER BY last_modified DESC LIMIT 1")
                .bind("customerId", customerId)
                .bind("paymentPlanStatus", paymentPlanStatus)
                .mapTo(PaymentPlan.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<PaymentPlan>> findAllByStatusAndNoPayment(Integer daysAgo, PaymentPlanStatus paymentPlanStatus) {
        final HandleCallback<List<PaymentPlan>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + " WHERE status = :status AND DATE(payment_date) = DATE_SUB(DATE(NOW()), INTERVAL :daysAgo DAY) AND id not in (SELECT payment_plan_id FROM payments p WHERE p.creation_date > (NOW() - INTERVAL :daysAgo DAY))")
                .bind("daysAgo", daysAgo)
                .bind("status", paymentPlanStatus)
                .mapTo(PaymentPlan.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<PaymentPlan> save(PaymentPlan paymentPlan) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindMethods(paymentPlan)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }

    @Override
    public Try<PaymentPlan> saveCopy(PaymentPlan paymentPlan) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY_COPY)
                .bindMethods(paymentPlan)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }

    @Override
    public Try<PaymentPlan> update(PaymentPlan paymentPlan) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_QUERY + " WHERE id = :id")
                .bindMethods(paymentPlan)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> find(paymentPlan.id()));
    }

    @Override
    public Try<Void> updatePaymentDateWithNextPaymentDate() {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_PAYMENT_DATE_WITH_NEXT_PAYMENT_DATE)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> Try.success(null));
    }

    @Override
    public Try<Void> updateDeliveryDateWithNextDeliveryDate() {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_DELIVERY_DATE_WITH_NEXT_DELIVERY_DATE)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> Try.success(null));
    }

    @Override
    public Try<Void> updatePaymentDate() {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_PAYMENT_DATE)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> Try.success(null));
    }

    @Override
    public Try<Void> updateDeliveryDate() {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_DELIVERY_DATE)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> Try.success(null));
    }

    @Override
    public Try<List<PaymentPlan>> findByPaymentDate(Integer daysUntil, PaymentPlanStatus paymentPlanStatus) {
        final HandleCallback<List<PaymentPlan>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + " WHERE status = :status AND DATE(payment_date) = DATE_ADD(DATE(NOW()), INTERVAL :daysUntil DAY)")
                .bind("daysUntil", daysUntil)
                .bind("status", paymentPlanStatus)
                .mapTo(PaymentPlan.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }
}
