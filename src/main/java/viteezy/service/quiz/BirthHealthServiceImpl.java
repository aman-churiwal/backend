package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.BirthHealthRepository;
import viteezy.domain.quiz.BirthHealth;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class BirthHealthServiceImpl implements BirthHealthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BirthHealthService.class);
    private final BirthHealthRepository birthHealthRepository;

    public BirthHealthServiceImpl(BirthHealthRepository birthHealthRepository) {
        this.birthHealthRepository = birthHealthRepository;
    }

    @Override
    public Either<Throwable, Optional<BirthHealth>> find(Long id) {
        return birthHealthRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<BirthHealth>> findAll() {
        return birthHealthRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, BirthHealth> save(BirthHealth birthHealth) {
        return birthHealthRepository
                .save(birthHealth)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, BirthHealth>> retrieveById() {
        return id -> {
            Try<Optional<BirthHealth>> optionalTry = birthHealthRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "BirthHealth entity was saved but could not be retrieved from db";
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