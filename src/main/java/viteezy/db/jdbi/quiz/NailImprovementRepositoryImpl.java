package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.NailImprovementRepository;
import viteezy.domain.quiz.NailImprovement;

import java.util.List;
import java.util.Optional;

public class NailImprovementRepositoryImpl implements NailImprovementRepository {

    private static final String SELECT_ALL = "SELECT nail_improvements.* FROM nail_improvements ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO nail_improvements (name, code, subtitle) " +
            "VALUES (:name, :code, :subtitle)";

    private final Jdbi jdbi;

    public NailImprovementRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<NailImprovement>> find(Long id) {
        final HandleCallback<Optional<NailImprovement>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(NailImprovement.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<NailImprovement>> findAll() {
        final HandleCallback<List<NailImprovement>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(NailImprovement.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(NailImprovement nailImprovement) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(nailImprovement)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}