package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DailyFourCoffeeRepository;
import viteezy.domain.quiz.DailyFourCoffee;

import java.util.List;
import java.util.Optional;

public class DailyFourCoffeeRepositoryImpl implements DailyFourCoffeeRepository {

    private static final String SELECT_ALL = "SELECT daily_four_coffees.* FROM daily_four_coffees ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO daily_four_coffees (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public DailyFourCoffeeRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DailyFourCoffee>> find(Long id) {
        final HandleCallback<Optional<DailyFourCoffee>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DailyFourCoffee.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<DailyFourCoffee>> findAll() {
        final HandleCallback<List<DailyFourCoffee>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(DailyFourCoffee.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DailyFourCoffee dailyFourCoffee) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(dailyFourCoffee)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}