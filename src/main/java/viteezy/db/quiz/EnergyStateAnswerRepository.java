package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.EnergyStateAnswer;

import java.util.Optional;
import java.util.UUID;

public interface EnergyStateAnswerRepository {

    Try<Optional<EnergyStateAnswer>> find(Long id);

    Try<Optional<EnergyStateAnswer>> find(UUID quizExternalReference);

    Try<Long> save(EnergyStateAnswer energyStateAnswer);

    Try<Long> update(EnergyStateAnswer energyStateAnswer);
}