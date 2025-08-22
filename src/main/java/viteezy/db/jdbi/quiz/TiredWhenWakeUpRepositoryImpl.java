package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.TiredWhenWakeUpRepository;
import viteezy.domain.quiz.TiredWhenWakeUp;

import java.util.List;
import java.util.Optional;

public class TiredWhenWakeUpRepositoryImpl implements TiredWhenWakeUpRepository {

    private static final String SELECT_ALL = "SELECT tired_when_wake_ups.* FROM tired_when_wake_ups ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO tired_when_wake_ups (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public TiredWhenWakeUpRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<TiredWhenWakeUp>> find(Long id) {
        final HandleCallback<Optional<TiredWhenWakeUp>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(TiredWhenWakeUp.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<TiredWhenWakeUp>> findAll() {
        final HandleCallback<List<TiredWhenWakeUp>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(TiredWhenWakeUp.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(TiredWhenWakeUp tiredWhenWakeUp) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(tiredWhenWakeUp)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}