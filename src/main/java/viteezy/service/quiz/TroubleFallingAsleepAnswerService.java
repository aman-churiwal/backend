package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.TroubleFallingAsleepAnswer;

import java.util.Optional;
import java.util.UUID;

public interface TroubleFallingAsleepAnswerService {

    Either<Throwable, Optional<TroubleFallingAsleepAnswer>> find(Long id);

    Either<Throwable, Optional<TroubleFallingAsleepAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TroubleFallingAsleepAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TroubleFallingAsleepAnswer> update(CategorizedAnswer categorizedAnswer);

}