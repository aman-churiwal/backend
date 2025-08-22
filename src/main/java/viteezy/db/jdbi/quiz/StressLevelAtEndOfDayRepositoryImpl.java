package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.StressLevelAtEndOfDayRepository;
import viteezy.domain.quiz.StressLevelAtEndOfDay;

import java.util.List;
import java.util.Optional;

public class StressLevelAtEndOfDayRepositoryImpl implements StressLevelAtEndOfDayRepository {

    private static final String SELECT_ALL = "SELECT stress_level_at_end_of_days.* FROM stress_level_at_end_of_days ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO stress_level_at_end_of_days (name, code, subtitle) " +
            "VALUES (:name, :code, :subtitle)";

    private final Jdbi jdbi;

    public StressLevelAtEndOfDayRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<StressLevelAtEndOfDay>> find(Long id) {
        final HandleCallback<Optional<StressLevelAtEndOfDay>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(StressLevelAtEndOfDay.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<StressLevelAtEndOfDay>> findAll() {
        final HandleCallback<List<StressLevelAtEndOfDay>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(StressLevelAtEndOfDay.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(StressLevelAtEndOfDay stressLevelAtEndOfDay) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(stressLevelAtEndOfDay)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}