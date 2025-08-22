package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.StressLevel;

import java.util.List;
import java.util.Optional;

public interface StressLevelRepository {

    Try<Optional<StressLevel>> find(Long id);

    Try<List<StressLevel>> findAll();

    Try<Long> save(StressLevel stressLevel);

}