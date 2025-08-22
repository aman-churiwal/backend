package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.ThirtyMinutesOfSunRepository;
import viteezy.domain.quiz.ThirtyMinutesOfSun;

import java.util.List;
import java.util.Optional;

public class ThirtyMinutesOfSunRepositoryImpl implements ThirtyMinutesOfSunRepository {

    private static final String SELECT_ALL = "SELECT thirty_minutes_of_suns.* FROM thirty_minutes_of_suns ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO thirty_minutes_of_suns (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public ThirtyMinutesOfSunRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<ThirtyMinutesOfSun>> find(Long id) {
        final HandleCallback<Optional<ThirtyMinutesOfSun>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(ThirtyMinutesOfSun.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<ThirtyMinutesOfSun>> findAll() {
        final HandleCallback<List<ThirtyMinutesOfSun>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(ThirtyMinutesOfSun.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(ThirtyMinutesOfSun thirtyMinutesOfSun) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(thirtyMinutesOfSun)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}