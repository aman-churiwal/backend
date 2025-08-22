package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.BingeEatingAnswerRepository;
import viteezy.domain.quiz.BingeEatingAnswer;

import java.util.Optional;
import java.util.UUID;

public class BingeEatingAnswerRepositoryImpl implements BingeEatingAnswerRepository {

    private static final String SELECT_ALL = "SELECT binge_eatings_answers.* FROM binge_eatings_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on binge_eatings_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE binge_eatings_answers " +
            "SET binge_eating_id = :bingeEatingId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO binge_eatings_answers(quiz_id, binge_eating_id) " +
            "VALUES(:quizId, :bingeEatingId);";

    private final Jdbi jdbi;

    public BingeEatingAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<BingeEatingAnswer>> find(Long id) {
        final HandleCallback<Optional<BingeEatingAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(BingeEatingAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<BingeEatingAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<BingeEatingAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(BingeEatingAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(BingeEatingAnswer bingeEatingAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(bingeEatingAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(BingeEatingAnswer bingeEatingAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(bingeEatingAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> bingeEatingAnswer.getId());
    }
}