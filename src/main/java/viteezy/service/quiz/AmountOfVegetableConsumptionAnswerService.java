package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfVegetableConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfVegetableConsumptionAnswerService {

    Either<Throwable, Optional<AmountOfVegetableConsumptionAnswer>> find(Long id);

    Either<Throwable, Optional<AmountOfVegetableConsumptionAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfVegetableConsumptionAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfVegetableConsumptionAnswer> update(CategorizedAnswer categorizedAnswer);

}