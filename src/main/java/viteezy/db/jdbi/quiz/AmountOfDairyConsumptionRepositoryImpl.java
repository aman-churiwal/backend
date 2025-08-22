package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AmountOfDairyConsumptionRepository;
import viteezy.domain.quiz.AmountOfDairyConsumption;

import java.util.List;
import java.util.Optional;

public class AmountOfDairyConsumptionRepositoryImpl implements AmountOfDairyConsumptionRepository {

    private static final String SELECT_ALL = "SELECT amount_of_dairy_consumptions.* FROM amount_of_dairy_consumptions ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO amount_of_dairy_consumptions (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AmountOfDairyConsumptionRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AmountOfDairyConsumption>> find(Long id) {
        final HandleCallback<Optional<AmountOfDairyConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AmountOfDairyConsumption.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AmountOfDairyConsumption>> findAll() {
        final HandleCallback<List<AmountOfDairyConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AmountOfDairyConsumption.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AmountOfDairyConsumption amountOfDairyConsumption) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(amountOfDairyConsumption)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}