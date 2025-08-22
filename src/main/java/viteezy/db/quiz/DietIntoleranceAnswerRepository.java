package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DietIntoleranceAnswer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DietIntoleranceAnswerRepository {

    Try<Optional<DietIntoleranceAnswer>> find(Long id);

    Try<List<DietIntoleranceAnswer>> find(UUID quizExternalReference);

    Try<Optional<DietIntoleranceAnswer>> find(UUID quizExternalReference, Long dietIntoleranceId);

    Try<Long> save(DietIntoleranceAnswer dietIntoleranceAnswer);

    Try<Void> delete(Long id);
}