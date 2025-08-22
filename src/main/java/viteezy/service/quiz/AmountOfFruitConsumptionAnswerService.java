package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfFruitConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfFruitConsumptionAnswerService {

    Either<Throwable, Optional<AmountOfFruitConsumptionAnswer>> find(Long id);

    Either<Throwable, Optional<AmountOfFruitConsumptionAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfFruitConsumptionAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfFruitConsumptionAnswer> update(CategorizedAnswer categorizedAnswer);

}