package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DigestionAmountAnswerRepository;
import viteezy.domain.quiz.DigestionAmountAnswer;

import java.util.Optional;
import java.util.UUID;

public class DigestionAmountAnswerRepositoryImpl implements DigestionAmountAnswerRepository {

    private static final String SELECT_ALL = "SELECT digestion_amount_answers.* FROM digestion_amount_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on digestion_amount_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE digestion_amount_answers " +
            "SET digestion_amount_id = :digestionAmountId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO digestion_amount_answers(quiz_id, digestion_amount_id) " +
            "VALUES(:quizId, :digestionAmountId);";

    private final Jdbi jdbi;

    public DigestionAmountAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DigestionAmountAnswer>> find(Long id) {
        final HandleCallback<Optional<DigestionAmountAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DigestionAmountAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<DigestionAmountAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<DigestionAmountAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(DigestionAmountAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DigestionAmountAnswer digestionAmountAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(digestionAmountAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(DigestionAmountAnswer digestionAmountAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(digestionAmountAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> digestionAmountAnswer.getId());
    }
}