package viteezy.service;

import io.vavr.control.Try;
import viteezy.domain.Logging;
import viteezy.domain.LoggingEvent;

import java.util.List;
import java.util.UUID;

public interface LoggingService {

    Try<List<Logging>> findAll(UUID customerExternalReference);

    Try<Logging> create(Long customerId, LoggingEvent event, String info);
}
