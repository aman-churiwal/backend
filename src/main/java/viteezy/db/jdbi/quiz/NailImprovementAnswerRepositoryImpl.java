package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.NailImprovementAnswerRepository;
import viteezy.domain.quiz.NailImprovementAnswer;

import java.util.Optional;
import java.util.UUID;

public class NailImprovementAnswerRepositoryImpl implements NailImprovementAnswerRepository {

    private static final String SELECT_ALL = "SELECT nail_improvement_answers.* FROM nail_improvement_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on nail_improvement_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE nail_improvement_answers " +
            "SET nail_improvement_id = :nailImprovementId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO nail_improvement_answers(quiz_id, nail_improvement_id) " +
            "VALUES(:quizId, :nailImprovementId);";

    private final Jdbi jdbi;

    public NailImprovementAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<NailImprovementAnswer>> find(Long id) {
        final HandleCallback<Optional<NailImprovementAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(NailImprovementAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<NailImprovementAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<NailImprovementAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(NailImprovementAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(NailImprovementAnswer nailImprovementAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(nailImprovementAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(NailImprovementAnswer nailImprovementAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(nailImprovementAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> nailImprovementAnswer.getId());
    }
}