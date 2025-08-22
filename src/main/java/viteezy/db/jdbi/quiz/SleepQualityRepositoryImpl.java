package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.SleepQualityRepository;
import viteezy.domain.quiz.SleepQuality;

import java.util.List;
import java.util.Optional;

public class SleepQualityRepositoryImpl implements SleepQualityRepository {

    private static final String SELECT_ALL = "SELECT sleep_qualitys.* FROM sleep_qualitys ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO sleep_qualitys (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public SleepQualityRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<SleepQuality>> find(Long id) {
        final HandleCallback<Optional<SleepQuality>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(SleepQuality.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<SleepQuality>> findAll() {
        final HandleCallback<List<SleepQuality>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(SleepQuality.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(SleepQuality sleepQuality) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(sleepQuality)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}