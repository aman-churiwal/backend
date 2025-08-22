package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DigestionAmountAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DigestionAmountAnswerService {

    Either<Throwable, Optional<DigestionAmountAnswer>> find(Long id);

    Either<Throwable, Optional<DigestionAmountAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DigestionAmountAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DigestionAmountAnswer> update(CategorizedAnswer categorizedAnswer);

}