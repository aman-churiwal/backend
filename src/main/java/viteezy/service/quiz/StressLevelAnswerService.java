package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.StressLevelAnswer;

import java.util.Optional;
import java.util.UUID;

public interface StressLevelAnswerService {

    Either<Throwable, Optional<StressLevelAnswer>> find(Long id);

    Either<Throwable, Optional<StressLevelAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, StressLevelAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, StressLevelAnswer> update(CategorizedAnswer categorizedAnswer);

}