package viteezy.service;

import com.google.common.base.Throwables;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.CustomerRepository;
import viteezy.db.LoggingRepository;
import viteezy.domain.Logging;
import viteezy.domain.LoggingEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class LoggingServiceImpl implements LoggingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingService.class);

    private final CustomerRepository customerRepository;
    private final LoggingRepository loggingRepository;

    protected LoggingServiceImpl(CustomerRepository customerRepository, LoggingRepository loggingRepository) {
        this.customerRepository = customerRepository;
        this.loggingRepository = loggingRepository;
    }

    @Override
    public Try<List<Logging>> findAll(UUID customerExternalReference) {
        return customerRepository.find(customerExternalReference)
                .flatMap(customer -> loggingRepository.findAll(customer.getId()))
                .onFailure(peekException());
    }

    @Override
    public Try<Logging> create(Long customerId, LoggingEvent loggingEvent, String info) {
        return loggingRepository.create(buildLogging(customerId, loggingEvent, info));
    }

    private Logging buildLogging(Long customerId, LoggingEvent loggingEvent, String info) {
        return new Logging(null, customerId, loggingEvent, info, LocalDateTime.now());
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
