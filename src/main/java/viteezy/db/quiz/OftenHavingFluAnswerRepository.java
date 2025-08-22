package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.OftenHavingFluAnswer;

import java.util.Optional;
import java.util.UUID;

public interface OftenHavingFluAnswerRepository {

    Try<Optional<OftenHavingFluAnswer>> find(Long id);

    Try<Optional<OftenHavingFluAnswer>> find(UUID quizExternalReference);

    Try<Long> save(OftenHavingFluAnswer oftenHavingFluAnswer);

    Try<Long> update(OftenHavingFluAnswer oftenHavingFluAnswer);
}