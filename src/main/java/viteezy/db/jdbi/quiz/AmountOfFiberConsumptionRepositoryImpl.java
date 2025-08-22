package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AmountOfFiberConsumptionRepository;
import viteezy.domain.quiz.AmountOfFiberConsumption;

import java.util.List;
import java.util.Optional;

public class AmountOfFiberConsumptionRepositoryImpl implements AmountOfFiberConsumptionRepository {

    private static final String SELECT_ALL = "SELECT amount_of_fiber_consumptions.* FROM amount_of_fiber_consumptions ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO amount_of_fiber_consumptions (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AmountOfFiberConsumptionRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AmountOfFiberConsumption>> find(Long id) {
        final HandleCallback<Optional<AmountOfFiberConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AmountOfFiberConsumption.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AmountOfFiberConsumption>> findAll() {
        final HandleCallback<List<AmountOfFiberConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AmountOfFiberConsumption.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AmountOfFiberConsumption amountOfFiberConsumption) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(amountOfFiberConsumption)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}