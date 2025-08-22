package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.GenderAnswer;

import java.util.Optional;
import java.util.UUID;

public interface GenderAnswerRepository {

    Try<Optional<GenderAnswer>> find(Long id);

    Try<Optional<GenderAnswer>> find(UUID quizExternalReference);

    Try<Long> save(GenderAnswer genderAnswer);

    Try<Long> update(GenderAnswer genderAnswer);
}