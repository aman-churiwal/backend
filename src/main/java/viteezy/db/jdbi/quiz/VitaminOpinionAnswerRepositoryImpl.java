package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.VitaminOpinionAnswerRepository;
import viteezy.domain.quiz.VitaminOpinionAnswer;

import java.util.Optional;
import java.util.UUID;

public class VitaminOpinionAnswerRepositoryImpl implements VitaminOpinionAnswerRepository {

    private static final String SELECT_ALL = "SELECT vitamin_opinion_answers.* FROM vitamin_opinion_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on vitamin_opinion_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE vitamin_opinion_answers " +
            "SET vitamin_opinion_id = :vitaminOpinionId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO vitamin_opinion_answers(quiz_id, vitamin_opinion_id) " +
            "VALUES(:quizId, :vitaminOpinionId);";

    private final Jdbi jdbi;

    public VitaminOpinionAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<VitaminOpinionAnswer>> find(Long id) {
        final HandleCallback<Optional<VitaminOpinionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(VitaminOpinionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<VitaminOpinionAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<VitaminOpinionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(VitaminOpinionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(VitaminOpinionAnswer vitaminOpinionAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(vitaminOpinionAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(VitaminOpinionAnswer vitaminOpinionAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(vitaminOpinionAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> vitaminOpinionAnswer.getId());
    }
}