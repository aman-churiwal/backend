package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.StressLevelConditionRepository;
import viteezy.domain.quiz.StressLevelCondition;

import java.util.List;
import java.util.Optional;

public class StressLevelConditionRepositoryImpl implements StressLevelConditionRepository {

    private static final String SELECT_ALL = "SELECT stress_level_conditions.* FROM stress_level_conditions ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO stress_level_conditions (name, code, subtitle) " +
            "VALUES (:name, :code, :subtitle)";

    private final Jdbi jdbi;

    public StressLevelConditionRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<StressLevelCondition>> find(Long id) {
        final HandleCallback<Optional<StressLevelCondition>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(StressLevelCondition.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<StressLevelCondition>> findAll() {
        final HandleCallback<List<StressLevelCondition>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(StressLevelCondition.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(StressLevelCondition stressLevelCondition) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(stressLevelCondition)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}