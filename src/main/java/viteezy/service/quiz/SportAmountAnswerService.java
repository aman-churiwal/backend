package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.SportAmountAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SportAmountAnswerService {

    Either<Throwable, Optional<SportAmountAnswer>> find(Long id);

    Either<Throwable, Optional<SportAmountAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SportAmountAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SportAmountAnswer> update(CategorizedAnswer categorizedAnswer);

}