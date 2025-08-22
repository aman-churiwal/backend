package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.SportReasonAnswerRepository;
import viteezy.domain.quiz.SportReasonAnswer;

import java.util.Optional;
import java.util.UUID;

public class SportReasonAnswerRepositoryImpl implements SportReasonAnswerRepository {

    private static final String SELECT_ALL = "SELECT sport_reason_answers.* FROM sport_reason_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on sport_reason_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE sport_reason_answers " +
            "SET sport_reason_id = :sportReasonId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO sport_reason_answers(quiz_id, sport_reason_id) " +
            "VALUES(:quizId, :sportReasonId);";

    private final Jdbi jdbi;

    public SportReasonAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<SportReasonAnswer>> find(Long id) {
        final HandleCallback<Optional<SportReasonAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(SportReasonAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<SportReasonAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<SportReasonAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(SportReasonAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(SportReasonAnswer sportReasonAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(sportReasonAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(SportReasonAnswer sportReasonAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(sportReasonAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> sportReasonAnswer.getId());
    }
}