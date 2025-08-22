package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SmokeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SmokeAnswerRepository {

    Try<Optional<SmokeAnswer>> find(Long id);

    Try<Optional<SmokeAnswer>> find(UUID quizExternalReference);

    Try<Long> save(SmokeAnswer smokeAnswer);

    Try<Long> update(SmokeAnswer smokeAnswer);
}