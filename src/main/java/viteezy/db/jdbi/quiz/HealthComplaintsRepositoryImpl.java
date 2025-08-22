package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.HealthComplaintsRepository;
import viteezy.domain.quiz.HealthComplaints;

import java.util.List;
import java.util.Optional;

public class HealthComplaintsRepositoryImpl implements HealthComplaintsRepository {

    private static final String SELECT_ALL = "SELECT health_complaints.* FROM health_complaints ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO health_complaints (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public HealthComplaintsRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<HealthComplaints>> find(Long id) {
        final HandleCallback<Optional<HealthComplaints>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(HealthComplaints.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<HealthComplaints>> findAll() {
        final HandleCallback<List<HealthComplaints>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(HealthComplaints.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(HealthComplaints healthComplaints) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(healthComplaints)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}