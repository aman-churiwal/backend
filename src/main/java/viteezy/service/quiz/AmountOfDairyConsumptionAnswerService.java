package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfDairyConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfDairyConsumptionAnswerService {

    Either<Throwable, Optional<AmountOfDairyConsumptionAnswer>> find(Long id);

    Either<Throwable, Optional<AmountOfDairyConsumptionAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfDairyConsumptionAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfDairyConsumptionAnswer> update(CategorizedAnswer categorizedAnswer);

}