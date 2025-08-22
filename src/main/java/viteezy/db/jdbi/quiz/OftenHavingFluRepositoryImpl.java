package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.OftenHavingFluRepository;
import viteezy.domain.quiz.OftenHavingFlu;

import java.util.List;
import java.util.Optional;

public class OftenHavingFluRepositoryImpl implements OftenHavingFluRepository {

    private static final String SELECT_ALL = "SELECT often_having_flus.* FROM often_having_flus ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO often_having_flus (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public OftenHavingFluRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<OftenHavingFlu>> find(Long id) {
        final HandleCallback<Optional<OftenHavingFlu>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(OftenHavingFlu.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<OftenHavingFlu>> findAll() {
        final HandleCallback<List<OftenHavingFlu>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(OftenHavingFlu.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(OftenHavingFlu oftenHavingFlu) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(oftenHavingFlu)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}