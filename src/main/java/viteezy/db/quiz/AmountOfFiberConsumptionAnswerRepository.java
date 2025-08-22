package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfFiberConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfFiberConsumptionAnswerRepository {

    Try<Optional<AmountOfFiberConsumptionAnswer>> find(Long id);

    Try<Optional<AmountOfFiberConsumptionAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AmountOfFiberConsumptionAnswer amountOfFiberConsumptionAnswer);

    Try<Long> update(AmountOfFiberConsumptionAnswer amountOfFiberConsumptionAnswer);
}