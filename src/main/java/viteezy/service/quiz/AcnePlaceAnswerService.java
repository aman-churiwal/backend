package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AcnePlaceAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AcnePlaceAnswerService {

    Either<Throwable, Optional<AcnePlaceAnswer>> find(Long id);

    Either<Throwable, Optional<AcnePlaceAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AcnePlaceAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AcnePlaceAnswer> update(CategorizedAnswer categorizedAnswer);

}