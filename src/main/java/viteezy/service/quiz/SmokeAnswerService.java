package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.SmokeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SmokeAnswerService {

    Either<Throwable, Optional<SmokeAnswer>> find(Long id);

    Either<Throwable, Optional<SmokeAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SmokeAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SmokeAnswer> update(CategorizedAnswer categorizedAnswer);

}