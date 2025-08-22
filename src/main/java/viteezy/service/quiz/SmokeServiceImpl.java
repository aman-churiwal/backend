package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SmokeRepository;
import viteezy.domain.quiz.Smoke;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SmokeServiceImpl implements SmokeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmokeService.class);
    private final SmokeRepository smokeRepository;

    public SmokeServiceImpl(SmokeRepository smokeRepository) {
        this.smokeRepository = smokeRepository;
    }

    @Override
    public Either<Throwable, Optional<Smoke>> find(Long id) {
        return smokeRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<Smoke>> findAll() {
        return smokeRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, Smoke> save(Smoke smoke) {
        return smokeRepository
                .save(smoke)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, Smoke>> retrieveById() {
        return id -> {
            Try<Optional<Smoke>> optionalTry = smokeRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "Smoke entity was saved but could not be retrieved from db";
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