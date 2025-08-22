package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.BingeEatingRepository;
import viteezy.domain.quiz.BingeEating;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class BingeEatingServiceImpl implements BingeEatingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BingeEatingService.class);
    private final BingeEatingRepository bingeEatingRepository;

    public BingeEatingServiceImpl(BingeEatingRepository bingeEatingRepository) {
        this.bingeEatingRepository = bingeEatingRepository;
    }

    @Override
    public Either<Throwable, Optional<BingeEating>> find(Long id) {
        return bingeEatingRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<BingeEating>> findAll() {
        return bingeEatingRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, BingeEating> save(BingeEating bingeEating) {
        return bingeEatingRepository
                .save(bingeEating)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, BingeEating>> retrieveById() {
        return id -> {
            Try<Optional<BingeEating>> optionalTry = bingeEatingRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "BingeEating entity was saved but could not be retrieved from db";
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