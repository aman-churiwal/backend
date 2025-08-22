package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.NewProductAvailableAnswer;

import java.util.Optional;
import java.util.UUID;

public interface NewProductAvailableAnswerService {

    Either<Throwable, Optional<NewProductAvailableAnswer>> find(Long id);

    Either<Throwable, Optional<NewProductAvailableAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, NewProductAvailableAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, NewProductAvailableAnswer> update(CategorizedAnswer categorizedAnswer);

}