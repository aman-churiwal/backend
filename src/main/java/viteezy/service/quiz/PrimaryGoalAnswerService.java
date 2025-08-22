package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.PrimaryGoalAnswer;

import java.util.Optional;
import java.util.UUID;

public interface PrimaryGoalAnswerService {

    Either<Throwable, Optional<PrimaryGoalAnswer>> find(Long id);

    Either<Throwable, Optional<PrimaryGoalAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, PrimaryGoalAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, PrimaryGoalAnswer> update(CategorizedAnswer categorizedAnswer);

}