package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SleepQualityAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SleepQualityAnswerRepository {

    Try<Optional<SleepQualityAnswer>> find(Long id);

    Try<Optional<SleepQualityAnswer>> find(UUID quizExternalReference);

    Try<Long> save(SleepQualityAnswer sleepQualityAnswer);

    Try<Long> update(SleepQualityAnswer sleepQualityAnswer);
}