package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DailySixAlcoholicDrinksRepository;
import viteezy.domain.quiz.DailySixAlcoholicDrinks;

import java.util.List;
import java.util.Optional;

public class DailySixAlcoholicDrinksRepositoryImpl implements DailySixAlcoholicDrinksRepository {

    private static final String SELECT_ALL = "SELECT daily_six_alcoholic_drinks.* FROM daily_six_alcoholic_drinks ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO daily_six_alcoholic_drinks (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public DailySixAlcoholicDrinksRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DailySixAlcoholicDrinks>> find(Long id) {
        final HandleCallback<Optional<DailySixAlcoholicDrinks>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DailySixAlcoholicDrinks.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<DailySixAlcoholicDrinks>> findAll() {
        final HandleCallback<List<DailySixAlcoholicDrinks>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(DailySixAlcoholicDrinks.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DailySixAlcoholicDrinks dailySixAlcoholicDrinks) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(dailySixAlcoholicDrinks)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}