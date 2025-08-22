package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.SleepHoursLessThanSevenAnswerRepository;
import viteezy.domain.quiz.SleepHoursLessThanSevenAnswer;

import java.util.Optional;
import java.util.UUID;

public class SleepHoursLessThanSevenAnswerRepositoryImpl implements SleepHoursLessThanSevenAnswerRepository {

    private static final String SELECT_ALL = "SELECT sleep_hours_less_than_seven_answers.* FROM sleep_hours_less_than_seven_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on sleep_hours_less_than_seven_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE sleep_hours_less_than_seven_answers " +
            "SET sleep_hours_less_than_seven_id = :sleepHoursLessThanSevenId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO sleep_hours_less_than_seven_answers(quiz_id, sleep_hours_less_than_seven_id) " +
            "VALUES(:quizId, :sleepHoursLessThanSevenId);";

    private final Jdbi jdbi;

    public SleepHoursLessThanSevenAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<SleepHoursLessThanSevenAnswer>> find(Long id) {
        final HandleCallback<Optional<SleepHoursLessThanSevenAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(SleepHoursLessThanSevenAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<SleepHoursLessThanSevenAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<SleepHoursLessThanSevenAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(SleepHoursLessThanSevenAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(SleepHoursLessThanSevenAnswer sleepHoursLessThanSevenAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(sleepHoursLessThanSevenAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(SleepHoursLessThanSevenAnswer sleepHoursLessThanSevenAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(sleepHoursLessThanSevenAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> sleepHoursLessThanSevenAnswer.getId());
    }
}