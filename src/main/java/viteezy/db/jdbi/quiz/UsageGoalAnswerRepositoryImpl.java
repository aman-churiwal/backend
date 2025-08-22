package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.UsageGoalAnswerRepository;
import viteezy.domain.quiz.UsageGoalAnswer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsageGoalAnswerRepositoryImpl implements UsageGoalAnswerRepository {

    private static final String SELECT_ALL = "SELECT usage_goal_answers.* FROM usage_goal_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on usage_goal_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";
    private static final String SELECT_BY_QUIZ_EXT_REF_AND_USAGE_GOAL_ID = SELECT_ALL +
            " JOIN quiz q on usage_goal_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference AND usage_goal_id = :usageGoalId";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE usage_goal_answers " +
            "SET usage_goal_id = :usageGoalId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO usage_goal_answers(quiz_id, usage_goal_id) " +
            "VALUES(:quizId, :usageGoalId);";
    private static final String DELETE_BY_ID = "" +
            "DELETE FROM usage_goal_answers " +
            "WHERE id = :id";

    private final Jdbi jdbi;

    public UsageGoalAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<UsageGoalAnswer>> find(Long id) {
        final HandleCallback<Optional<UsageGoalAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(UsageGoalAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<UsageGoalAnswer>> find(UUID quizExternalReference, Long usageGoalId) {
        final HandleCallback<Optional<UsageGoalAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF_AND_USAGE_GOAL_ID)
                .bind("quizExternalReference", quizExternalReference)
                .bind("usageGoalId", usageGoalId)
                .mapTo(UsageGoalAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<UsageGoalAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<List<UsageGoalAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(UsageGoalAnswer.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(UsageGoalAnswer usageGoalAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(usageGoalAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Void> delete(Long id) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(DELETE_BY_ID)
                .bind("id", id)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> Try.success(null));
    }
}