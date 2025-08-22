package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SportAmountAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SportAmountAnswerRepository {

    Try<Optional<SportAmountAnswer>> find(Long id);

    Try<Optional<SportAmountAnswer>> find(UUID quizExternalReference);

    Try<Long> save(SportAmountAnswer sportAmountAnswer);

    Try<Long> update(SportAmountAnswer sportAmountAnswer);
}