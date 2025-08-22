package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AttentionFocusRepository;
import viteezy.domain.quiz.AttentionFocus;

import java.util.List;
import java.util.Optional;

public class AttentionFocusRepositoryImpl implements AttentionFocusRepository {

    private static final String SELECT_ALL = "SELECT attention_focus.* FROM attention_focus ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO attention_focus (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AttentionFocusRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AttentionFocus>> find(Long id) {
        final HandleCallback<Optional<AttentionFocus>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AttentionFocus.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AttentionFocus>> findAll() {
        final HandleCallback<List<AttentionFocus>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AttentionFocus.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AttentionFocus attentionFocus) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(attentionFocus)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}