package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.UrinaryInfectionAnswerRepository;
import viteezy.domain.quiz.UrinaryInfectionAnswer;

import java.util.Optional;
import java.util.UUID;

public class UrinaryInfectionAnswerRepositoryImpl implements UrinaryInfectionAnswerRepository {

    private static final String SELECT_ALL = "SELECT urinary_infection_answers.* FROM urinary_infection_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on urinary_infection_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE urinary_infection_answers " +
            "SET urinary_infection_id = :urinaryInfectionId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO urinary_infection_answers(quiz_id, urinary_infection_id) " +
            "VALUES(:quizId, :urinaryInfectionId);";

    private final Jdbi jdbi;

    public UrinaryInfectionAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<UrinaryInfectionAnswer>> find(Long id) {
        final HandleCallback<Optional<UrinaryInfectionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(UrinaryInfectionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<UrinaryInfectionAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<UrinaryInfectionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(UrinaryInfectionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(UrinaryInfectionAnswer urinaryInfectionAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(urinaryInfectionAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(UrinaryInfectionAnswer urinaryInfectionAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(urinaryInfectionAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> urinaryInfectionAnswer.getId());
    }
}