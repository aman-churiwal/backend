package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.TransitionPeriodComplaintsAnswer;

import java.util.Optional;
import java.util.UUID;

public interface TransitionPeriodComplaintsAnswerRepository {

    Try<Optional<TransitionPeriodComplaintsAnswer>> find(Long id);

    Try<Optional<TransitionPeriodComplaintsAnswer>> find(UUID quizExternalReference);

    Try<Long> save(TransitionPeriodComplaintsAnswer transitionPeriodComplaintsAnswer);

    Try<Long> update(TransitionPeriodComplaintsAnswer transitionPeriodComplaintsAnswer);
}