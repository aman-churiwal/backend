package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DateOfBirthAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DateOfBirthAnswerRepository {

    Try<Optional<DateOfBirthAnswer>> find(Long id);

    Try<Optional<DateOfBirthAnswer>> find(UUID quizExternalReference);

    Try<Long> save(DateOfBirthAnswer dateOfBirthAnswer);

    Try<Long> update(DateOfBirthAnswer dateOfBirthAnswer);
}