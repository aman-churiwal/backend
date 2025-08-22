package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.TrainingIntensivelyAnswer;

import java.util.Optional;
import java.util.UUID;

public interface TrainingIntensivelyAnswerRepository {

    Try<Optional<TrainingIntensivelyAnswer>> find(Long id);

    Try<Optional<TrainingIntensivelyAnswer>> find(UUID quizExternalReference);

    Try<Long> save(TrainingIntensivelyAnswer trainingIntensivelyAnswer);

    Try<Long> update(TrainingIntensivelyAnswer trainingIntensivelyAnswer);
}