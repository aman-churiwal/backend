package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.WeeklyTwelveAlcoholicDrinksRepository;
import viteezy.domain.quiz.WeeklyTwelveAlcoholicDrinks;

import java.util.List;
import java.util.Optional;

public class WeeklyTwelveAlcoholicDrinksRepositoryImpl implements WeeklyTwelveAlcoholicDrinksRepository {

    private static final String SELECT_ALL = "SELECT weekly_twelve_alcoholic_drinks.* FROM weekly_twelve_alcoholic_drinks ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO weekly_twelve_alcoholic_drinks (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public WeeklyTwelveAlcoholicDrinksRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<WeeklyTwelveAlcoholicDrinks>> find(Long id) {
        final HandleCallback<Optional<WeeklyTwelveAlcoholicDrinks>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(WeeklyTwelveAlcoholicDrinks.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<WeeklyTwelveAlcoholicDrinks>> findAll() {
        final HandleCallback<List<WeeklyTwelveAlcoholicDrinks>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(WeeklyTwelveAlcoholicDrinks.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(WeeklyTwelveAlcoholicDrinks weeklyTwelveAlcoholicDrinks) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(weeklyTwelveAlcoholicDrinks)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}