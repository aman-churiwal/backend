package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AmountOfProteinConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfProteinConsumptionAnswerRepository {

    Try<Optional<AmountOfProteinConsumptionAnswer>> find(Long id);

    Try<Optional<AmountOfProteinConsumptionAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AmountOfProteinConsumptionAnswer amountOfProteinConsumptionAnswer);

    Try<Long> update(AmountOfProteinConsumptionAnswer amountOfProteinConsumptionAnswer);
}