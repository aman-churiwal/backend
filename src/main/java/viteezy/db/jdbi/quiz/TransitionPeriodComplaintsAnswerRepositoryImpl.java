package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.TransitionPeriodComplaintsAnswerRepository;
import viteezy.domain.quiz.TransitionPeriodComplaintsAnswer;

import java.util.Optional;
import java.util.UUID;

public class TransitionPeriodComplaintsAnswerRepositoryImpl implements TransitionPeriodComplaintsAnswerRepository {

    private static final String SELECT_ALL = "SELECT transition_period_complaints_answers.* FROM transition_period_complaints_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on transition_period_complaints_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE transition_period_complaints_answers " +
            "SET transition_period_complaints_id = :transitionPeriodComplaintsId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO transition_period_complaints_answers(quiz_id, transition_period_complaints_id) " +
            "VALUES(:quizId, :transitionPeriodComplaintsId);";

    private final Jdbi jdbi;

    public TransitionPeriodComplaintsAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<TransitionPeriodComplaintsAnswer>> find(Long id) {
        final HandleCallback<Optional<TransitionPeriodComplaintsAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(TransitionPeriodComplaintsAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<TransitionPeriodComplaintsAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<TransitionPeriodComplaintsAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(TransitionPeriodComplaintsAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(TransitionPeriodComplaintsAnswer transitionPeriodComplaintsAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(transitionPeriodComplaintsAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(TransitionPeriodComplaintsAnswer transitionPeriodComplaintsAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(transitionPeriodComplaintsAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> transitionPeriodComplaintsAnswer.getId());
    }
}