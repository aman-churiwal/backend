package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DailyFourCoffeeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DailyFourCoffeeAnswerService {

    Either<Throwable, Optional<DailyFourCoffeeAnswer>> find(Long id);

    Either<Throwable, Optional<DailyFourCoffeeAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DailyFourCoffeeAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DailyFourCoffeeAnswer> update(CategorizedAnswer categorizedAnswer);

}