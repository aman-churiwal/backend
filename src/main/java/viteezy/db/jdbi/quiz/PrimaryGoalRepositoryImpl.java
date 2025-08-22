package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.PrimaryGoalRepository;
import viteezy.domain.quiz.PrimaryGoal;

import java.util.List;
import java.util.Optional;

public class PrimaryGoalRepositoryImpl implements PrimaryGoalRepository {

    private static final String SELECT_ALL = "SELECT primary_goals.* FROM primary_goals ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO primary_goals (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public PrimaryGoalRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<PrimaryGoal>> find(Long id) {
        final HandleCallback<Optional<PrimaryGoal>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(PrimaryGoal.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<PrimaryGoal>> findAll() {
        final HandleCallback<List<PrimaryGoal>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(PrimaryGoal.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(PrimaryGoal primaryGoal) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(primaryGoal)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}