package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.UsageGoalRepository;
import viteezy.domain.quiz.UsageGoal;

import java.util.List;
import java.util.Optional;

public class UsageGoalRepositoryImpl implements UsageGoalRepository {

    private static final String SELECT_ALL = "SELECT usage_goals.* FROM usage_goals ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO usage_goals (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public UsageGoalRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<UsageGoal>> find(Long id) {
        final HandleCallback<Optional<UsageGoal>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(UsageGoal.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<UsageGoal>> findAll() {
        final HandleCallback<List<UsageGoal>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(UsageGoal.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(UsageGoal usageGoal) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(usageGoal)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}