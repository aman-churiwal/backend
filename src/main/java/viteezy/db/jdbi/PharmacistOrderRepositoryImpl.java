package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.PharmacistOrderRepository;
import viteezy.domain.fulfilment.PharmacistOrder;
import viteezy.domain.fulfilment.PharmacistOrderStatus;

import java.util.List;

public class PharmacistOrderRepositoryImpl implements PharmacistOrderRepository {

    private static final String SELECT_ALL = "SELECT * FROM pharmacist_orders ";
    private static final String ORDER_BY_ID_DESC_LIMIT_1 = "ORDER BY id DESC LIMIT 1";

    private static final String UPSERT_QUERY = "" +
            "INSERT INTO " +
            "pharmacist_orders (id, batch_name, batch_number, order_number, file_name, status, creation_timestamp, modification_timestamp) " +
            "VALUES (:id, :batchName, :batchNumber, :orderNumber, :fileName, :status, :creationTimestamp, :modificationTimestamp) " +
            "ON DUPLICATE KEY UPDATE status = :status, modification_timestamp = NOW(), id = LAST_INSERT_ID(id)";

    private final Jdbi jdbi;

    public PharmacistOrderRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<PharmacistOrder> find(Long id) {
        final HandleCallback<PharmacistOrder, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(PharmacistOrder.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<PharmacistOrder> find(String fileName) {
        final HandleCallback<PharmacistOrder, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE file_name = :fileName")
                .bind("fileName", fileName)
                .mapTo(PharmacistOrder.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<PharmacistOrder> findByLastInsertedId() {
        final HandleCallback<PharmacistOrder, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + ORDER_BY_ID_DESC_LIMIT_1)
                .mapTo(PharmacistOrder.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<PharmacistOrder>> findByStatus(PharmacistOrderStatus pharmacistOrderStatus) {
        final HandleCallback<List<PharmacistOrder>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE status = :status")
                .bind("status", pharmacistOrderStatus)
                .mapTo(PharmacistOrder.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<PharmacistOrder> save(PharmacistOrder order) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPSERT_QUERY)
                .bindBean(order)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }
}
