package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.MenstruationMoodAnswerRepository;
import viteezy.domain.quiz.MenstruationMoodAnswer;

import java.util.Optional;
import java.util.UUID;

public class MenstruationMoodAnswerRepositoryImpl implements MenstruationMoodAnswerRepository {

    private static final String SELECT_ALL = "SELECT menstruation_mood_answers.* FROM menstruation_mood_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on menstruation_mood_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE menstruation_mood_answers " +
            "SET menstruation_mood_id = :menstruationMoodId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO menstruation_mood_answers(quiz_id, menstruation_mood_id) " +
            "VALUES(:quizId, :menstruationMoodId);";

    private final Jdbi jdbi;

    public MenstruationMoodAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<MenstruationMoodAnswer>> find(Long id) {
        final HandleCallback<Optional<MenstruationMoodAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(MenstruationMoodAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<MenstruationMoodAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<MenstruationMoodAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(MenstruationMoodAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(MenstruationMoodAnswer menstruationMoodAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(menstruationMoodAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(MenstruationMoodAnswer menstruationMoodAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(menstruationMoodAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> menstruationMoodAnswer.getId());
    }
}