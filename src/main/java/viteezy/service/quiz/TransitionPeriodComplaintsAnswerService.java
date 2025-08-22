package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.TransitionPeriodComplaintsAnswer;

import java.util.Optional;
import java.util.UUID;

public interface TransitionPeriodComplaintsAnswerService {

    Either<Throwable, Optional<TransitionPeriodComplaintsAnswer>> find(Long id);

    Either<Throwable, Optional<TransitionPeriodComplaintsAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TransitionPeriodComplaintsAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TransitionPeriodComplaintsAnswer> update(CategorizedAnswer categorizedAnswer);

}