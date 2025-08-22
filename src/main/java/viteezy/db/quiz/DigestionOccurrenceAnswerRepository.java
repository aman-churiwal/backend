package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DigestionOccurrenceAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DigestionOccurrenceAnswerRepository {

    Try<Optional<DigestionOccurrenceAnswer>> find(Long id);

    Try<Optional<DigestionOccurrenceAnswer>> find(UUID quizExternalReference);

    Try<Long> save(DigestionOccurrenceAnswer digestionOccurrenceAnswer);

    Try<Long> update(DigestionOccurrenceAnswer digestionOccurrenceAnswer);
}