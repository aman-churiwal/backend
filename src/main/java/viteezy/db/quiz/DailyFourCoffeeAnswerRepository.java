package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DailyFourCoffeeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DailyFourCoffeeAnswerRepository {

    Try<Optional<DailyFourCoffeeAnswer>> find(Long id);

    Try<Optional<DailyFourCoffeeAnswer>> find(UUID quizExternalReference);

    Try<Long> save(DailyFourCoffeeAnswer dailyFourCoffeeAnswer);

    Try<Long> update(DailyFourCoffeeAnswer dailyFourCoffeeAnswer);
}