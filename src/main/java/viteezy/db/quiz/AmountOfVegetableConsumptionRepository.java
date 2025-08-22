package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfVegetableConsumption;

import java.util.List;
import java.util.Optional;

public interface AmountOfVegetableConsumptionRepository {

    Try<Optional<AmountOfVegetableConsumption>> find(Long id);

    Try<List<AmountOfVegetableConsumption>> findAll();

    Try<Long> save(AmountOfVegetableConsumption amountOfVegetableConsumption);

}