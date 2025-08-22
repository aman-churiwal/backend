package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfFishConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfFishConsumptionAnswerRepository {

    Try<Optional<AmountOfFishConsumptionAnswer>> find(Long id);

    Try<Optional<AmountOfFishConsumptionAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AmountOfFishConsumptionAnswer amountOfFishConsumptionAnswer);

    Try<Long> update(AmountOfFishConsumptionAnswer amountOfFishConsumptionAnswer);
}