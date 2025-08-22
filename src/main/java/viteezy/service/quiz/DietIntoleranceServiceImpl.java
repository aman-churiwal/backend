package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DietIntoleranceRepository;
import viteezy.domain.quiz.DietIntolerance;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DietIntoleranceServiceImpl implements DietIntoleranceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DietIntoleranceService.class);
    private final DietIntoleranceRepository dietIntoleranceRepository;

    public DietIntoleranceServiceImpl(DietIntoleranceRepository dietIntoleranceRepository) {
        this.dietIntoleranceRepository = dietIntoleranceRepository;
    }

    @Override
    public Either<Throwable, Optional<DietIntolerance>> find(Long id) {
        return dietIntoleranceRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<DietIntolerance>> findAll() {
        return dietIntoleranceRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, DietIntolerance> save(DietIntolerance dietIntolerance) {
        return dietIntoleranceRepository
                .save(dietIntolerance)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, DietIntolerance>> retrieveById() {
        return id -> {
            Try<Optional<DietIntolerance>> optionalTry = dietIntoleranceRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DietIntolerance entity was saved but could not be retrieved from db";
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