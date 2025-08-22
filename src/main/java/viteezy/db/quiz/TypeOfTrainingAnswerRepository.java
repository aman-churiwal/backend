package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.TypeOfTrainingAnswer;

import java.util.Optional;
import java.util.UUID;

public interface TypeOfTrainingAnswerRepository {

    Try<Optional<TypeOfTrainingAnswer>> find(Long id);

    Try<Optional<TypeOfTrainingAnswer>> find(UUID quizExternalReference);

    Try<Long> save(TypeOfTrainingAnswer typeOfTrainingAnswer);

    Try<Long> update(TypeOfTrainingAnswer typeOfTrainingAnswer);
}