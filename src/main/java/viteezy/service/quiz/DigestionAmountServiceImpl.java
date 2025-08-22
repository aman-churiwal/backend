package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DigestionAmountRepository;
import viteezy.domain.quiz.DigestionAmount;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DigestionAmountServiceImpl implements DigestionAmountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigestionAmountService.class);
    private final DigestionAmountRepository digestionAmountRepository;

    public DigestionAmountServiceImpl(DigestionAmountRepository digestionAmountRepository) {
        this.digestionAmountRepository = digestionAmountRepository;
    }

    @Override
    public Either<Throwable, Optional<DigestionAmount>> find(Long id) {
        return digestionAmountRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<DigestionAmount>> findAll() {
        return digestionAmountRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, DigestionAmount> save(DigestionAmount digestionAmount) {
        return digestionAmountRepository
                .save(digestionAmount)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, DigestionAmount>> retrieveById() {
        return id -> {
            Try<Optional<DigestionAmount>> optionalTry = digestionAmountRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DigestionAmount entity was saved but could not be retrieved from db";
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