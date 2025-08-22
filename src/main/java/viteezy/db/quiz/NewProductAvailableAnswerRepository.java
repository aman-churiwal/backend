package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.NewProductAvailableAnswer;

import java.util.Optional;
import java.util.UUID;

public interface NewProductAvailableAnswerRepository {

    Try<Optional<NewProductAvailableAnswer>> find(Long id);

    Try<Optional<NewProductAvailableAnswer>> find(UUID quizExternalReference);

    Try<Long> save(NewProductAvailableAnswer newProductAvailableAnswer);

    Try<Long> update(NewProductAvailableAnswer newProductAvailableAnswer);
}