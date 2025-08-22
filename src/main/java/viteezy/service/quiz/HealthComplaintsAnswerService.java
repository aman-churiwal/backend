package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.HealthComplaintsAnswer;

import java.util.Optional;
import java.util.UUID;

public interface HealthComplaintsAnswerService {

    Either<Throwable, Optional<HealthComplaintsAnswer>> find(Long id);

    Either<Throwable, Optional<HealthComplaintsAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, HealthComplaintsAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, HealthComplaintsAnswer> update(CategorizedAnswer categorizedAnswer);

}