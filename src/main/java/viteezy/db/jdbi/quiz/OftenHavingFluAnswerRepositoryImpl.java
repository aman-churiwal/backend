package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.OftenHavingFluAnswerRepository;
import viteezy.domain.quiz.OftenHavingFluAnswer;

import java.util.Optional;
import java.util.UUID;

public class OftenHavingFluAnswerRepositoryImpl implements OftenHavingFluAnswerRepository {

    private static final String SELECT_ALL = "SELECT often_having_flu_answers.* FROM often_having_flu_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on often_having_flu_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE often_having_flu_answers " +
            "SET often_having_flu_id = :oftenHavingFluId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO often_having_flu_answers(quiz_id, often_having_flu_id) " +
            "VALUES(:quizId, :oftenHavingFluId);";

    private final Jdbi jdbi;

    public OftenHavingFluAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<OftenHavingFluAnswer>> find(Long id) {
        final HandleCallback<Optional<OftenHavingFluAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(OftenHavingFluAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<OftenHavingFluAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<OftenHavingFluAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(OftenHavingFluAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(OftenHavingFluAnswer oftenHavingFluAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(oftenHavingFluAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(OftenHavingFluAnswer oftenHavingFluAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(oftenHavingFluAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> oftenHavingFluAnswer.getId());
    }
}