package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfDairyConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfDairyConsumptionAnswerRepository {

    Try<Optional<AmountOfDairyConsumptionAnswer>> find(Long id);

    Try<Optional<AmountOfDairyConsumptionAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AmountOfDairyConsumptionAnswer amountOfDairyConsumptionAnswer);

    Try<Long> update(AmountOfDairyConsumptionAnswer amountOfDairyConsumptionAnswer);
}