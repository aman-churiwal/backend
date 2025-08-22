package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.PregnancyStateRepository;
import viteezy.domain.quiz.PregnancyState;

import java.util.List;
import java.util.Optional;

public class PregnancyStateRepositoryImpl implements PregnancyStateRepository {

    private static final String SELECT_ALL = "SELECT pregnancy_states.* FROM pregnancy_states ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO pregnancy_states (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public PregnancyStateRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<PregnancyState>> find(Long id) {
        final HandleCallback<Optional<PregnancyState>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(PregnancyState.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<PregnancyState>> findAll() {
        final HandleCallback<List<PregnancyState>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(PregnancyState.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(PregnancyState pregnancyState) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(pregnancyState)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}