package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfMeatConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfMeatConsumptionAnswerRepository {

    Try<Optional<AmountOfMeatConsumptionAnswer>> find(Long id);

    Try<Optional<AmountOfMeatConsumptionAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AmountOfMeatConsumptionAnswer amountOfMeatConsumptionAnswer);

    Try<Long> update(AmountOfMeatConsumptionAnswer amountOfMeatConsumptionAnswer);
}