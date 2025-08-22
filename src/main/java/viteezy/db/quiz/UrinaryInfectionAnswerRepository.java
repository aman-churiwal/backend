package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.UrinaryInfectionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface UrinaryInfectionAnswerRepository {

    Try<Optional<UrinaryInfectionAnswer>> find(Long id);

    Try<Optional<UrinaryInfectionAnswer>> find(UUID quizExternalReference);

    Try<Long> save(UrinaryInfectionAnswer urinaryInfectionAnswer);

    Try<Long> update(UrinaryInfectionAnswer urinaryInfectionAnswer);
}