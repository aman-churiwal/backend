package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.WeeklyTwelveAlcoholicDrinksAnswerRepository;
import viteezy.domain.quiz.WeeklyTwelveAlcoholicDrinksAnswer;

import java.util.Optional;
import java.util.UUID;

public class WeeklyTwelveAlcoholicDrinksAnswerRepositoryImpl implements WeeklyTwelveAlcoholicDrinksAnswerRepository {

    private static final String SELECT_ALL = "SELECT weekly_twelve_alcoholic_drinks_answers.* FROM weekly_twelve_alcoholic_drinks_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on weekly_twelve_alcoholic_drinks_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE weekly_twelve_alcoholic_drinks_answers " +
            "SET weekly_twelve_alcoholic_drinks_id = :weeklyTwelveAlcoholicDrinksId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO weekly_twelve_alcoholic_drinks_answers(quiz_id, weekly_twelve_alcoholic_drinks_id) " +
            "VALUES(:quizId, :weeklyTwelveAlcoholicDrinksId);";

    private final Jdbi jdbi;

    public WeeklyTwelveAlcoholicDrinksAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<WeeklyTwelveAlcoholicDrinksAnswer>> find(Long id) {
        final HandleCallback<Optional<WeeklyTwelveAlcoholicDrinksAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(WeeklyTwelveAlcoholicDrinksAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<WeeklyTwelveAlcoholicDrinksAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<WeeklyTwelveAlcoholicDrinksAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(WeeklyTwelveAlcoholicDrinksAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(WeeklyTwelveAlcoholicDrinksAnswer weeklyTwelveAlcoholicDrinksAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(weeklyTwelveAlcoholicDrinksAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(WeeklyTwelveAlcoholicDrinksAnswer weeklyTwelveAlcoholicDrinksAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(weeklyTwelveAlcoholicDrinksAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> weeklyTwelveAlcoholicDrinksAnswer.getId());
    }
}