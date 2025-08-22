package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.EasternMedicineOpinionAnswerRepository;
import viteezy.domain.quiz.EasternMedicineOpinionAnswer;

import java.util.Optional;
import java.util.UUID;

public class EasternMedicineOpinionAnswerRepositoryImpl implements EasternMedicineOpinionAnswerRepository {

    private static final String SELECT_ALL = "SELECT eastern_medicine_opinion_answers.* FROM eastern_medicine_opinion_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on eastern_medicine_opinion_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE eastern_medicine_opinion_answers " +
            "SET eastern_medicine_opinion_id = :easternMedicineOpinionId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO eastern_medicine_opinion_answers(quiz_id, eastern_medicine_opinion_id) " +
            "VALUES(:quizId, :easternMedicineOpinionId);";

    private final Jdbi jdbi;

    public EasternMedicineOpinionAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<EasternMedicineOpinionAnswer>> find(Long id) {
        final HandleCallback<Optional<EasternMedicineOpinionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(EasternMedicineOpinionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<EasternMedicineOpinionAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<EasternMedicineOpinionAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(EasternMedicineOpinionAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(EasternMedicineOpinionAnswer easternMedicineOpinionAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(easternMedicineOpinionAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(EasternMedicineOpinionAnswer easternMedicineOpinionAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(easternMedicineOpinionAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> easternMedicineOpinionAnswer.getId());
    }
}