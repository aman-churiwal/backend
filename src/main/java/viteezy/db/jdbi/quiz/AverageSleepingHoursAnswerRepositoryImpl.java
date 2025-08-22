package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AverageSleepingHoursAnswerRepository;
import viteezy.domain.quiz.AverageSleepingHoursAnswer;

import java.util.Optional;
import java.util.UUID;

public class AverageSleepingHoursAnswerRepositoryImpl implements AverageSleepingHoursAnswerRepository {

    private static final String SELECT_ALL = "SELECT average_sleeping_hours_answers.* FROM average_sleeping_hours_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on average_sleeping_hours_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE average_sleeping_hours_answers " +
            "SET average_sleeping_hours_id = :averageSleepingHoursId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO average_sleeping_hours_answers(quiz_id, average_sleeping_hours_id) " +
            "VALUES(:quizId, :averageSleepingHoursId);";

    private final Jdbi jdbi;

    public AverageSleepingHoursAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AverageSleepingHoursAnswer>> find(Long id) {
        final HandleCallback<Optional<AverageSleepingHoursAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AverageSleepingHoursAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<AverageSleepingHoursAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<AverageSleepingHoursAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(AverageSleepingHoursAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AverageSleepingHoursAnswer averageSleepingHoursAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(averageSleepingHoursAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(AverageSleepingHoursAnswer averageSleepingHoursAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(averageSleepingHoursAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> averageSleepingHoursAnswer.getId());
    }
}