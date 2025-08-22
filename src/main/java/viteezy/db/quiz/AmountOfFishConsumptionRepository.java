package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfFishConsumption;

import java.util.List;
import java.util.Optional;

public interface AmountOfFishConsumptionRepository {

    Try<Optional<AmountOfFishConsumption>> find(Long id);

    Try<List<AmountOfFishConsumption>> findAll();

    Try<Long> save(AmountOfFishConsumption amountOfFishConsumption);

}