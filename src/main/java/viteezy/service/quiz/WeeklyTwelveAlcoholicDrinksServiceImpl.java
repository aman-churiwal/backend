package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.WeeklyTwelveAlcoholicDrinksRepository;
import viteezy.domain.quiz.WeeklyTwelveAlcoholicDrinks;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class WeeklyTwelveAlcoholicDrinksServiceImpl implements WeeklyTwelveAlcoholicDrinksService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeeklyTwelveAlcoholicDrinksService.class);
    private final WeeklyTwelveAlcoholicDrinksRepository weeklyTwelveAlcoholicDrinksRepository;

    public WeeklyTwelveAlcoholicDrinksServiceImpl(WeeklyTwelveAlcoholicDrinksRepository weeklyTwelveAlcoholicDrinksRepository) {
        this.weeklyTwelveAlcoholicDrinksRepository = weeklyTwelveAlcoholicDrinksRepository;
    }

    @Override
    public Either<Throwable, Optional<WeeklyTwelveAlcoholicDrinks>> find(Long id) {
        return weeklyTwelveAlcoholicDrinksRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<WeeklyTwelveAlcoholicDrinks>> findAll() {
        return weeklyTwelveAlcoholicDrinksRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, WeeklyTwelveAlcoholicDrinks> save(WeeklyTwelveAlcoholicDrinks weeklyTwelveAlcoholicDrinks) {
        return weeklyTwelveAlcoholicDrinksRepository
                .save(weeklyTwelveAlcoholicDrinks)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, WeeklyTwelveAlcoholicDrinks>> retrieveById() {
        return id -> {
            Try<Optional<WeeklyTwelveAlcoholicDrinks>> optionalTry = weeklyTwelveAlcoholicDrinksRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "WeeklyTwelveAlcoholicDrinks entity was saved but could not be retrieved from db";
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