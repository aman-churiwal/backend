package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DigestionOccurrenceAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DigestionOccurrenceAnswerService {

    Either<Throwable, Optional<DigestionOccurrenceAnswer>> find(Long id);

    Either<Throwable, Optional<DigestionOccurrenceAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DigestionOccurrenceAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DigestionOccurrenceAnswer> update(CategorizedAnswer categorizedAnswer);

}