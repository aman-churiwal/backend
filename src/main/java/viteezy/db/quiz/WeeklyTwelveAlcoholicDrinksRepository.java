package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.WeeklyTwelveAlcoholicDrinks;

import java.util.List;
import java.util.Optional;

public interface WeeklyTwelveAlcoholicDrinksRepository {

    Try<Optional<WeeklyTwelveAlcoholicDrinks>> find(Long id);

    Try<List<WeeklyTwelveAlcoholicDrinks>> findAll();

    Try<Long> save(WeeklyTwelveAlcoholicDrinks weeklyTwelveAlcoholicDrinks);

}