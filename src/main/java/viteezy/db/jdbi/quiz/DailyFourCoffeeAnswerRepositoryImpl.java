package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DailyFourCoffeeAnswerRepository;
import viteezy.domain.quiz.DailyFourCoffeeAnswer;

import java.util.Optional;
import java.util.UUID;

public class DailyFourCoffeeAnswerRepositoryImpl implements DailyFourCoffeeAnswerRepository {

    private static final String SELECT_ALL = "SELECT daily_four_coffee_answers.* FROM daily_four_coffee_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on daily_four_coffee_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE daily_four_coffee_answers " +
            "SET daily_four_coffee_id = :dailyFourCoffeeId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO daily_four_coffee_answers(quiz_id, daily_four_coffee_id) " +
            "VALUES(:quizId, :dailyFourCoffeeId);";

    private final Jdbi jdbi;

    public DailyFourCoffeeAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DailyFourCoffeeAnswer>> find(Long id) {
        final HandleCallback<Optional<DailyFourCoffeeAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DailyFourCoffeeAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<DailyFourCoffeeAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<DailyFourCoffeeAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(DailyFourCoffeeAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DailyFourCoffeeAnswer dailyFourCoffeeAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(dailyFourCoffeeAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(DailyFourCoffeeAnswer dailyFourCoffeeAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(dailyFourCoffeeAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> dailyFourCoffeeAnswer.getId());
    }
}