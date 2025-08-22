package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DietIntoleranceAnswer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DietIntoleranceAnswerService {

    Either<Throwable, Optional<DietIntoleranceAnswer>> find(Long id);

    Either<Throwable, List<DietIntoleranceAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DietIntoleranceAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, Void> delete(CategorizedAnswer categorizedAnswer);

}