package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.TypeOfTrainingAnswerRepository;
import viteezy.domain.quiz.TypeOfTrainingAnswer;

import java.util.Optional;
import java.util.UUID;

public class TypeOfTrainingAnswerRepositoryImpl implements TypeOfTrainingAnswerRepository {

    private static final String SELECT_ALL = "SELECT type_of_training_answers.* FROM type_of_training_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on type_of_training_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE type_of_training_answers " +
            "SET type_of_training_id = :typeOfTrainingId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO type_of_training_answers(quiz_id, type_of_training_id) " +
            "VALUES(:quizId, :typeOfTrainingId);";

    private final Jdbi jdbi;

    public TypeOfTrainingAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<TypeOfTrainingAnswer>> find(Long id) {
        final HandleCallback<Optional<TypeOfTrainingAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(TypeOfTrainingAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<TypeOfTrainingAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<TypeOfTrainingAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(TypeOfTrainingAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(TypeOfTrainingAnswer typeOfTrainingAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(typeOfTrainingAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(TypeOfTrainingAnswer typeOfTrainingAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(typeOfTrainingAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> typeOfTrainingAnswer.getId());
    }
}