package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.ThirtyMinutesOfSunAnswerRepository;
import viteezy.domain.quiz.ThirtyMinutesOfSunAnswer;

import java.util.Optional;
import java.util.UUID;

public class ThirtyMinutesOfSunAnswerRepositoryImpl implements ThirtyMinutesOfSunAnswerRepository {

    private static final String SELECT_ALL = "SELECT thirty_minutes_of_sun_answers.* FROM thirty_minutes_of_sun_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on thirty_minutes_of_sun_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE thirty_minutes_of_sun_answers " +
            "SET thirty_minutes_of_sun_id = :thirtyMinutesOfSunId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO thirty_minutes_of_sun_answers(quiz_id, thirty_minutes_of_sun_id) " +
            "VALUES(:quizId, :thirtyMinutesOfSunId);";

    private final Jdbi jdbi;

    public ThirtyMinutesOfSunAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<ThirtyMinutesOfSunAnswer>> find(Long id) {
        final HandleCallback<Optional<ThirtyMinutesOfSunAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(ThirtyMinutesOfSunAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<ThirtyMinutesOfSunAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<ThirtyMinutesOfSunAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(ThirtyMinutesOfSunAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(ThirtyMinutesOfSunAnswer thirtyMinutesOfSunAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(thirtyMinutesOfSunAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(ThirtyMinutesOfSunAnswer thirtyMinutesOfSunAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(thirtyMinutesOfSunAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> thirtyMinutesOfSunAnswer.getId());
    }
}