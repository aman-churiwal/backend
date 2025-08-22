package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.LoggingRepository;
import viteezy.domain.Logging;

import java.util.List;

public class LoggingRepositoryImpl implements LoggingRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingRepositoryImpl.class);

    private static final String SELECT_ALL = "" +
            "SELECT * FROM logging ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO logging(customer_id, event, info) " +
            "VALUES (:customerId, :event, :info)";

    private final Jdbi jdbi;
    public LoggingRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<Logging> find(Long id) {
        final HandleCallback<Logging, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Logging.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Logging>> findAll(Long customerId) {
        final HandleCallback<List<Logging>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE customer_id = :customerId ORDER BY creation_timestamp DESC" )
                .bind("customerId", customerId)
                .mapTo(Logging.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Logging> create(Logging logging) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bind("customerId", logging.getCustomerId())
                .bind("event", logging.getEvent())
                .bind("info", logging.getInfo())
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }
}
