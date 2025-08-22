package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.IronPrescribedAnswerRepository;
import viteezy.domain.quiz.IronPrescribedAnswer;

import java.util.Optional;
import java.util.UUID;

public class IronPrescribedAnswerRepositoryImpl implements IronPrescribedAnswerRepository {

    private static final String SELECT_ALL = "SELECT iron_prescribed_answers.* FROM iron_prescribed_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on iron_prescribed_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE iron_prescribed_answers " +
            "SET iron_prescribed_id = :ironPrescribedId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO iron_prescribed_answers(quiz_id, iron_prescribed_id) " +
            "VALUES(:quizId, :ironPrescribedId);";

    private final Jdbi jdbi;

    public IronPrescribedAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<IronPrescribedAnswer>> find(Long id) {
        final HandleCallback<Optional<IronPrescribedAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(IronPrescribedAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<IronPrescribedAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<IronPrescribedAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(IronPrescribedAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(IronPrescribedAnswer ironPrescribedAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(ironPrescribedAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(IronPrescribedAnswer ironPrescribedAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(ironPrescribedAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> ironPrescribedAnswer.getId());
    }
}