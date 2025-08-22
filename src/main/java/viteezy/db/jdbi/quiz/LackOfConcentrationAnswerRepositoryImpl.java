package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.LackOfConcentrationAnswerRepository;
import viteezy.domain.quiz.LackOfConcentrationAnswer;

import java.util.Optional;
import java.util.UUID;

public class LackOfConcentrationAnswerRepositoryImpl implements LackOfConcentrationAnswerRepository {

    private static final String SELECT_ALL = "SELECT lack_of_concentration_answers.* FROM lack_of_concentration_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on lack_of_concentration_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE lack_of_concentration_answers " +
            "SET lack_of_concentration_id = :lackOfConcentrationId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO lack_of_concentration_answers(quiz_id, lack_of_concentration_id) " +
            "VALUES(:quizId, :lackOfConcentrationId);";

    private final Jdbi jdbi;

    public LackOfConcentrationAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<LackOfConcentrationAnswer>> find(Long id) {
        final HandleCallback<Optional<LackOfConcentrationAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(LackOfConcentrationAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<LackOfConcentrationAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<LackOfConcentrationAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(LackOfConcentrationAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(LackOfConcentrationAnswer lackOfConcentrationAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(lackOfConcentrationAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(LackOfConcentrationAnswer lackOfConcentrationAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(lackOfConcentrationAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> lackOfConcentrationAnswer.getId());
    }
}