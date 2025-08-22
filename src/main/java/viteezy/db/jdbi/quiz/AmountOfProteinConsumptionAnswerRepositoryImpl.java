package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AmountOfProteinConsumptionAnswerRepository;
import viteezy.domain.quiz.AmountOfProteinConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public class AmountOfProteinConsumptionAnswerRepositoryImpl implements AmountOfProteinConsumptionAnswerRepository {

    private static final String SELECT_ALL = "SELECT amount_of_protein_consumption_answers.* FROM amount_of_protein_consumption_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on amount_of_protein_consumption_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE amount_of_protein_consumption_answers " +
            "SET amount_of_protein_consumption_id = :amountOfProteinConsumptionId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO amount_of_protein_consumption_answers(quiz_id, amount_of_protein_consumption_id) " +
            "VALUES(:quizId, :amountOfProteinConsumptionId);";

    private final Jdbi jdbi;

    public AmountOfProteinConsumptionAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AmountOfProteinConsumptionAnswer>> find(Long id) {
        final HandleCallback<Optional<AmountOfProteinConsumptionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AmountOfProteinConsumptionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<AmountOfProteinConsumptionAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<AmountOfProteinConsumptionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(AmountOfProteinConsumptionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AmountOfProteinConsumptionAnswer amountOfProteinConsumptionAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(amountOfProteinConsumptionAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(AmountOfProteinConsumptionAnswer amountOfProteinConsumptionAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(amountOfProteinConsumptionAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> amountOfProteinConsumptionAnswer.getId());
    }
}