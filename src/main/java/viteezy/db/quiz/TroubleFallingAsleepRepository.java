package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.TroubleFallingAsleep;

import java.util.List;
import java.util.Optional;

public interface TroubleFallingAsleepRepository {

    Try<Optional<TroubleFallingAsleep>> find(Long id);

    Try<List<TroubleFallingAsleep>> findAll();

    Try<Long> save(TroubleFallingAsleep troubleFallingAsleep);

}