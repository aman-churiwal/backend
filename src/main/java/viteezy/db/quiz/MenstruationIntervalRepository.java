package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.MenstruationInterval;

import java.util.List;
import java.util.Optional;

public interface MenstruationIntervalRepository {

    Try<Optional<MenstruationInterval>> find(Long id);

    Try<List<MenstruationInterval>> findAll();

    Try<Long> save(MenstruationInterval menstruationInterval);

}