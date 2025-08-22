package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.MenstruationIntervalAnswerRepository;
import viteezy.domain.quiz.MenstruationIntervalAnswer;

import java.util.Optional;
import java.util.UUID;

public class MenstruationIntervalAnswerRepositoryImpl implements MenstruationIntervalAnswerRepository {

    private static final String SELECT_ALL = "SELECT menstruation_interval_answers.* FROM menstruation_interval_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on menstruation_interval_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE menstruation_interval_answers " +
            "SET menstruation_interval_id = :menstruationIntervalId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO menstruation_interval_answers(quiz_id, menstruation_interval_id) " +
            "VALUES(:quizId, :menstruationIntervalId);";

    private final Jdbi jdbi;

    public MenstruationIntervalAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<MenstruationIntervalAnswer>> find(Long id) {
        final HandleCallback<Optional<MenstruationIntervalAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(MenstruationIntervalAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<MenstruationIntervalAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<MenstruationIntervalAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(MenstruationIntervalAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(MenstruationIntervalAnswer menstruationIntervalAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(menstruationIntervalAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(MenstruationIntervalAnswer menstruationIntervalAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(menstruationIntervalAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> menstruationIntervalAnswer.getId());
    }
}