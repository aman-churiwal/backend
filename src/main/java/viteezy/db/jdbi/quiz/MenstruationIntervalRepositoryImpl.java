package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.MenstruationIntervalRepository;
import viteezy.domain.quiz.MenstruationInterval;

import java.util.List;
import java.util.Optional;

public class MenstruationIntervalRepositoryImpl implements MenstruationIntervalRepository {

    private static final String SELECT_ALL = "SELECT menstruation_interval.* FROM menstruation_interval ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO menstruation_interval (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public MenstruationIntervalRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<MenstruationInterval>> find(Long id) {
        final HandleCallback<Optional<MenstruationInterval>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(MenstruationInterval.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<MenstruationInterval>> findAll() {
        final HandleCallback<List<MenstruationInterval>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(MenstruationInterval.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(MenstruationInterval menstruationInterval) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(menstruationInterval)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}