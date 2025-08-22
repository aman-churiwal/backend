package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.NewProductAvailableRepository;
import viteezy.domain.quiz.NewProductAvailable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class NewProductAvailableServiceImpl implements NewProductAvailableService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewProductAvailableService.class);
    private final NewProductAvailableRepository newProductAvailableRepository;

    public NewProductAvailableServiceImpl(NewProductAvailableRepository newProductAvailableRepository) {
        this.newProductAvailableRepository = newProductAvailableRepository;
    }

    @Override
    public Either<Throwable, Optional<NewProductAvailable>> find(Long id) {
        return newProductAvailableRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<NewProductAvailable>> findAll() {
        return newProductAvailableRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, NewProductAvailable> save(NewProductAvailable newProductAvailable) {
        return newProductAvailableRepository
                .save(newProductAvailable)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, NewProductAvailable>> retrieveById() {
        return id -> {
            Try<Optional<NewProductAvailable>> optionalTry = newProductAvailableRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "NewProductAvailable entity was saved but could not be retrieved from db";
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