package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.LibidoStressLevelRepository;
import viteezy.domain.quiz.LibidoStressLevel;

import java.util.List;
import java.util.Optional;

public class LibidoStressLevelRepositoryImpl implements LibidoStressLevelRepository {

    private static final String SELECT_ALL = "SELECT libido_stress_levels.* FROM libido_stress_levels ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO libido_stress_levels (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public LibidoStressLevelRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<LibidoStressLevel>> find(Long id) {
        final HandleCallback<Optional<LibidoStressLevel>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(LibidoStressLevel.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<LibidoStressLevel>> findAll() {
        final HandleCallback<List<LibidoStressLevel>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(LibidoStressLevel.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(LibidoStressLevel libidoStressLevel) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(libidoStressLevel)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}