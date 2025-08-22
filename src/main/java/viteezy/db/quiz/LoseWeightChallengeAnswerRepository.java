package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.LoseWeightChallengeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface LoseWeightChallengeAnswerRepository {

    Try<Optional<LoseWeightChallengeAnswer>> find(Long id);

    Try<Optional<LoseWeightChallengeAnswer>> find(UUID quizExternalReference);

    Try<Long> save(LoseWeightChallengeAnswer loseWeightChallengeAnswer);

    Try<Long> update(LoseWeightChallengeAnswer loseWeightChallengeAnswer);
}