package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.HealthyLifestyleRepository;
import viteezy.domain.quiz.HealthyLifestyle;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class HealthyLifestyleServiceImpl implements HealthyLifestyleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthyLifestyleService.class);
    private final HealthyLifestyleRepository healthyLifestyleRepository;

    public HealthyLifestyleServiceImpl(HealthyLifestyleRepository healthyLifestyleRepository) {
        this.healthyLifestyleRepository = healthyLifestyleRepository;
    }

    @Override
    public Either<Throwable, Optional<HealthyLifestyle>> find(Long id) {
        return healthyLifestyleRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<HealthyLifestyle>> findAll() {
        return healthyLifestyleRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, HealthyLifestyle> save(HealthyLifestyle healthyLifestyle) {
        return healthyLifestyleRepository
                .save(healthyLifestyle)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, HealthyLifestyle>> retrieveById() {
        return id -> {
            Try<Optional<HealthyLifestyle>> optionalTry = healthyLifestyleRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "HealthyLifestyle entity was saved but could not be retrieved from db";
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