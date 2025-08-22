package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DietIntoleranceRepository;
import viteezy.domain.quiz.DietIntolerance;

import java.util.List;
import java.util.Optional;

public class DietIntoleranceRepositoryImpl implements DietIntoleranceRepository {

    private static final String SELECT_ALL = "SELECT diet_intolerances.* FROM diet_intolerances ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO diet_intolerances (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public DietIntoleranceRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DietIntolerance>> find(Long id) {
        final HandleCallback<Optional<DietIntolerance>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DietIntolerance.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<DietIntolerance>> findAll() {
        final HandleCallback<List<DietIntolerance>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(DietIntolerance.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DietIntolerance dietIntolerance) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(dietIntolerance)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}