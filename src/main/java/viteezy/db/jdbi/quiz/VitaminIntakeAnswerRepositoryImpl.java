package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.VitaminIntakeAnswerRepository;
import viteezy.domain.quiz.VitaminIntakeAnswer;

import java.util.Optional;
import java.util.UUID;

public class VitaminIntakeAnswerRepositoryImpl implements VitaminIntakeAnswerRepository {

    private static final String SELECT_ALL = "SELECT vitamin_intake_answers.* FROM vitamin_intake_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on vitamin_intake_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE vitamin_intake_answers " +
            "SET vitamin_intake_id = :vitaminIntakeId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO vitamin_intake_answers(quiz_id, vitamin_intake_id) " +
            "VALUES(:quizId, :vitaminIntakeId);";

    private final Jdbi jdbi;

    public VitaminIntakeAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<VitaminIntakeAnswer>> find(Long id) {
        final HandleCallback<Optional<VitaminIntakeAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(VitaminIntakeAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<VitaminIntakeAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<VitaminIntakeAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(VitaminIntakeAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(VitaminIntakeAnswer vitaminIntakeAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(vitaminIntakeAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(VitaminIntakeAnswer vitaminIntakeAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(vitaminIntakeAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> vitaminIntakeAnswer.getId());
    }
}