package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.StressLevelConditionAnswerRepository;
import viteezy.domain.quiz.StressLevelConditionAnswer;

import java.util.Optional;
import java.util.UUID;

public class StressLevelConditionAnswerRepositoryImpl implements StressLevelConditionAnswerRepository {

    private static final String SELECT_ALL = "SELECT stress_level_condition_answers.* FROM stress_level_condition_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on stress_level_condition_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE stress_level_condition_answers " +
            "SET stress_level_condition_id = :stressLevelConditionId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO stress_level_condition_answers(quiz_id, stress_level_condition_id) " +
            "VALUES(:quizId, :stressLevelConditionId);";

    private final Jdbi jdbi;

    public StressLevelConditionAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<StressLevelConditionAnswer>> find(Long id) {
        final HandleCallback<Optional<StressLevelConditionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(StressLevelConditionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<StressLevelConditionAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<StressLevelConditionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(StressLevelConditionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(StressLevelConditionAnswer stressLevelConditionAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(stressLevelConditionAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(StressLevelConditionAnswer stressLevelConditionAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(stressLevelConditionAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> stressLevelConditionAnswer.getId());
    }
}