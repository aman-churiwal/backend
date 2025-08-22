package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.EnergyStateRepository;
import viteezy.domain.quiz.EnergyState;

import java.util.List;
import java.util.Optional;

public class EnergyStateRepositoryImpl implements EnergyStateRepository {

    private static final String SELECT_ALL = "SELECT energy_states.* FROM energy_states ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO energy_states (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public EnergyStateRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<EnergyState>> find(Long id) {
        final HandleCallback<Optional<EnergyState>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(EnergyState.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<EnergyState>> findAll() {
        final HandleCallback<List<EnergyState>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(EnergyState.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(EnergyState energyState) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(energyState)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}