package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.Logging;

import java.util.List;

public interface LoggingRepository {

    Try<Logging> find(Long id);

    Try<List<Logging>> findAll(Long id);

    Try<Logging> create(Logging logging);
}
