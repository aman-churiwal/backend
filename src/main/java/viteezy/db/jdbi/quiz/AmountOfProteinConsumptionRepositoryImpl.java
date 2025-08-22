package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AmountOfProteinConsumptionRepository;
import viteezy.domain.quiz.AmountOfProteinConsumption;

import java.util.List;
import java.util.Optional;

public class AmountOfProteinConsumptionRepositoryImpl implements AmountOfProteinConsumptionRepository {

    private static final String SELECT_ALL = "SELECT amount_of_protein_consumptions.* FROM amount_of_protein_consumptions ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO amount_of_protein_consumptions (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AmountOfProteinConsumptionRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AmountOfProteinConsumption>> find(Long id) {
        final HandleCallback<Optional<AmountOfProteinConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AmountOfProteinConsumption.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AmountOfProteinConsumption>> findAll() {
        final HandleCallback<List<AmountOfProteinConsumption>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AmountOfProteinConsumption.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AmountOfProteinConsumption amountOfProteinConsumption) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(amountOfProteinConsumption)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}