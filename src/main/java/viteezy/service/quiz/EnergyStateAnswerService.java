package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.EnergyStateAnswer;

import java.util.Optional;
import java.util.UUID;

public interface EnergyStateAnswerService {

    Either<Throwable, Optional<EnergyStateAnswer>> find(Long id);

    Either<Throwable, Optional<EnergyStateAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, EnergyStateAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, EnergyStateAnswer> update(CategorizedAnswer categorizedAnswer);

}