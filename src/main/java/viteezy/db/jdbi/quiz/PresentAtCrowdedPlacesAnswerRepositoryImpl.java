package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.PresentAtCrowdedPlacesAnswerRepository;
import viteezy.domain.quiz.PresentAtCrowdedPlacesAnswer;

import java.util.Optional;
import java.util.UUID;

public class PresentAtCrowdedPlacesAnswerRepositoryImpl implements PresentAtCrowdedPlacesAnswerRepository {

    private static final String SELECT_ALL = "SELECT present_at_crowded_places_answers.* FROM present_at_crowded_places_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on present_at_crowded_places_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE present_at_crowded_places_answers " +
            "SET present_at_crowded_places_id = :presentAtCrowdedPlacesId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO present_at_crowded_places_answers(quiz_id, present_at_crowded_places_id) " +
            "VALUES(:quizId, :presentAtCrowdedPlacesId);";

    private final Jdbi jdbi;

    public PresentAtCrowdedPlacesAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<PresentAtCrowdedPlacesAnswer>> find(Long id) {
        final HandleCallback<Optional<PresentAtCrowdedPlacesAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(PresentAtCrowdedPlacesAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<PresentAtCrowdedPlacesAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<PresentAtCrowdedPlacesAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(PresentAtCrowdedPlacesAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(PresentAtCrowdedPlacesAnswer presentAtCrowdedPlacesAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(presentAtCrowdedPlacesAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(PresentAtCrowdedPlacesAnswer presentAtCrowdedPlacesAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(presentAtCrowdedPlacesAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> presentAtCrowdedPlacesAnswer.getId());
    }
}