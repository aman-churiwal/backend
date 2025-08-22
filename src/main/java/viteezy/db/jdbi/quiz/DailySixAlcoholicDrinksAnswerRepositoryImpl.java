package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DailySixAlcoholicDrinksAnswerRepository;
import viteezy.domain.quiz.DailySixAlcoholicDrinksAnswer;

import java.util.Optional;
import java.util.UUID;

public class DailySixAlcoholicDrinksAnswerRepositoryImpl implements DailySixAlcoholicDrinksAnswerRepository {

    private static final String SELECT_ALL = "SELECT daily_six_alcoholic_drinks_answers.* FROM daily_six_alcoholic_drinks_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on daily_six_alcoholic_drinks_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE daily_six_alcoholic_drinks_answers " +
            "SET daily_six_alcoholic_drinks_id = :dailySixAlcoholicDrinksId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO daily_six_alcoholic_drinks_answers(quiz_id, daily_six_alcoholic_drinks_id) " +
            "VALUES(:quizId, :dailySixAlcoholicDrinksId);";

    private final Jdbi jdbi;

    public DailySixAlcoholicDrinksAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DailySixAlcoholicDrinksAnswer>> find(Long id) {
        final HandleCallback<Optional<DailySixAlcoholicDrinksAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DailySixAlcoholicDrinksAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<DailySixAlcoholicDrinksAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<DailySixAlcoholicDrinksAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(DailySixAlcoholicDrinksAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DailySixAlcoholicDrinksAnswer dailySixAlcoholicDrinksAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(dailySixAlcoholicDrinksAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(DailySixAlcoholicDrinksAnswer dailySixAlcoholicDrinksAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(dailySixAlcoholicDrinksAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> dailySixAlcoholicDrinksAnswer.getId());
    }
}