package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.WeeklyTwelveAlcoholicDrinksAnswer;

import java.util.Optional;
import java.util.UUID;

public interface WeeklyTwelveAlcoholicDrinksAnswerRepository {

    Try<Optional<WeeklyTwelveAlcoholicDrinksAnswer>> find(Long id);

    Try<Optional<WeeklyTwelveAlcoholicDrinksAnswer>> find(UUID quizExternalReference);

    Try<Long> save(WeeklyTwelveAlcoholicDrinksAnswer weeklyTwelveAlcoholicDrinksAnswer);

    Try<Long> update(WeeklyTwelveAlcoholicDrinksAnswer weeklyTwelveAlcoholicDrinksAnswer);
}