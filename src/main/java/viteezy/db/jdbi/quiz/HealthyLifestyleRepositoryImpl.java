package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.HealthyLifestyleRepository;
import viteezy.domain.quiz.HealthyLifestyle;

import java.util.List;
import java.util.Optional;

public class HealthyLifestyleRepositoryImpl implements HealthyLifestyleRepository {

    private static final String SELECT_ALL = "SELECT healthy_lifestyles.* FROM healthy_lifestyles ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO healthy_lifestyles (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public HealthyLifestyleRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<HealthyLifestyle>> find(Long id) {
        final HandleCallback<Optional<HealthyLifestyle>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(HealthyLifestyle.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<HealthyLifestyle>> findAll() {
        final HandleCallback<List<HealthyLifestyle>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(HealthyLifestyle.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(HealthyLifestyle healthyLifestyle) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(healthyLifestyle)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}