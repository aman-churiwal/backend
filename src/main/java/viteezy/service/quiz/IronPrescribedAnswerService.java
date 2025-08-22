package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.IronPrescribedAnswer;

import java.util.Optional;
import java.util.UUID;

public interface IronPrescribedAnswerService {

    Either<Throwable, Optional<IronPrescribedAnswer>> find(Long id);

    Either<Throwable, Optional<IronPrescribedAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, IronPrescribedAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, IronPrescribedAnswer> update(CategorizedAnswer categorizedAnswer);

}