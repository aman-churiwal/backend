package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DailyFourCoffee;

import java.util.List;
import java.util.Optional;

public interface DailyFourCoffeeRepository {

    Try<Optional<DailyFourCoffee>> find(Long id);

    Try<List<DailyFourCoffee>> findAll();

    Try<Long> save(DailyFourCoffee dailyFourCoffee);

}