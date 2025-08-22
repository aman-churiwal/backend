package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.TroubleFallingAsleepRepository;
import viteezy.domain.quiz.TroubleFallingAsleep;

import java.util.List;
import java.util.Optional;

public class TroubleFallingAsleepRepositoryImpl implements TroubleFallingAsleepRepository {

    private static final String SELECT_ALL = "SELECT trouble_falling_asleeps.* FROM trouble_falling_asleeps ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO trouble_falling_asleeps (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public TroubleFallingAsleepRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<TroubleFallingAsleep>> find(Long id) {
        final HandleCallback<Optional<TroubleFallingAsleep>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(TroubleFallingAsleep.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<TroubleFallingAsleep>> findAll() {
        final HandleCallback<List<TroubleFallingAsleep>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(TroubleFallingAsleep.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(TroubleFallingAsleep troubleFallingAsleep) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(troubleFallingAsleep)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}