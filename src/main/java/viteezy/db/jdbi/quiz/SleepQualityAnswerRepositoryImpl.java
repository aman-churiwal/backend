package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.SleepQualityAnswerRepository;
import viteezy.domain.quiz.SleepQualityAnswer;

import java.util.Optional;
import java.util.UUID;

public class SleepQualityAnswerRepositoryImpl implements SleepQualityAnswerRepository {

    private static final String SELECT_ALL = "SELECT sleep_quality_answers.* FROM sleep_quality_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on sleep_quality_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE sleep_quality_answers " +
            "SET sleep_quality_id = :sleepQualityId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO sleep_quality_answers(quiz_id, sleep_quality_id) " +
            "VALUES(:quizId, :sleepQualityId);";

    private final Jdbi jdbi;

    public SleepQualityAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<SleepQualityAnswer>> find(Long id) {
        final HandleCallback<Optional<SleepQualityAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(SleepQualityAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<SleepQualityAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<SleepQualityAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(SleepQualityAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(SleepQualityAnswer sleepQualityAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(sleepQualityAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(SleepQualityAnswer sleepQualityAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(sleepQualityAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> sleepQualityAnswer.getId());
    }
}