package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AcnePlaceAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AcnePlaceAnswerRepository {

    Try<Optional<AcnePlaceAnswer>> find(Long id);

    Try<Optional<AcnePlaceAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AcnePlaceAnswer acnePlaceAnswer);

    Try<Long> update(AcnePlaceAnswer acnePlaceAnswer);
}