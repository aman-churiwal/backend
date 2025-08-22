package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.NailImprovementAnswer;

import java.util.Optional;
import java.util.UUID;

public interface NailImprovementAnswerService {

    Either<Throwable, Optional<NailImprovementAnswer>> find(Long id);

    Either<Throwable, Optional<NailImprovementAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, NailImprovementAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, NailImprovementAnswer> update(CategorizedAnswer categorizedAnswer);

}