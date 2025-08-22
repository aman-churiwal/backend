package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.PrimaryGoalAnswerRepository;
import viteezy.domain.quiz.PrimaryGoalAnswer;

import java.util.Optional;
import java.util.UUID;

public class PrimaryGoalAnswerRepositoryImpl implements PrimaryGoalAnswerRepository {

    private static final String SELECT_ALL = "SELECT primary_goal_answers.* FROM primary_goal_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on primary_goal_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE primary_goal_answers " +
            "SET primary_goal_id = :primaryGoalId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO primary_goal_answers(quiz_id, primary_goal_id) " +
            "VALUES(:quizId, :primaryGoalId);";

    private final Jdbi jdbi;

    public PrimaryGoalAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<PrimaryGoalAnswer>> find(Long id) {
        final HandleCallback<Optional<PrimaryGoalAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(PrimaryGoalAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<PrimaryGoalAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<PrimaryGoalAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(PrimaryGoalAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(PrimaryGoalAnswer primaryGoalAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(primaryGoalAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(PrimaryGoalAnswer primaryGoalAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(primaryGoalAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> primaryGoalAnswer.getId());
    }
}