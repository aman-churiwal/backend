package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AllergyTypeAnswerRepository;
import viteezy.domain.quiz.AllergyTypeAnswer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AllergyTypeAnswerRepositoryImpl implements AllergyTypeAnswerRepository {

    private static final String SELECT_ALL = "SELECT allergy_type_answers.* FROM allergy_type_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on allergy_type_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";
    private static final String SELECT_BY_QUIZ_EXT_REF_AND_ALLERGY_TYPE_ID = SELECT_ALL +
            " JOIN quiz q on allergy_type_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference AND allergy_type_answers.allergy_type_id = :allergyTypeId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO allergy_type_answers(quiz_id, allergy_type_id) " +
            "VALUES(:quizId, :allergyTypeId);";

    private static final String DELETE_BY_ID = "" +
            "DELETE FROM allergy_type_answers " +
            "WHERE id = :id";

    private final Jdbi jdbi;

    public AllergyTypeAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AllergyTypeAnswer>> find(Long id) {
        final HandleCallback<Optional<AllergyTypeAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AllergyTypeAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AllergyTypeAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<List<AllergyTypeAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(AllergyTypeAnswer.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<AllergyTypeAnswer>> find(UUID quizExternalReference, Long allergyTypeId) {
        final HandleCallback<Optional<AllergyTypeAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF_AND_ALLERGY_TYPE_ID)
                .bind("quizExternalReference", quizExternalReference)
                .bind("allergyTypeId", allergyTypeId)
                .mapTo(AllergyTypeAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AllergyTypeAnswer allergyTypeAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(allergyTypeAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Void> delete(Long id) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(DELETE_BY_ID)
                .bind("id", id)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> Try.success(null));
    }
}