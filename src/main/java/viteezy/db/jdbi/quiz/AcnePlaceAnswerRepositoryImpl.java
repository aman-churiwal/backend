package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AcnePlaceAnswerRepository;
import viteezy.domain.quiz.AcnePlaceAnswer;

import java.util.Optional;
import java.util.UUID;

public class AcnePlaceAnswerRepositoryImpl implements AcnePlaceAnswerRepository {

    private static final String SELECT_ALL = "SELECT acne_place_answers.* FROM acne_place_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on acne_place_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE acne_place_answers " +
            "SET acne_place_id = :acnePlaceId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO acne_place_answers(quiz_id, acne_place_id) " +
            "VALUES(:quizId, :acnePlaceId);";

    private final Jdbi jdbi;

    public AcnePlaceAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AcnePlaceAnswer>> find(Long id) {
        final HandleCallback<Optional<AcnePlaceAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AcnePlaceAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<AcnePlaceAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<AcnePlaceAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(AcnePlaceAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AcnePlaceAnswer acnePlaceAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(acnePlaceAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(AcnePlaceAnswer acnePlaceAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(acnePlaceAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> acnePlaceAnswer.getId());
    }
}