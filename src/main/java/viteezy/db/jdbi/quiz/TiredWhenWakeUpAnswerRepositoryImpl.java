package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.TiredWhenWakeUpAnswerRepository;
import viteezy.domain.quiz.TiredWhenWakeUpAnswer;

import java.util.Optional;
import java.util.UUID;

public class TiredWhenWakeUpAnswerRepositoryImpl implements TiredWhenWakeUpAnswerRepository {

    private static final String SELECT_ALL = "SELECT tired_when_wake_up_answers.* FROM tired_when_wake_up_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on tired_when_wake_up_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE tired_when_wake_up_answers " +
            "SET tired_when_wake_up_id = :tiredWhenWakeUpId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO tired_when_wake_up_answers(quiz_id, tired_when_wake_up_id) " +
            "VALUES(:quizId, :tiredWhenWakeUpId);";

    private final Jdbi jdbi;

    public TiredWhenWakeUpAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<TiredWhenWakeUpAnswer>> find(Long id) {
        final HandleCallback<Optional<TiredWhenWakeUpAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(TiredWhenWakeUpAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<TiredWhenWakeUpAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<TiredWhenWakeUpAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(TiredWhenWakeUpAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(TiredWhenWakeUpAnswer tiredWhenWakeUpAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(tiredWhenWakeUpAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(TiredWhenWakeUpAnswer tiredWhenWakeUpAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(tiredWhenWakeUpAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> tiredWhenWakeUpAnswer.getId());
    }
}