package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.LibidoStressLevelAnswerRepository;
import viteezy.domain.quiz.LibidoStressLevelAnswer;

import java.util.Optional;
import java.util.UUID;

public class LibidoStressLevelAnswerRepositoryImpl implements LibidoStressLevelAnswerRepository {

    private static final String SELECT_ALL = "SELECT libido_stress_level_answers.* FROM libido_stress_level_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on libido_stress_level_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE libido_stress_level_answers " +
            "SET libido_stress_level_id = :libidoStressLevelId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO libido_stress_level_answers(quiz_id, libido_stress_level_id) " +
            "VALUES(:quizId, :libidoStressLevelId);";

    private final Jdbi jdbi;

    public LibidoStressLevelAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<LibidoStressLevelAnswer>> find(Long id) {
        final HandleCallback<Optional<LibidoStressLevelAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(LibidoStressLevelAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<LibidoStressLevelAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<LibidoStressLevelAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(LibidoStressLevelAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(LibidoStressLevelAnswer libidoStressLevelAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(libidoStressLevelAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(LibidoStressLevelAnswer libidoStressLevelAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(libidoStressLevelAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> libidoStressLevelAnswer.getId());
    }
}