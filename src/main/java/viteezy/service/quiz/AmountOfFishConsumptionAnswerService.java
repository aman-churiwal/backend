package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfFishConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfFishConsumptionAnswerService {

    Either<Throwable, Optional<AmountOfFishConsumptionAnswer>> find(Long id);

    Either<Throwable, Optional<AmountOfFishConsumptionAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfFishConsumptionAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfFishConsumptionAnswer> update(CategorizedAnswer categorizedAnswer);

}