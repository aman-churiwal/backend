package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.SleepQualityAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SleepQualityAnswerService {

    Either<Throwable, Optional<SleepQualityAnswer>> find(Long id);

    Either<Throwable, Optional<SleepQualityAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SleepQualityAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SleepQualityAnswer> update(CategorizedAnswer categorizedAnswer);

}