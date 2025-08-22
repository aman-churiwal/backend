package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.NameAnswer;

import java.util.Optional;
import java.util.UUID;

public interface NameAnswerRepository {

    Try<Optional<NameAnswer>> find(Long id);

    Try<Optional<NameAnswer>> find(UUID quizExternalReference);

    Try<Long> save(NameAnswer nameAnswer);

    Try<Long> update(NameAnswer nameAnswer);
}