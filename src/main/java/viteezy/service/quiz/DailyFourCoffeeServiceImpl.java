package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DailyFourCoffeeRepository;
import viteezy.domain.quiz.DailyFourCoffee;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DailyFourCoffeeServiceImpl implements DailyFourCoffeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyFourCoffeeService.class);
    private final DailyFourCoffeeRepository dailyFourCoffeeRepository;

    public DailyFourCoffeeServiceImpl(DailyFourCoffeeRepository dailyFourCoffeeRepository) {
        this.dailyFourCoffeeRepository = dailyFourCoffeeRepository;
    }

    @Override
    public Either<Throwable, Optional<DailyFourCoffee>> find(Long id) {
        return dailyFourCoffeeRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<DailyFourCoffee>> findAll() {
        return dailyFourCoffeeRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, DailyFourCoffee> save(DailyFourCoffee dailyFourCoffee) {
        return dailyFourCoffeeRepository
                .save(dailyFourCoffee)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, DailyFourCoffee>> retrieveById() {
        return id -> {
            Try<Optional<DailyFourCoffee>> optionalTry = dailyFourCoffeeRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DailyFourCoffee entity was saved but could not be retrieved from db";
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