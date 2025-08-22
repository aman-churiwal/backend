package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DailySixAlcoholicDrinksAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DailySixAlcoholicDrinksAnswerRepository {

    Try<Optional<DailySixAlcoholicDrinksAnswer>> find(Long id);

    Try<Optional<DailySixAlcoholicDrinksAnswer>> find(UUID quizExternalReference);

    Try<Long> save(DailySixAlcoholicDrinksAnswer dailySixAlcoholicDrinksAnswer);

    Try<Long> update(DailySixAlcoholicDrinksAnswer dailySixAlcoholicDrinksAnswer);
}