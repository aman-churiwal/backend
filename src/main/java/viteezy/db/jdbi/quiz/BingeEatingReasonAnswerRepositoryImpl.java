package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.BingeEatingReasonAnswerRepository;
import viteezy.domain.quiz.BingeEatingReasonAnswer;

import java.util.Optional;
import java.util.UUID;

public class BingeEatingReasonAnswerRepositoryImpl implements BingeEatingReasonAnswerRepository {

    private static final String SELECT_ALL = "SELECT binge_eating_reason_answers.* FROM binge_eating_reason_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on binge_eating_reason_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE binge_eating_reason_answers " +
            "SET binge_eating_reason_id = :bingeEatingReasonId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO binge_eating_reason_answers(quiz_id, binge_eating_reason_id) " +
            "VALUES(:quizId, :bingeEatingReasonId);";

    private final Jdbi jdbi;

    public BingeEatingReasonAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<BingeEatingReasonAnswer>> find(Long id) {
        final HandleCallback<Optional<BingeEatingReasonAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(BingeEatingReasonAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<BingeEatingReasonAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<BingeEatingReasonAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(BingeEatingReasonAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(BingeEatingReasonAnswer bingeEatingReasonAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(bingeEatingReasonAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(BingeEatingReasonAnswer bingeEatingReasonAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(bingeEatingReasonAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> bingeEatingReasonAnswer.getId());
    }
}