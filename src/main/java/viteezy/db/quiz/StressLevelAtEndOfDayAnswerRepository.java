package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.StressLevelAtEndOfDayAnswer;

import java.util.Optional;
import java.util.UUID;

public interface StressLevelAtEndOfDayAnswerRepository {

    Try<Optional<StressLevelAtEndOfDayAnswer>> find(Long id);

    Try<Optional<StressLevelAtEndOfDayAnswer>> find(UUID quizExternalReference);

    Try<Long> save(StressLevelAtEndOfDayAnswer stressLevelAtEndOfDayAnswer);

    Try<Long> update(StressLevelAtEndOfDayAnswer stressLevelAtEndOfDayAnswer);
}