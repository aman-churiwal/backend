package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AttentionStateRepository;
import viteezy.domain.quiz.AttentionState;

import java.util.List;
import java.util.Optional;

public class AttentionStateRepositoryImpl implements AttentionStateRepository {

    private static final String SELECT_ALL = "SELECT attention_states.* FROM attention_states ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO attention_states (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AttentionStateRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AttentionState>> find(Long id) {
        final HandleCallback<Optional<AttentionState>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AttentionState.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AttentionState>> findAll() {
        final HandleCallback<List<AttentionState>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AttentionState.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AttentionState attentionState) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(attentionState)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}