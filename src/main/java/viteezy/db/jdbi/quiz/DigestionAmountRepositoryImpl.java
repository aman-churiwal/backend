package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DigestionAmountRepository;
import viteezy.domain.quiz.DigestionAmount;

import java.util.List;
import java.util.Optional;

public class DigestionAmountRepositoryImpl implements DigestionAmountRepository {

    private static final String SELECT_ALL = "SELECT digestion_amounts.* FROM digestion_amounts ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO digestion_amounts (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public DigestionAmountRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DigestionAmount>> find(Long id) {
        final HandleCallback<Optional<DigestionAmount>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DigestionAmount.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<DigestionAmount>> findAll() {
        final HandleCallback<List<DigestionAmount>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(DigestionAmount.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DigestionAmount digestionAmount) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(digestionAmount)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}