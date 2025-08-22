package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DigestionOccurrenceAnswerRepository;
import viteezy.domain.quiz.DigestionOccurrenceAnswer;

import java.util.Optional;
import java.util.UUID;

public class DigestionOccurrenceAnswerRepositoryImpl implements DigestionOccurrenceAnswerRepository {

    private static final String SELECT_ALL = "SELECT digestion_occurrence_answers.* FROM digestion_occurrence_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on digestion_occurrence_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE digestion_occurrence_answers " +
            "SET digestion_occurrence_id = :digestionOccurrenceId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO digestion_occurrence_answers(quiz_id, digestion_occurrence_id) " +
            "VALUES(:quizId, :digestionOccurrenceId);";

    private final Jdbi jdbi;

    public DigestionOccurrenceAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DigestionOccurrenceAnswer>> find(Long id) {
        final HandleCallback<Optional<DigestionOccurrenceAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DigestionOccurrenceAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<DigestionOccurrenceAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<DigestionOccurrenceAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(DigestionOccurrenceAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DigestionOccurrenceAnswer digestionOccurrenceAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(digestionOccurrenceAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(DigestionOccurrenceAnswer digestionOccurrenceAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(digestionOccurrenceAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> digestionOccurrenceAnswer.getId());
    }
}