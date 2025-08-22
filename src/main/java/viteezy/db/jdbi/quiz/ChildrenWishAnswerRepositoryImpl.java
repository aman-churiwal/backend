package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.ChildrenWishAnswerRepository;
import viteezy.domain.quiz.ChildrenWishAnswer;

import java.util.Optional;
import java.util.UUID;

public class ChildrenWishAnswerRepositoryImpl implements ChildrenWishAnswerRepository {

    private static final String SELECT_ALL = "SELECT children_wish_answers.* FROM children_wish_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on children_wish_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE children_wish_answers " +
            "SET children_wish_id = :childrenWishId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO children_wish_answers(quiz_id, children_wish_id) " +
            "VALUES(:quizId, :childrenWishId);";

    private final Jdbi jdbi;

    public ChildrenWishAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<ChildrenWishAnswer>> find(Long id) {
        final HandleCallback<Optional<ChildrenWishAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(ChildrenWishAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<ChildrenWishAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<ChildrenWishAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(ChildrenWishAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(ChildrenWishAnswer childrenWishAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(childrenWishAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(ChildrenWishAnswer childrenWishAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(childrenWishAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> childrenWishAnswer.getId());
    }
}