package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.StressLevelConditionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface StressLevelConditionAnswerRepository {

    Try<Optional<StressLevelConditionAnswer>> find(Long id);

    Try<Optional<StressLevelConditionAnswer>> find(UUID quizExternalReference);

    Try<Long> save(StressLevelConditionAnswer stressLevelConditionAnswer);

    Try<Long> update(StressLevelConditionAnswer stressLevelConditionAnswer);
}