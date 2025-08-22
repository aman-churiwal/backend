package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.LoseWeightChallengeAnswerRepository;
import viteezy.domain.quiz.LoseWeightChallengeAnswer;

import java.util.Optional;
import java.util.UUID;

public class LoseWeightChallengeAnswerRepositoryImpl implements LoseWeightChallengeAnswerRepository {

    private static final String SELECT_ALL = "SELECT lose_weight_challenge_answers.* FROM lose_weight_challenge_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on lose_weight_challenge_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE lose_weight_challenge_answers " +
            "SET lose_weight_challenge_id = :loseWeightChallengeId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO lose_weight_challenge_answers(quiz_id, lose_weight_challenge_id) " +
            "VALUES(:quizId, :loseWeightChallengeId);";

    private final Jdbi jdbi;

    public LoseWeightChallengeAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<LoseWeightChallengeAnswer>> find(Long id) {
        final HandleCallback<Optional<LoseWeightChallengeAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(LoseWeightChallengeAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<LoseWeightChallengeAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<LoseWeightChallengeAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(LoseWeightChallengeAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(LoseWeightChallengeAnswer loseWeightChallengeAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(loseWeightChallengeAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(LoseWeightChallengeAnswer loseWeightChallengeAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(loseWeightChallengeAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> loseWeightChallengeAnswer.getId());
    }
}