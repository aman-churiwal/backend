package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DateOfBirthAnswerRepository;
import viteezy.domain.quiz.DateOfBirthAnswer;

import java.util.Optional;
import java.util.UUID;

public class DateOfBirthAnswerRepositoryImpl implements DateOfBirthAnswerRepository {

    private static final String SELECT_ALL = "SELECT date_of_birth_answers.* FROM date_of_birth_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on date_of_birth_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE date_of_birth_answers " +
            "SET date = :date, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO date_of_birth_answers(quiz_id, date) " +
            "VALUES(:quizId, :date);";

    private final Jdbi jdbi;

    public DateOfBirthAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DateOfBirthAnswer>> find(Long id) {
        final HandleCallback<Optional<DateOfBirthAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DateOfBirthAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<DateOfBirthAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<DateOfBirthAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(DateOfBirthAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DateOfBirthAnswer dateOfBirthAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(dateOfBirthAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(DateOfBirthAnswer dateOfBirthAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(dateOfBirthAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> dateOfBirthAnswer.getId());
    }
}