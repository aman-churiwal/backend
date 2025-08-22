package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.PregnancyStateAnswerRepository;
import viteezy.domain.quiz.PregnancyStateAnswer;

import java.util.Optional;
import java.util.UUID;

public class PregnancyStateAnswerRepositoryImpl implements PregnancyStateAnswerRepository {

    private static final String SELECT_ALL = "SELECT pregnancy_state_answers.* FROM pregnancy_state_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on pregnancy_state_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE pregnancy_state_answers " +
            "SET pregnancy_state_id = :pregnancyStateId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO pregnancy_state_answers(quiz_id, pregnancy_state_id) " +
            "VALUES(:quizId, :pregnancyStateId);";

    private final Jdbi jdbi;

    public PregnancyStateAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<PregnancyStateAnswer>> find(Long id) {
        final HandleCallback<Optional<PregnancyStateAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(PregnancyStateAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<PregnancyStateAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<PregnancyStateAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(PregnancyStateAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(PregnancyStateAnswer pregnancyStateAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(pregnancyStateAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(PregnancyStateAnswer pregnancyStateAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(pregnancyStateAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> pregnancyStateAnswer.getId());
    }
}