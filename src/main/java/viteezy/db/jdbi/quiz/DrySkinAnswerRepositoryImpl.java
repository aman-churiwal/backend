package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DrySkinAnswerRepository;
import viteezy.domain.quiz.DrySkinAnswer;

import java.util.Optional;
import java.util.UUID;

public class DrySkinAnswerRepositoryImpl implements DrySkinAnswerRepository {

    private static final String SELECT_ALL = "SELECT dry_skin_answers.* FROM dry_skin_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on dry_skin_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE dry_skin_answers " +
            "SET dry_skin_id = :drySkinId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO dry_skin_answers(quiz_id, dry_skin_id) " +
            "VALUES(:quizId, :drySkinId);";

    private final Jdbi jdbi;

    public DrySkinAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DrySkinAnswer>> find(Long id) {
        final HandleCallback<Optional<DrySkinAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DrySkinAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<DrySkinAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<DrySkinAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(DrySkinAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DrySkinAnswer drySkinAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(drySkinAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(DrySkinAnswer drySkinAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(drySkinAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> drySkinAnswer.getId());
    }
}