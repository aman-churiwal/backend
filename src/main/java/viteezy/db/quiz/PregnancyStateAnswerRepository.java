package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.PregnancyStateAnswer;

import java.util.Optional;
import java.util.UUID;

public interface PregnancyStateAnswerRepository {

    Try<Optional<PregnancyStateAnswer>> find(Long id);

    Try<Optional<PregnancyStateAnswer>> find(UUID quizExternalReference);

    Try<Long> save(PregnancyStateAnswer pregnancyStateAnswer);

    Try<Long> update(PregnancyStateAnswer pregnancyStateAnswer);
}