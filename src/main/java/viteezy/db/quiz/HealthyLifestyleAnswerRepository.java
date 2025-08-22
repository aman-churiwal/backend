package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.HealthyLifestyleAnswer;

import java.util.Optional;
import java.util.UUID;

public interface HealthyLifestyleAnswerRepository {

    Try<Optional<HealthyLifestyleAnswer>> find(Long id);

    Try<Optional<HealthyLifestyleAnswer>> find(UUID quizExternalReference);

    Try<Long> save(HealthyLifestyleAnswer healthyLifestyleAnswer);

    Try<Long> update(HealthyLifestyleAnswer healthyLifestyleAnswer);
}