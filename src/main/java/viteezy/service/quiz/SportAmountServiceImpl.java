package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SportAmountRepository;
import viteezy.domain.quiz.SportAmount;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SportAmountServiceImpl implements SportAmountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportAmountService.class);
    private final SportAmountRepository sportAmountRepository;

    public SportAmountServiceImpl(SportAmountRepository sportAmountRepository) {
        this.sportAmountRepository = sportAmountRepository;
    }

    @Override
    public Either<Throwable, Optional<SportAmount>> find(Long id) {
        return sportAmountRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<SportAmount>> findAll() {
        return sportAmountRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, SportAmount> save(SportAmount sportAmount) {
        return sportAmountRepository
                .save(sportAmount)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, SportAmount>> retrieveById() {
        return id -> {
            Try<Optional<SportAmount>> optionalTry = sportAmountRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "SportAmount entity was saved but could not be retrieved from db";
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