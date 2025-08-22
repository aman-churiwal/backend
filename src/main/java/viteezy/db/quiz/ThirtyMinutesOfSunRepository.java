package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.ThirtyMinutesOfSun;

import java.util.List;
import java.util.Optional;

public interface ThirtyMinutesOfSunRepository {

    Try<Optional<ThirtyMinutesOfSun>> find(Long id);

    Try<List<ThirtyMinutesOfSun>> findAll();

    Try<Long> save(ThirtyMinutesOfSun thirtyMinutesOfSun);

}