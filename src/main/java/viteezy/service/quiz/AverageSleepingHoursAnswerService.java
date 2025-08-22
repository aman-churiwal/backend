package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AverageSleepingHoursAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AverageSleepingHoursAnswerService {

    Either<Throwable, Optional<AverageSleepingHoursAnswer>> find(Long id);

    Either<Throwable, Optional<AverageSleepingHoursAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AverageSleepingHoursAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AverageSleepingHoursAnswer> update(CategorizedAnswer categorizedAnswer);

}