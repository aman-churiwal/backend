package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfProteinConsumptionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AmountOfProteinConsumptionAnswerService {

    Either<Throwable, Optional<AmountOfProteinConsumptionAnswer>> find(Long id);

    Either<Throwable, Optional<AmountOfProteinConsumptionAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfProteinConsumptionAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfProteinConsumptionAnswer> update(CategorizedAnswer categorizedAnswer);

}