package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.MentalFitnessAnswer;

import java.util.Optional;
import java.util.UUID;

public interface MentalFitnessAnswerService {

    Either<Throwable, Optional<MentalFitnessAnswer>> find(Long id);

    Either<Throwable, Optional<MentalFitnessAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MentalFitnessAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MentalFitnessAnswer> update(CategorizedAnswer categorizedAnswer);

}