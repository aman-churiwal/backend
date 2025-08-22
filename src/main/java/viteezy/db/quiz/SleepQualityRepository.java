package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SleepQuality;

import java.util.List;
import java.util.Optional;

public interface SleepQualityRepository {

    Try<Optional<SleepQuality>> find(Long id);

    Try<List<SleepQuality>> findAll();

    Try<Long> save(SleepQuality sleepQuality);

}