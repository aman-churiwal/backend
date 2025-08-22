package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.CurrentLibidoAnswerRepository;
import viteezy.domain.quiz.CurrentLibidoAnswer;

import java.util.Optional;
import java.util.UUID;

public class CurrentLibidoAnswerRepositoryImpl implements CurrentLibidoAnswerRepository {

    private static final String SELECT_ALL = "SELECT current_libido_answers.* FROM current_libido_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on current_libido_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE current_libido_answers " +
            "SET current_libido_id = :currentLibidoId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO current_libido_answers(quiz_id, current_libido_id) " +
            "VALUES(:quizId, :currentLibidoId);";

    private final Jdbi jdbi;

    public CurrentLibidoAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<CurrentLibidoAnswer>> find(Long id) {
        final HandleCallback<Optional<CurrentLibidoAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(CurrentLibidoAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<CurrentLibidoAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<CurrentLibidoAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(CurrentLibidoAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(CurrentLibidoAnswer currentLibidoAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(currentLibidoAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(CurrentLibidoAnswer currentLibidoAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(currentLibidoAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> currentLibidoAnswer.getId());
    }
}