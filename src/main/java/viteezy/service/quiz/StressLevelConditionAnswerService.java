package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.StressLevelConditionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface StressLevelConditionAnswerService {

    Either<Throwable, Optional<StressLevelConditionAnswer>> find(Long id);

    Either<Throwable, Optional<StressLevelConditionAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, StressLevelConditionAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, StressLevelConditionAnswer> update(CategorizedAnswer categorizedAnswer);

}