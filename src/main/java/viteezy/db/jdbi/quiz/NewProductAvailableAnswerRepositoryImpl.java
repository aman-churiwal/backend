package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.NewProductAvailableAnswerRepository;
import viteezy.domain.quiz.NewProductAvailableAnswer;

import java.util.Optional;
import java.util.UUID;

public class NewProductAvailableAnswerRepositoryImpl implements NewProductAvailableAnswerRepository {

    private static final String SELECT_ALL = "SELECT new_product_available_answers.* FROM new_product_available_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on new_product_available_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE new_product_available_answers " +
            "SET new_product_available_id = :newProductAvailableId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO new_product_available_answers(quiz_id, new_product_available_id) " +
            "VALUES(:quizId, :newProductAvailableId);";

    private final Jdbi jdbi;

    public NewProductAvailableAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<NewProductAvailableAnswer>> find(Long id) {
        final HandleCallback<Optional<NewProductAvailableAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(NewProductAvailableAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<NewProductAvailableAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<NewProductAvailableAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(NewProductAvailableAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(NewProductAvailableAnswer newProductAvailableAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(newProductAvailableAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(NewProductAvailableAnswer newProductAvailableAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(newProductAvailableAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> newProductAvailableAnswer.getId());
    }
}