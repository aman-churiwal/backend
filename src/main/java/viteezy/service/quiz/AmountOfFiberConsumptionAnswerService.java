package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfFiberConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfFiberConsumptionAnswerService {

    Either<Throwable, Optional<AmountOfFiberConsumptionAnswer>> find(Long id);

    Either<Throwable, Optional<AmountOfFiberConsumptionAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfFiberConsumptionAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfFiberConsumptionAnswer> update(CategorizedAnswer categorizedAnswer);

}