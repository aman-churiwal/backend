package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.HelpGoalRepository;
import viteezy.domain.quiz.HelpGoal;

import java.util.List;
import java.util.Optional;

public class HelpGoalRepositoryImpl implements HelpGoalRepository {

    private static final String SELECT_ALL = "SELECT help_goals.* FROM help_goals ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO help_goals (name, code, subtitle) " +
            "VALUES (:name, :code, :subtitle)";

    private final Jdbi jdbi;

    public HelpGoalRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<HelpGoal>> find(Long id) {
        final HandleCallback<Optional<HelpGoal>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(HelpGoal.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<HelpGoal>> findAll() {
        final HandleCallback<List<HelpGoal>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(HelpGoal.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(HelpGoal helpGoal) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(helpGoal)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}