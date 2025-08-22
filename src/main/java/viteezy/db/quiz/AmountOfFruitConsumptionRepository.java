package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfFruitConsumption;

import java.util.List;
import java.util.Optional;

public interface AmountOfFruitConsumptionRepository {

    Try<Optional<AmountOfFruitConsumption>> find(Long id);

    Try<List<AmountOfFruitConsumption>> findAll();

    Try<Long> save(AmountOfFruitConsumption amountOfFruitConsumption);

}