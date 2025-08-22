package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.OftenHavingFluAnswer;

import java.util.Optional;
import java.util.UUID;

public interface OftenHavingFluAnswerService {

    Either<Throwable, Optional<OftenHavingFluAnswer>> find(Long id);

    Either<Throwable, Optional<OftenHavingFluAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, OftenHavingFluAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, OftenHavingFluAnswer> update(CategorizedAnswer categorizedAnswer);

}