package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.springframework.lang.NonNull;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.quiz.Quiz;

import java.util.Optional;
import java.util.UUID;

public class QuizRepositoryImpl implements QuizRepository {

    private static final String SELECT_ALL = "SELECT * FROM quiz ";
    private static final String INSERT_QUERY = "" +
            "INSERT INTO " +
            "quiz (external_reference, utm_content) " +
            "VALUES (:externalReference, :utmContent)";

    private static final String UPDATE_CUSTOMER_ID_QUERY = "" +
            "UPDATE quiz " +
            "SET last_modified=NOW(), customer_id = :customerId, utm_content = :utmContent " +
            "WHERE id = :id";
    private final Jdbi jdbi;

    public QuizRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<Quiz> find(@NonNull Long id) {
        final HandleCallback<Quiz, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Quiz.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<Quiz>> findOptional(Long id) {
        final HandleCallback<Optional<Quiz>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Quiz.class)
                .findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Quiz> find(UUID externalReference) {
        final HandleCallback<Quiz, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE external_reference = :externalReference")
                .bind("externalReference", externalReference)
                .mapTo(Quiz.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<Quiz>> findByCustomerId(Long customerId) {
        final HandleCallback<Optional<Quiz>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE customer_id = :customerId ORDER BY last_modified DESC LIMIT 1")
                .bind("customerId", customerId)
                .mapTo(Quiz.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Quiz> save(Quiz quiz) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(quiz)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }

    @Override
    public Try<Quiz> update(Quiz quiz) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_CUSTOMER_ID_QUERY)
                .bindBean(quiz)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> find(quiz.getId()));
    }
}
