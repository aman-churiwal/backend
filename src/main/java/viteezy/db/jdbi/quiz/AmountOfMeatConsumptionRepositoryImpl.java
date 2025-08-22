package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AmountOfMeatConsumptionRepository;
import viteezy.domain.quiz.AmountOfMeatConsumption;

import java.util.List;
import java.util.Optional;

public class AmountOfMeatConsumptionRepositoryImpl implements AmountOfMeatConsumptionRepository {

    private static final String SELECT_ALL = "SELECT amount_of_meat_consumptions.* FROM amount_of_meat_consumptions ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO amount_of_meat_consumptions (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AmountOfMeatConsumptionRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AmountOfMeatConsumption>> find(Long id) {
        final HandleCallback<Optional<AmountOfMeatConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AmountOfMeatConsumption.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AmountOfMeatConsumption>> findAll() {
        final HandleCallback<List<AmountOfMeatConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AmountOfMeatConsumption.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AmountOfMeatConsumption amountOfMeatConsumption) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(amountOfMeatConsumption)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}