package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.TrainingIntensivelyAnswer;

import java.util.Optional;
import java.util.UUID;

public interface TrainingIntensivelyAnswerService {

    Either<Throwable, Optional<TrainingIntensivelyAnswer>> find(Long id);

    Either<Throwable, Optional<TrainingIntensivelyAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TrainingIntensivelyAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TrainingIntensivelyAnswer> update(CategorizedAnswer categorizedAnswer);

}