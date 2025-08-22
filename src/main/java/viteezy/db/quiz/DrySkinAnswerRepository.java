package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DrySkinAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DrySkinAnswerRepository {

    Try<Optional<DrySkinAnswer>> find(Long id);

    Try<Optional<DrySkinAnswer>> find(UUID quizExternalReference);

    Try<Long> save(DrySkinAnswer drySkinAnswer);

    Try<Long> update(DrySkinAnswer drySkinAnswer);
}