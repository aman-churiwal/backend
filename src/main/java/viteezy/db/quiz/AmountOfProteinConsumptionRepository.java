package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfProteinConsumption;

import java.util.List;
import java.util.Optional;

public interface AmountOfProteinConsumptionRepository {

    Try<Optional<AmountOfProteinConsumption>> find(Long id);

    Try<List<AmountOfProteinConsumption>> findAll();

    Try<Long> save(AmountOfProteinConsumption amountOfProteinConsumption);

}