package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.PregnancyStateAnswer;

import java.util.Optional;
import java.util.UUID;

public interface PregnancyStateAnswerService {

    Either<Throwable, Optional<PregnancyStateAnswer>> find(Long id);

    Either<Throwable, Optional<PregnancyStateAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, PregnancyStateAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, PregnancyStateAnswer> update(CategorizedAnswer categorizedAnswer);

}