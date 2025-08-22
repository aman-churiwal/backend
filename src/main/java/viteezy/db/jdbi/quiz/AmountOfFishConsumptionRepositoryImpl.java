package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AmountOfFishConsumptionRepository;
import viteezy.domain.quiz.AmountOfFishConsumption;

import java.util.List;
import java.util.Optional;

public class AmountOfFishConsumptionRepositoryImpl implements AmountOfFishConsumptionRepository {

    private static final String SELECT_ALL = "SELECT amount_of_fish_consumptions.* FROM amount_of_fish_consumptions ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO amount_of_fish_consumptions (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AmountOfFishConsumptionRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AmountOfFishConsumption>> find(Long id) {
        final HandleCallback<Optional<AmountOfFishConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AmountOfFishConsumption.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AmountOfFishConsumption>> findAll() {
        final HandleCallback<List<AmountOfFishConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AmountOfFishConsumption.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AmountOfFishConsumption amountOfFishConsumption) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(amountOfFishConsumption)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}