package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.TroubleFallingAsleepRepository;
import viteezy.domain.quiz.TroubleFallingAsleep;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class TroubleFallingAsleepServiceImpl implements TroubleFallingAsleepService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TroubleFallingAsleepService.class);
    private final TroubleFallingAsleepRepository troubleFallingAsleepRepository;

    public TroubleFallingAsleepServiceImpl(TroubleFallingAsleepRepository troubleFallingAsleepRepository) {
        this.troubleFallingAsleepRepository = troubleFallingAsleepRepository;
    }

    @Override
    public Either<Throwable, Optional<TroubleFallingAsleep>> find(Long id) {
        return troubleFallingAsleepRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<TroubleFallingAsleep>> findAll() {
        return troubleFallingAsleepRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, TroubleFallingAsleep> save(TroubleFallingAsleep troubleFallingAsleep) {
        return troubleFallingAsleepRepository
                .save(troubleFallingAsleep)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, TroubleFallingAsleep>> retrieveById() {
        return id -> {
            Try<Optional<TroubleFallingAsleep>> optionalTry = troubleFallingAsleepRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "TroubleFallingAsleep entity was saved but could not be retrieved from db";
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