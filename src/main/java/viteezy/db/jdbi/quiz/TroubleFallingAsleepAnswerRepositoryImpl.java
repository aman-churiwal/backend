package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.TroubleFallingAsleepAnswerRepository;
import viteezy.domain.quiz.TroubleFallingAsleepAnswer;

import java.util.Optional;
import java.util.UUID;

public class TroubleFallingAsleepAnswerRepositoryImpl implements TroubleFallingAsleepAnswerRepository {

    private static final String SELECT_ALL = "SELECT trouble_falling_asleep_answers.* FROM trouble_falling_asleep_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on trouble_falling_asleep_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE trouble_falling_asleep_answers " +
            "SET trouble_falling_asleep_id = :troubleFallingAsleepId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO trouble_falling_asleep_answers(quiz_id, trouble_falling_asleep_id) " +
            "VALUES(:quizId, :troubleFallingAsleepId);";

    private final Jdbi jdbi;

    public TroubleFallingAsleepAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<TroubleFallingAsleepAnswer>> find(Long id) {
        final HandleCallback<Optional<TroubleFallingAsleepAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(TroubleFallingAsleepAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<TroubleFallingAsleepAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<TroubleFallingAsleepAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(TroubleFallingAsleepAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(TroubleFallingAsleepAnswer troubleFallingAsleepAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(troubleFallingAsleepAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(TroubleFallingAsleepAnswer troubleFallingAsleepAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(troubleFallingAsleepAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> troubleFallingAsleepAnswer.getId());
    }
}