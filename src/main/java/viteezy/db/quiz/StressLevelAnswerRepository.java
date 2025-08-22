package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.StressLevelAnswer;

import java.util.Optional;
import java.util.UUID;

public interface StressLevelAnswerRepository {

    Try<Optional<StressLevelAnswer>> find(Long id);

    Try<Optional<StressLevelAnswer>> find(UUID quizExternalReference);

    Try<Long> save(StressLevelAnswer stressLevelAnswer);

    Try<Long> update(StressLevelAnswer stressLevelAnswer);
}