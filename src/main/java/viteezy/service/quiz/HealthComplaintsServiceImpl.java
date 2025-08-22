package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.HealthComplaintsRepository;
import viteezy.domain.quiz.HealthComplaints;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class HealthComplaintsServiceImpl implements HealthComplaintsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthComplaintsService.class);
    private final HealthComplaintsRepository healthComplaintsRepository;

    public HealthComplaintsServiceImpl(HealthComplaintsRepository healthComplaintsRepository) {
        this.healthComplaintsRepository = healthComplaintsRepository;
    }

    @Override
    public Either<Throwable, Optional<HealthComplaints>> find(Long id) {
        return healthComplaintsRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<HealthComplaints>> findAll() {
        return healthComplaintsRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, HealthComplaints> save(HealthComplaints healthComplaints) {
        return healthComplaintsRepository
                .save(healthComplaints)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, HealthComplaints>> retrieveById() {
        return id -> {
            Try<Optional<HealthComplaints>> optionalTry = healthComplaintsRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "HealthComplaints entity was saved but could not be retrieved from db";
                LOGGER.error("{}", message);
                return Either.left(new NoSuchElementException(message));
            } else {
                LOGGER.error(optionalTry.getCause().toString());
                return Either.left(optionalTry.getCause());
            }
        };
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}