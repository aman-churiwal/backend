package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.ThirtyMinutesOfSunAnswer;

import java.util.Optional;
import java.util.UUID;

public interface ThirtyMinutesOfSunAnswerRepository {

    Try<Optional<ThirtyMinutesOfSunAnswer>> find(Long id);

    Try<Optional<ThirtyMinutesOfSunAnswer>> find(UUID quizExternalReference);

    Try<Long> save(ThirtyMinutesOfSunAnswer thirtyMinutesOfSunAnswer);

    Try<Long> update(ThirtyMinutesOfSunAnswer thirtyMinutesOfSunAnswer);
}