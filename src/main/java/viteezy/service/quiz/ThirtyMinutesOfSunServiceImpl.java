package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.ThirtyMinutesOfSunRepository;
import viteezy.domain.quiz.ThirtyMinutesOfSun;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ThirtyMinutesOfSunServiceImpl implements ThirtyMinutesOfSunService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirtyMinutesOfSunService.class);
    private final ThirtyMinutesOfSunRepository thirtyMinutesOfSunRepository;

    public ThirtyMinutesOfSunServiceImpl(ThirtyMinutesOfSunRepository thirtyMinutesOfSunRepository) {
        this.thirtyMinutesOfSunRepository = thirtyMinutesOfSunRepository;
    }

    @Override
    public Either<Throwable, Optional<ThirtyMinutesOfSun>> find(Long id) {
        return thirtyMinutesOfSunRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<ThirtyMinutesOfSun>> findAll() {
        return thirtyMinutesOfSunRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, ThirtyMinutesOfSun> save(ThirtyMinutesOfSun thirtyMinutesOfSun) {
        return thirtyMinutesOfSunRepository
                .save(thirtyMinutesOfSun)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, ThirtyMinutesOfSun>> retrieveById() {
        return id -> {
            Try<Optional<ThirtyMinutesOfSun>> optionalTry = thirtyMinutesOfSunRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "ThirtyMinutesOfSun entity was saved but could not be retrieved from db";
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