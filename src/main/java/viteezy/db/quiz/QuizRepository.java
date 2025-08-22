package viteezy.db.quiz;

import io.vavr.control.Try;
import org.springframework.lang.NonNull;
import viteezy.domain.quiz.Quiz;

import java.util.Optional;
import java.util.UUID;

public interface QuizRepository {

    Try<Quiz> find(@NonNull Long id);

    Try<Optional<Quiz>> findOptional(Long id);

    Try<Quiz> find(UUID externalReference);

    Try<Optional<Quiz>> findByCustomerId(Long customerId);

    Try<Quiz> save(Quiz quiz);

    Try<Quiz> update(Quiz quiz);
}
