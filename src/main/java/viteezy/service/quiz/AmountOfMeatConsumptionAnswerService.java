package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfMeatConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfMeatConsumptionAnswerService {

    Either<Throwable, Optional<AmountOfMeatConsumptionAnswer>> find(Long id);

    Either<Throwable, Optional<AmountOfMeatConsumptionAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfMeatConsumptionAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfMeatConsumptionAnswer> update(CategorizedAnswer categorizedAnswer);

}