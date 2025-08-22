package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.UrinaryInfection;

import java.util.List;
import java.util.Optional;

public interface UrinaryInfectionRepository {

    Try<Optional<UrinaryInfection>> find(Long id);

    Try<List<UrinaryInfection>> findAll();

    Try<Long> save(UrinaryInfection urinaryInfection);

}