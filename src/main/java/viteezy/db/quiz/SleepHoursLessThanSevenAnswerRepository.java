package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SleepHoursLessThanSevenAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SleepHoursLessThanSevenAnswerRepository {

    Try<Optional<SleepHoursLessThanSevenAnswer>> find(Long id);

    Try<Optional<SleepHoursLessThanSevenAnswer>> find(UUID quizExternalReference);

    Try<Long> save(SleepHoursLessThanSevenAnswer sleepHoursLessThanSevenAnswer);

    Try<Long> update(SleepHoursLessThanSevenAnswer sleepHoursLessThanSevenAnswer);
}