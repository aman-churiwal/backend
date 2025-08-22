package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.LoseWeightChallenge;

import java.util.List;
import java.util.Optional;

public interface LoseWeightChallengeRepository {

    Try<Optional<LoseWeightChallenge>> find(Long id);

    Try<List<LoseWeightChallenge>> findAll();

    Try<Long> save(LoseWeightChallenge loseWeightChallenge);

}