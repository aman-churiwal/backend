package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.TransitionPeriodComplaintsRepository;
import viteezy.domain.quiz.TransitionPeriodComplaints;

import java.util.List;
import java.util.Optional;

public class TransitionPeriodComplaintsRepositoryImpl implements TransitionPeriodComplaintsRepository {

    private static final String SELECT_ALL = "SELECT transition_period_complaints.* FROM transition_period_complaints ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO transition_period_complaints (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public TransitionPeriodComplaintsRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<TransitionPeriodComplaints>> find(Long id) {
        final HandleCallback<Optional<TransitionPeriodComplaints>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(TransitionPeriodComplaints.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<TransitionPeriodComplaints>> findAll() {
        final HandleCallback<List<TransitionPeriodComplaints>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(TransitionPeriodComplaints.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(TransitionPeriodComplaints transitionPeriodComplaints) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(transitionPeriodComplaints)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}