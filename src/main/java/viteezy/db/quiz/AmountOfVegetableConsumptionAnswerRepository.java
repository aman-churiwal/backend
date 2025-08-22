package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfVegetableConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfVegetableConsumptionAnswerRepository {

    Try<Optional<AmountOfVegetableConsumptionAnswer>> find(Long id);

    Try<Optional<AmountOfVegetableConsumptionAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AmountOfVegetableConsumptionAnswer amountOfVegetableConsumptionAnswer);

    Try<Long> update(AmountOfVegetableConsumptionAnswer amountOfVegetableConsumptionAnswer);
}