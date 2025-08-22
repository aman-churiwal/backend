package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AttentionFocusAnswerRepository;
import viteezy.domain.quiz.AttentionFocusAnswer;

import java.util.Optional;
import java.util.UUID;

public class AttentionFocusAnswerRepositoryImpl implements AttentionFocusAnswerRepository {

    private static final String SELECT_ALL = "SELECT attention_focus_answers.* FROM attention_focus_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on attention_focus_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE attention_focus_answers " +
            "SET attention_focus_id = :attentionFocusId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO attention_focus_answers(quiz_id, attention_focus_id) " +
            "VALUES(:quizId, :attentionFocusId);";

    private final Jdbi jdbi;

    public AttentionFocusAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AttentionFocusAnswer>> find(Long id) {
        final HandleCallback<Optional<AttentionFocusAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AttentionFocusAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<AttentionFocusAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<AttentionFocusAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(AttentionFocusAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AttentionFocusAnswer attentionFocusAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(attentionFocusAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(AttentionFocusAnswer attentionFocusAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(attentionFocusAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> attentionFocusAnswer.getId());
    }
}