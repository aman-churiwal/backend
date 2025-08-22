package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.MentalFitnessAnswerRepository;
import viteezy.domain.quiz.MentalFitnessAnswer;

import java.util.Optional;
import java.util.UUID;

public class MentalFitnessAnswerRepositoryImpl implements MentalFitnessAnswerRepository {

    private static final String SELECT_ALL = "SELECT mental_fitness_answers.* FROM mental_fitness_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on mental_fitness_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE mental_fitness_answers " +
            "SET mental_fitness_id = :mentalFitnessId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO mental_fitness_answers(quiz_id, mental_fitness_id) " +
            "VALUES(:quizId, :mentalFitnessId);";

    private final Jdbi jdbi;

    public MentalFitnessAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<MentalFitnessAnswer>> find(Long id) {
        final HandleCallback<Optional<MentalFitnessAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(MentalFitnessAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<MentalFitnessAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<MentalFitnessAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(MentalFitnessAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(MentalFitnessAnswer mentalFitnessAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(mentalFitnessAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(MentalFitnessAnswer mentalFitnessAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(mentalFitnessAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> mentalFitnessAnswer.getId());
    }
}