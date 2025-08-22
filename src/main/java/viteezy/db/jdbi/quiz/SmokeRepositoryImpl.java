package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.SmokeRepository;
import viteezy.domain.quiz.Smoke;

import java.util.List;
import java.util.Optional;

public class SmokeRepositoryImpl implements SmokeRepository {

    private static final String SELECT_ALL = "SELECT smokes.* FROM smokes ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO smokes (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public SmokeRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<Smoke>> find(Long id) {
        final HandleCallback<Optional<Smoke>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Smoke.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Smoke>> findAll() {
        final HandleCallback<List<Smoke>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(Smoke.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(Smoke smoke) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(smoke)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}