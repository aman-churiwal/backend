package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AmountOfDairyConsumptionAnswerRepository;
import viteezy.domain.quiz.AmountOfDairyConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public class AmountOfDairyConsumptionAnswerRepositoryImpl implements AmountOfDairyConsumptionAnswerRepository {

    private static final String SELECT_ALL = "SELECT amount_of_dairy_consumption_answers.* FROM amount_of_dairy_consumption_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on amount_of_dairy_consumption_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE amount_of_dairy_consumption_answers " +
            "SET amount_of_dairy_consumption_id = :amountOfDairyConsumptionId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO amount_of_dairy_consumption_answers(quiz_id, amount_of_dairy_consumption_id) " +
            "VALUES(:quizId, :amountOfDairyConsumptionId);";

    private final Jdbi jdbi;

    public AmountOfDairyConsumptionAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AmountOfDairyConsumptionAnswer>> find(Long id) {
        final HandleCallback<Optional<AmountOfDairyConsumptionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AmountOfDairyConsumptionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<AmountOfDairyConsumptionAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<AmountOfDairyConsumptionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(AmountOfDairyConsumptionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AmountOfDairyConsumptionAnswer amountOfDairyConsumptionAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(amountOfDairyConsumptionAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(AmountOfDairyConsumptionAnswer amountOfDairyConsumptionAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(amountOfDairyConsumptionAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> amountOfDairyConsumptionAnswer.getId());
    }
}