package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.WeeklyTwelveAlcoholicDrinksAnswer;

import java.util.Optional;
import java.util.UUID;

public interface WeeklyTwelveAlcoholicDrinksAnswerService {

    Either<Throwable, Optional<WeeklyTwelveAlcoholicDrinksAnswer>> find(Long id);

    Either<Throwable, Optional<WeeklyTwelveAlcoholicDrinksAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, WeeklyTwelveAlcoholicDrinksAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, WeeklyTwelveAlcoholicDrinksAnswer> update(CategorizedAnswer categorizedAnswer);

}