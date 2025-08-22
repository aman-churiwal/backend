package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.EnergyState;

import java.util.List;
import java.util.Optional;

public interface EnergyStateRepository {

    Try<Optional<EnergyState>> find(Long id);

    Try<List<EnergyState>> findAll();

    Try<Long> save(EnergyState energyState);

}