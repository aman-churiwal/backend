package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AverageSleepingHoursRepository;
import viteezy.domain.quiz.AverageSleepingHours;

import java.util.List;
import java.util.Optional;

public class AverageSleepingHoursRepositoryImpl implements AverageSleepingHoursRepository {

    private static final String SELECT_ALL = "SELECT average_sleeping_hours.* FROM average_sleeping_hours ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO average_sleeping_hours (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AverageSleepingHoursRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AverageSleepingHours>> find(Long id) {
        final HandleCallback<Optional<AverageSleepingHours>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AverageSleepingHours.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AverageSleepingHours>> findAll() {
        final HandleCallback<List<AverageSleepingHours>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AverageSleepingHours.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AverageSleepingHours averageSleepingHours) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(averageSleepingHours)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}