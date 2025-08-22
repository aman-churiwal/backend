package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.TrainingIntensivelyAnswerRepository;
import viteezy.domain.quiz.TrainingIntensivelyAnswer;

import java.util.Optional;
import java.util.UUID;

public class TrainingIntensivelyAnswerRepositoryImpl implements TrainingIntensivelyAnswerRepository {

    private static final String SELECT_ALL = "SELECT training_intensively_answers.* FROM training_intensively_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on training_intensively_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE training_intensively_answers " +
            "SET training_intensively_id = :trainingIntensivelyId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO training_intensively_answers(quiz_id, training_intensively_id) " +
            "VALUES(:quizId, :trainingIntensivelyId);";

    private final Jdbi jdbi;

    public TrainingIntensivelyAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<TrainingIntensivelyAnswer>> find(Long id) {
        final HandleCallback<Optional<TrainingIntensivelyAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(TrainingIntensivelyAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<TrainingIntensivelyAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<TrainingIntensivelyAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(TrainingIntensivelyAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(TrainingIntensivelyAnswer trainingIntensivelyAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(trainingIntensivelyAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(TrainingIntensivelyAnswer trainingIntensivelyAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(trainingIntensivelyAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> trainingIntensivelyAnswer.getId());
    }
}