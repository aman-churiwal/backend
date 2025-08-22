package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DailySixAlcoholicDrinks;

import java.util.List;
import java.util.Optional;

public interface DailySixAlcoholicDrinksRepository {

    Try<Optional<DailySixAlcoholicDrinks>> find(Long id);

    Try<List<DailySixAlcoholicDrinks>> findAll();

    Try<Long> save(DailySixAlcoholicDrinks dailySixAlcoholicDrinks);

}