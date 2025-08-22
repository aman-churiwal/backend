package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AmountOfFruitConsumptionAnswerRepository;
import viteezy.domain.quiz.AmountOfFruitConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public class AmountOfFruitConsumptionAnswerRepositoryImpl implements AmountOfFruitConsumptionAnswerRepository {

    private static final String SELECT_ALL = "SELECT amount_of_fruit_consumption_answers.* FROM amount_of_fruit_consumption_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on amount_of_fruit_consumption_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE amount_of_fruit_consumption_answers " +
            "SET amount_of_fruit_consumption_id = :amountOfFruitConsumptionId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO amount_of_fruit_consumption_answers(quiz_id, amount_of_fruit_consumption_id) " +
            "VALUES(:quizId, :amountOfFruitConsumptionId);";

    private final Jdbi jdbi;

    public AmountOfFruitConsumptionAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AmountOfFruitConsumptionAnswer>> find(Long id) {
        final HandleCallback<Optional<AmountOfFruitConsumptionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AmountOfFruitConsumptionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<AmountOfFruitConsumptionAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<AmountOfFruitConsumptionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(AmountOfFruitConsumptionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AmountOfFruitConsumptionAnswer amountOfFruitConsumptionAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(amountOfFruitConsumptionAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(AmountOfFruitConsumptionAnswer amountOfFruitConsumptionAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(amountOfFruitConsumptionAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> amountOfFruitConsumptionAnswer.getId());
    }
}