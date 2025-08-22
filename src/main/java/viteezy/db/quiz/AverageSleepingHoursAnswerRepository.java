package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AverageSleepingHoursAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AverageSleepingHoursAnswerRepository {

    Try<Optional<AverageSleepingHoursAnswer>> find(Long id);

    Try<Optional<AverageSleepingHoursAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AverageSleepingHoursAnswer averageSleepingHoursAnswer);

    Try<Long> update(AverageSleepingHoursAnswer averageSleepingHoursAnswer);
}