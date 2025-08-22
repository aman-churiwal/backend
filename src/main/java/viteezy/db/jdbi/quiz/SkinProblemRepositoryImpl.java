package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.SkinProblemRepository;
import viteezy.domain.quiz.SkinProblem;

import java.util.List;
import java.util.Optional;

public class SkinProblemRepositoryImpl implements SkinProblemRepository {

    private static final String SELECT_ALL = "SELECT skin_problems.* FROM skin_problems ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO skin_problems (name, code, subtitle) " +
            "VALUES (:name, :code, :subtitle)";

    private final Jdbi jdbi;

    public SkinProblemRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<SkinProblem>> find(Long id) {
        final HandleCallback<Optional<SkinProblem>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(SkinProblem.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<SkinProblem>> findAll() {
        final HandleCallback<List<SkinProblem>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(SkinProblem.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(SkinProblem skinProblem) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(skinProblem)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}