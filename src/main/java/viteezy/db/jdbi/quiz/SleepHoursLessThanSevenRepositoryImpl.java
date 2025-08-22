package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.SleepHoursLessThanSevenRepository;
import viteezy.domain.quiz.SleepHoursLessThanSeven;

import java.util.List;
import java.util.Optional;

public class SleepHoursLessThanSevenRepositoryImpl implements SleepHoursLessThanSevenRepository {

    private static final String SELECT_ALL = "SELECT sleep_hours_less_than_sevens.* FROM sleep_hours_less_than_sevens ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO sleep_hours_less_than_sevens (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public SleepHoursLessThanSevenRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<SleepHoursLessThanSeven>> find(Long id) {
        final HandleCallback<Optional<SleepHoursLessThanSeven>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(SleepHoursLessThanSeven.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<SleepHoursLessThanSeven>> findAll() {
        final HandleCallback<List<SleepHoursLessThanSeven>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(SleepHoursLessThanSeven.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(SleepHoursLessThanSeven sleepHoursLessThanSeven) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(sleepHoursLessThanSeven)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}