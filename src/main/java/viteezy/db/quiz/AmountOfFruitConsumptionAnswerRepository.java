package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfFruitConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfFruitConsumptionAnswerRepository {

    Try<Optional<AmountOfFruitConsumptionAnswer>> find(Long id);

    Try<Optional<AmountOfFruitConsumptionAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AmountOfFruitConsumptionAnswer amountOfFruitConsumptionAnswer);

    Try<Long> update(AmountOfFruitConsumptionAnswer amountOfFruitConsumptionAnswer);
}