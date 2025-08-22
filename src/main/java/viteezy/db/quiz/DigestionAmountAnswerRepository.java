package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DigestionAmountAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DigestionAmountAnswerRepository {

    Try<Optional<DigestionAmountAnswer>> find(Long id);

    Try<Optional<DigestionAmountAnswer>> find(UUID quizExternalReference);

    Try<Long> save(DigestionAmountAnswer digestionAmountAnswer);

    Try<Long> update(DigestionAmountAnswer digestionAmountAnswer);
}