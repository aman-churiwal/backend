package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.OrderRepository;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.fulfilment.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderRepositoryImpl implements OrderRepository {

    private static final String SELECT_ALL = "SELECT * FROM orders ";

    private static final String ORDER_BY_CREATION_DATE_DESC = "ORDER BY created DESC";

    private static final String ORDER_BY_ID_DESC_LIMIT_1 = "ORDER BY id DESC LIMIT 1";

    private static final String SELECT_LAST_DUPLICATE_ORDER = SELECT_ALL + "WHERE order_number < 0001500000 " + ORDER_BY_ID_DESC_LIMIT_1;

    private static final String INSERT_QUERY = "" +
            "INSERT INTO " +
            "orders (external_reference, order_number, payment_id, sequence_type, payment_plan_id, blend_id, customer_id, recurring_months, " +
            "first_name, last_name, street, house_number, house_number_addition, postcode, city, country, phone_number, email, referral_code, status) " +
            "VALUES (:externalReference, :orderNumber, :paymentId, :sequenceType, :paymentPlanId, :blendId, :customerId, :recurringMonths, " +
            ":shipToFirstName, :shipToLastName, :shipToStreet, :shipToHouseNo, :shipToAnnex, :shipToPostalCode, :shipToCity, :shipToCountryCode, " +
            ":shipToPhone, :shipToEmail, :referralCode, :status)";

    private static final String UPSERT_QUERY = "" +
            "INSERT INTO " +
            "orders (external_reference, order_number, payment_id, sequence_type, payment_plan_id, blend_id, customer_id, recurring_months, " +
            "first_name, last_name, street, house_number, house_number_addition, postcode, city, country, phone_number, email, referral_code, status) " +
            "VALUES (:externalReference, :orderNumber, :paymentId, :sequenceType, :paymentPlanId, :blendId, :customerId, :recurringMonths, " +
            ":shipToFirstName, :shipToLastName, :shipToStreet, :shipToHouseNo, :shipToAnnex, :shipToPostalCode, :shipToCity, :shipToCountryCode, " +
            ":shipToPhone, :shipToEmail, :referralCode, :status) " +
            "ON DUPLICATE KEY UPDATE status = :status, tracktrace = :trackTraceCode, pharmacist_order_number = :pharmacistOrderNumber, shipped = :shipped, last_modified = NOW(), id = LAST_INSERT_ID(id)";

    private final Jdbi jdbi;

    public OrderRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<Order> find(Long id) {
        final HandleCallback<Order, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Order.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<Order>> find(String orderNumber) {
        final HandleCallback<Optional<Order>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE order_number = :orderNumber")
                .bind("orderNumber", orderNumber)
                .mapTo(Order.class)
                .findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Order> find(UUID externalReference) {
        final HandleCallback<Order, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE external_reference = :externalReference")
                .bind("externalReference", externalReference)
                .mapTo(Order.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Order>> findByStatus(OrderStatus status) {
        final HandleCallback<List<Order>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE status = :status")
                .bind("status", status)
                .mapTo(Order.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Order> findByLastDuplicateOrderNumber() {
        final HandleCallback<Order, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_LAST_DUPLICATE_ORDER)
                .mapTo(Order.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Order> findLatestByPaymentId(Long paymentId) {
        final HandleCallback<Order, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE payment_id = :paymentId " + ORDER_BY_ID_DESC_LIMIT_1)
                .bind("paymentId", paymentId)
                .mapTo(Order.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Order> findByTrackTrace(String trackTrace) {
        final HandleCallback<Order, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE tracktrace = :trackTrace")
                .bind("trackTrace", trackTrace)
                .mapTo(Order.class)
                .one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<Order>> findLatestByCustomerId(Long customerId) {
        final HandleCallback<Optional<Order>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE customer_id = :customerId " + ORDER_BY_ID_DESC_LIMIT_1)
                .bind("customerId", customerId)
                .mapTo(Order.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Order>> findAllByCustomerId(Long customerId) {
        final HandleCallback<List<Order>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE customer_id = :customerId " + ORDER_BY_CREATION_DATE_DESC)
                .bind("customerId", customerId)
                .mapTo(Order.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Order>> findByTrackTraceNotDelivered() {
        final HandleCallback<List<Order>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE tracktrace is not null AND status in ('SHIPPED_TO_CUSTOMER','SHIPMENT_AT_PICK_UP_LOCATION')")
                .mapTo(Order.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Order>> findByPharmacistOrderNumber(String pharmacistOrderNumber) {
        final HandleCallback<List<Order>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE pharmacist_order_number = :pharmacistOrderNumber")
                .bind("pharmacistOrderNumber", pharmacistOrderNumber)
                .mapTo(Order.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Order> save(Order order) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPSERT_QUERY)
                .bindBean(order)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }

    @Override
    public Try<Order> saveCopy(Order order) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(order)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }
}
