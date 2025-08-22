package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfMeatConsumption;

import java.util.List;
import java.util.Optional;

public interface AmountOfMeatConsumptionRepository {

    Try<Optional<AmountOfMeatConsumption>> find(Long id);

    Try<List<AmountOfMeatConsumption>> findAll();

    Try<Long> save(AmountOfMeatConsumption amountOfMeatConsumption);

}