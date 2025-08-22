package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfDairyConsumption;

import java.util.List;
import java.util.Optional;

public interface AmountOfDairyConsumptionRepository {

    Try<Optional<AmountOfDairyConsumption>> find(Long id);

    Try<List<AmountOfDairyConsumption>> findAll();

    Try<Long> save(AmountOfDairyConsumption amountOfDairyConsumption);

}