package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.CurrentLibidoRepository;
import viteezy.domain.quiz.CurrentLibido;

import java.util.List;
import java.util.Optional;

public class CurrentLibidoRepositoryImpl implements CurrentLibidoRepository {

    private static final String SELECT_ALL = "SELECT current_libidos.* FROM current_libidos ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO current_libidos (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public CurrentLibidoRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<CurrentLibido>> find(Long id) {
        final HandleCallback<Optional<CurrentLibido>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(CurrentLibido.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<CurrentLibido>> findAll() {
        final HandleCallback<List<CurrentLibido>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(CurrentLibido.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(CurrentLibido currentLibido) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(currentLibido)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}