package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.IncentiveRepository;
import viteezy.domain.pricing.Incentive;
import viteezy.domain.pricing.IncentiveType;
import viteezy.domain.pricing.IncentiveStatus;

import java.util.Optional;

public class IncentiveRepositoryImpl implements IncentiveRepository {
    private static final String SELECT_ALL_BASE_QUERY = "SELECT * FROM incentives ";
    private static final String INSERT_QUERY = "" +
            "INSERT INTO incentives (customer_id, amount, status, incentive_type, creation_date, last_modified) " +
            "VALUES (:customerId, :amount, :status, :type, :creationDate, :lastModified)";

    private static final String UPDATE_STATUS_QUERY = "" +
            "UPDATE incentives SET status = :status WHERE id = :id";

    private final Jdbi jdbi;

    public IncentiveRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<Optional<Incentive>> findFromCustomer(Long customerId, IncentiveStatus status, IncentiveType incentiveType) {
        final HandleCallback<Optional<Incentive>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE customer_id = :customerId AND status = :status AND incentive_type = :incentiveType ORDER BY last_modified LIMIT 1")
                .bind("customerId", customerId)
                .bind("status", status)
                .bind("incentiveType", incentiveType)
                .mapTo(Incentive.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Incentive> save(Incentive incentive) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(incentive)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }

    @Override
    public Try<Incentive> updateStatus(Long id, IncentiveStatus status) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_STATUS_QUERY)
                .bind("id", id)
                .bind("status", status)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(__ -> find(id));
    }

    private Try<Incentive> find(Long id) {
        final HandleCallback<Incentive, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Incentive.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }
}
