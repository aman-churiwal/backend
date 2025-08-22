package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SportReasonAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SportReasonAnswerRepository {

    Try<Optional<SportReasonAnswer>> find(Long id);

    Try<Optional<SportReasonAnswer>> find(UUID quizExternalReference);

    Try<Long> save(SportReasonAnswer sportReasonAnswer);

    Try<Long> update(SportReasonAnswer sportReasonAnswer);
}