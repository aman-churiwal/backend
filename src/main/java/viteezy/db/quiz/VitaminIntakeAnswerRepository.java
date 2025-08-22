package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.VitaminIntakeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface VitaminIntakeAnswerRepository {

    Try<Optional<VitaminIntakeAnswer>> find(Long id);

    Try<Optional<VitaminIntakeAnswer>> find(UUID quizExternalReference);

    Try<Long> save(VitaminIntakeAnswer vitaminIntakeAnswer);

    Try<Long> update(VitaminIntakeAnswer vitaminIntakeAnswer);
}