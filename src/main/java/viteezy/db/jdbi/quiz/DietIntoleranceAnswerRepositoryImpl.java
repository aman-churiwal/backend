package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DietIntoleranceAnswerRepository;
import viteezy.domain.quiz.DietIntoleranceAnswer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DietIntoleranceAnswerRepositoryImpl implements DietIntoleranceAnswerRepository {

    private static final String SELECT_ALL = "SELECT diet_intolerance_answers.* FROM diet_intolerance_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on diet_intolerance_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String SELECT_BY_QUIZ_EXT_REF_AND_DIET_INTOLERANCE_ID = SELECT_ALL +
            " JOIN quiz q on diet_intolerance_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference AND diet_intolerance_answers.diet_intolerance_id = :dietIntoleranceId";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE diet_intolerance_answers " +
            "SET diet_intolerance_id = :dietIntoleranceId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO diet_intolerance_answers(quiz_id, diet_intolerance_id) " +
            "VALUES(:quizId, :dietIntoleranceId);";

    private static final String DELETE_BY_ID = "" +
            "DELETE FROM diet_intolerance_answers " +
            "WHERE id = :id";

    private final Jdbi jdbi;

    public DietIntoleranceAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DietIntoleranceAnswer>> find(Long id) {
        final HandleCallback<Optional<DietIntoleranceAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DietIntoleranceAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<DietIntoleranceAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<List<DietIntoleranceAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(DietIntoleranceAnswer.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<DietIntoleranceAnswer>> find(UUID quizExternalReference, Long dietIntoleranceId) {
        final HandleCallback<Optional<DietIntoleranceAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF_AND_DIET_INTOLERANCE_ID)
                .bind("quizExternalReference", quizExternalReference)
                .bind("dietIntoleranceId", dietIntoleranceId)
                .mapTo(DietIntoleranceAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DietIntoleranceAnswer dietIntoleranceAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(dietIntoleranceAnswer)
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