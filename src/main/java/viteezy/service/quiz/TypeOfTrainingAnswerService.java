package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.TypeOfTrainingAnswer;

import java.util.Optional;
import java.util.UUID;

public interface TypeOfTrainingAnswerService {

    Either<Throwable, Optional<TypeOfTrainingAnswer>> find(Long id);

    Either<Throwable, Optional<TypeOfTrainingAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TypeOfTrainingAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TypeOfTrainingAnswer> update(CategorizedAnswer categorizedAnswer);

}