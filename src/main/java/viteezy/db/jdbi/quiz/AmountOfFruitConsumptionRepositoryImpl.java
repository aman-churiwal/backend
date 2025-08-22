package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AmountOfFruitConsumptionRepository;
import viteezy.domain.quiz.AmountOfFruitConsumption;

import java.util.List;
import java.util.Optional;

public class AmountOfFruitConsumptionRepositoryImpl implements AmountOfFruitConsumptionRepository {

    private static final String SELECT_ALL = "SELECT amount_of_fruit_consumptions.* FROM amount_of_fruit_consumptions ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO amount_of_fruit_consumptions (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AmountOfFruitConsumptionRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AmountOfFruitConsumption>> find(Long id) {
        final HandleCallback<Optional<AmountOfFruitConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AmountOfFruitConsumption.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AmountOfFruitConsumption>> findAll() {
        final HandleCallback<List<AmountOfFruitConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AmountOfFruitConsumption.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AmountOfFruitConsumption amountOfFruitConsumption) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(amountOfFruitConsumption)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}