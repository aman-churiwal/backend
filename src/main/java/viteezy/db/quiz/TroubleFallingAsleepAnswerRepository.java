package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.TroubleFallingAsleepAnswer;

import java.util.Optional;
import java.util.UUID;

public interface TroubleFallingAsleepAnswerRepository {

    Try<Optional<TroubleFallingAsleepAnswer>> find(Long id);

    Try<Optional<TroubleFallingAsleepAnswer>> find(UUID quizExternalReference);

    Try<Long> save(TroubleFallingAsleepAnswer troubleFallingAsleepAnswer);

    Try<Long> update(TroubleFallingAsleepAnswer troubleFallingAsleepAnswer);
}