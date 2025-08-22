package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.PrimaryGoalAnswer;

import java.util.Optional;
import java.util.UUID;

public interface PrimaryGoalAnswerRepository {

    Try<Optional<PrimaryGoalAnswer>> find(Long id);

    Try<Optional<PrimaryGoalAnswer>> find(UUID quizExternalReference);

    Try<Long> save(PrimaryGoalAnswer primaryGoalAnswer);

    Try<Long> update(PrimaryGoalAnswer primaryGoalAnswer);
}