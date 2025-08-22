package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfFiberConsumption;

import java.util.List;
import java.util.Optional;

public interface AmountOfFiberConsumptionRepository {

    Try<Optional<AmountOfFiberConsumption>> find(Long id);

    Try<List<AmountOfFiberConsumption>> findAll();

    Try<Long> save(AmountOfFiberConsumption amountOfFiberConsumption);

}