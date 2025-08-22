package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DrySkinRepository;
import viteezy.domain.quiz.DrySkin;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DrySkinServiceImpl implements DrySkinService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrySkinService.class);
    private final DrySkinRepository drySkinRepository;

    public DrySkinServiceImpl(DrySkinRepository drySkinRepository) {
        this.drySkinRepository = drySkinRepository;
    }

    @Override
    public Either<Throwable, Optional<DrySkin>> find(Long id) {
        return drySkinRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<DrySkin>> findAll() {
        return drySkinRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, DrySkin> save(DrySkin drySkin) {
        return drySkinRepository
                .save(drySkin)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, DrySkin>> retrieveById() {
        return id -> {
            Try<Optional<DrySkin>> optionalTry = drySkinRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DrySkin entity was saved but could not be retrieved from db";
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