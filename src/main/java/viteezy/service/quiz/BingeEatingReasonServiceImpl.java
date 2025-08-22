package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.BingeEatingReasonRepository;
import viteezy.domain.quiz.BingeEatingReason;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class BingeEatingReasonServiceImpl implements BingeEatingReasonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BingeEatingReasonService.class);
    private final BingeEatingReasonRepository bingeEatingReasonRepository;

    public BingeEatingReasonServiceImpl(BingeEatingReasonRepository bingeEatingReasonRepository) {
        this.bingeEatingReasonRepository = bingeEatingReasonRepository;
    }

    @Override
    public Either<Throwable, Optional<BingeEatingReason>> find(Long id) {
        return bingeEatingReasonRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<BingeEatingReason>> findAll() {
        return bingeEatingReasonRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, BingeEatingReason> save(BingeEatingReason bingeEatingReason) {
        return bingeEatingReasonRepository
                .save(bingeEatingReason)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, BingeEatingReason>> retrieveById() {
        return id -> {
            Try<Optional<BingeEatingReason>> optionalTry = bingeEatingReasonRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "BingeEatingReason entity was saved but could not be retrieved from db";
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