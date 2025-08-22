package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.MenstruationMoodAnswer;

import java.util.Optional;
import java.util.UUID;

public interface MenstruationMoodAnswerRepository {

    Try<Optional<MenstruationMoodAnswer>> find(Long id);

    Try<Optional<MenstruationMoodAnswer>> find(UUID quizExternalReference);

    Try<Long> save(MenstruationMoodAnswer menstruationMoodAnswer);

    Try<Long> update(MenstruationMoodAnswer menstruationMoodAnswer);
}