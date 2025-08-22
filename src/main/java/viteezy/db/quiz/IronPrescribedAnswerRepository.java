package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.IronPrescribedAnswer;

import java.util.Optional;
import java.util.UUID;

public interface IronPrescribedAnswerRepository {

    Try<Optional<IronPrescribedAnswer>> find(Long id);

    Try<Optional<IronPrescribedAnswer>> find(UUID quizExternalReference);

    Try<Long> save(IronPrescribedAnswer ironPrescribedAnswer);

    Try<Long> update(IronPrescribedAnswer ironPrescribedAnswer);
}