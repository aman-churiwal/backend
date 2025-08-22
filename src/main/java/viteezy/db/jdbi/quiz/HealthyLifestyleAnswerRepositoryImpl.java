package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.HealthyLifestyleAnswerRepository;
import viteezy.domain.quiz.HealthyLifestyleAnswer;

import java.util.Optional;
import java.util.UUID;

public class HealthyLifestyleAnswerRepositoryImpl implements HealthyLifestyleAnswerRepository {

    private static final String SELECT_ALL = "SELECT healthy_lifestyle_answers.* FROM healthy_lifestyle_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on healthy_lifestyle_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE healthy_lifestyle_answers " +
            "SET healthy_lifestyle_id = :healthyLifestyleId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO healthy_lifestyle_answers(quiz_id, healthy_lifestyle_id) " +
            "VALUES(:quizId, :healthyLifestyleId);";

    private final Jdbi jdbi;

    public HealthyLifestyleAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<HealthyLifestyleAnswer>> find(Long id) {
        final HandleCallback<Optional<HealthyLifestyleAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(HealthyLifestyleAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<HealthyLifestyleAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<HealthyLifestyleAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(HealthyLifestyleAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(HealthyLifestyleAnswer healthyLifestyleAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(healthyLifestyleAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(HealthyLifestyleAnswer healthyLifestyleAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(healthyLifestyleAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> healthyLifestyleAnswer.getId());
    }
}