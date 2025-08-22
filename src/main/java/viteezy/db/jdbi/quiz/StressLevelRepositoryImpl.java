package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.StressLevelRepository;
import viteezy.domain.quiz.StressLevel;

import java.util.List;
import java.util.Optional;

public class StressLevelRepositoryImpl implements StressLevelRepository {

    private static final String SELECT_ALL = "SELECT stress_levels.* FROM stress_levels ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO stress_levels (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public StressLevelRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<StressLevel>> find(Long id) {
        final HandleCallback<Optional<StressLevel>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(StressLevel.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<StressLevel>> findAll() {
        final HandleCallback<List<StressLevel>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(StressLevel.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(StressLevel stressLevel) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(stressLevel)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}