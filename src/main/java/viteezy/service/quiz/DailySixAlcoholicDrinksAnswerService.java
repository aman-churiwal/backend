package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DailySixAlcoholicDrinksAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DailySixAlcoholicDrinksAnswerService {

    Either<Throwable, Optional<DailySixAlcoholicDrinksAnswer>> find(Long id);

    Either<Throwable, Optional<DailySixAlcoholicDrinksAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DailySixAlcoholicDrinksAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DailySixAlcoholicDrinksAnswer> update(CategorizedAnswer categorizedAnswer);

}