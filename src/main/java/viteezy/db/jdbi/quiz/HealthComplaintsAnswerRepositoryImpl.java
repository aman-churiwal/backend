package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.HealthComplaintsAnswerRepository;
import viteezy.domain.quiz.HealthComplaintsAnswer;

import java.util.Optional;
import java.util.UUID;

public class HealthComplaintsAnswerRepositoryImpl implements HealthComplaintsAnswerRepository {

    private static final String SELECT_ALL = "SELECT health_complaints_answers.* FROM health_complaints_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on health_complaints_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE health_complaints_answers " +
            "SET health_complaints_id = :healthComplaintsId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO health_complaints_answers(quiz_id, health_complaints_id) " +
            "VALUES(:quizId, :healthComplaintsId);";

    private final Jdbi jdbi;

    public HealthComplaintsAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<HealthComplaintsAnswer>> find(Long id) {
        final HandleCallback<Optional<HealthComplaintsAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(HealthComplaintsAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<HealthComplaintsAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<HealthComplaintsAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(HealthComplaintsAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(HealthComplaintsAnswer healthComplaintsAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(healthComplaintsAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(HealthComplaintsAnswer healthComplaintsAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(healthComplaintsAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> healthComplaintsAnswer.getId());
    }
}