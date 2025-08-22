package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.StressLevelAtEndOfDayAnswer;

import java.util.Optional;
import java.util.UUID;

public interface StressLevelAtEndOfDayAnswerService {

    Either<Throwable, Optional<StressLevelAtEndOfDayAnswer>> find(Long id);

    Either<Throwable, Optional<StressLevelAtEndOfDayAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, StressLevelAtEndOfDayAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, StressLevelAtEndOfDayAnswer> update(CategorizedAnswer categorizedAnswer);

}