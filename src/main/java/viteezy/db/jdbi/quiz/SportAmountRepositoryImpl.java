package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.SportAmountRepository;
import viteezy.domain.quiz.SportAmount;

import java.util.List;
import java.util.Optional;

public class SportAmountRepositoryImpl implements SportAmountRepository {

    private static final String SELECT_ALL = "SELECT sport_amounts.* FROM sport_amounts ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO sport_amounts (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public SportAmountRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<SportAmount>> find(Long id) {
        final HandleCallback<Optional<SportAmount>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(SportAmount.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<SportAmount>> findAll() {
        final HandleCallback<List<SportAmount>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(SportAmount.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(SportAmount sportAmount) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(sportAmount)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}