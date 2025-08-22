package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.MenstruationSideIssueRepository;
import viteezy.domain.quiz.MenstruationSideIssue;

import java.util.List;
import java.util.Optional;

public class MenstruationSideIssueRepositoryImpl implements MenstruationSideIssueRepository {

    private static final String SELECT_ALL = "SELECT menstruation_side_issues.* FROM menstruation_side_issues ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO menstruation_side_issues (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public MenstruationSideIssueRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<MenstruationSideIssue>> find(Long id) {
        final HandleCallback<Optional<MenstruationSideIssue>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(MenstruationSideIssue.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<MenstruationSideIssue>> findAll() {
        final HandleCallback<List<MenstruationSideIssue>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(MenstruationSideIssue.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(MenstruationSideIssue menstruationSideIssue) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(menstruationSideIssue)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}