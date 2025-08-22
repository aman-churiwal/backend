package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AmountOfVegetableConsumptionRepository;
import viteezy.domain.quiz.AmountOfVegetableConsumption;

import java.util.List;
import java.util.Optional;

public class AmountOfVegetableConsumptionRepositoryImpl implements AmountOfVegetableConsumptionRepository {

    private static final String SELECT_ALL = "SELECT amount_of_vegetable_consumptions.* FROM amount_of_vegetable_consumptions ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO amount_of_vegetable_consumptions (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AmountOfVegetableConsumptionRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AmountOfVegetableConsumption>> find(Long id) {
        final HandleCallback<Optional<AmountOfVegetableConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AmountOfVegetableConsumption.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AmountOfVegetableConsumption>> findAll() {
        final HandleCallback<List<AmountOfVegetableConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AmountOfVegetableConsumption.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AmountOfVegetableConsumption amountOfVegetableConsumption) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(amountOfVegetableConsumption)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}