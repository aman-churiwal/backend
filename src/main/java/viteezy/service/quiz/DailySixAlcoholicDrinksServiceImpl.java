package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DailySixAlcoholicDrinksRepository;
import viteezy.domain.quiz.DailySixAlcoholicDrinks;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DailySixAlcoholicDrinksServiceImpl implements DailySixAlcoholicDrinksService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailySixAlcoholicDrinksService.class);
    private final DailySixAlcoholicDrinksRepository dailySixAlcoholicDrinksRepository;

    public DailySixAlcoholicDrinksServiceImpl(DailySixAlcoholicDrinksRepository dailySixAlcoholicDrinksRepository) {
        this.dailySixAlcoholicDrinksRepository = dailySixAlcoholicDrinksRepository;
    }

    @Override
    public Either<Throwable, Optional<DailySixAlcoholicDrinks>> find(Long id) {
        return dailySixAlcoholicDrinksRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<DailySixAlcoholicDrinks>> findAll() {
        return dailySixAlcoholicDrinksRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, DailySixAlcoholicDrinks> save(DailySixAlcoholicDrinks dailySixAlcoholicDrinks) {
        return dailySixAlcoholicDrinksRepository
                .save(dailySixAlcoholicDrinks)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, DailySixAlcoholicDrinks>> retrieveById() {
        return id -> {
            Try<Optional<DailySixAlcoholicDrinks>> optionalTry = dailySixAlcoholicDrinksRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DailySixAlcoholicDrinks entity was saved but could not be retrieved from db";
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