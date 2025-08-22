package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.BirthHealthAnswer;

import java.util.Optional;
import java.util.UUID;

public interface BirthHealthAnswerRepository {

    Try<Optional<BirthHealthAnswer>> find(Long id);

    Try<Optional<BirthHealthAnswer>> find(UUID quizExternalReference);

    Try<Long> save(BirthHealthAnswer birthHealthAnswer);

    Try<Long> update(BirthHealthAnswer birthHealthAnswer);
}