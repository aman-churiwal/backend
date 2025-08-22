package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.SkinProblemAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SkinProblemAnswerService {

    Either<Throwable, Optional<SkinProblemAnswer>> find(Long id);

    Either<Throwable, Optional<SkinProblemAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SkinProblemAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SkinProblemAnswer> update(CategorizedAnswer categorizedAnswer);

}