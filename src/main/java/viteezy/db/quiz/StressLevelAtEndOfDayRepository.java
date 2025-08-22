package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.StressLevelAtEndOfDay;

import java.util.List;
import java.util.Optional;

public interface StressLevelAtEndOfDayRepository {

    Try<Optional<StressLevelAtEndOfDay>> find(Long id);

    Try<List<StressLevelAtEndOfDay>> findAll();

    Try<Long> save(StressLevelAtEndOfDay stressLevelAtEndOfDay);

}