package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.UrinaryInfectionRepository;
import viteezy.domain.quiz.UrinaryInfection;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class UrinaryInfectionServiceImpl implements UrinaryInfectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrinaryInfectionService.class);
    private final UrinaryInfectionRepository urinaryInfectionRepository;

    public UrinaryInfectionServiceImpl(UrinaryInfectionRepository urinaryInfectionRepository) {
        this.urinaryInfectionRepository = urinaryInfectionRepository;
    }

    @Override
    public Either<Throwable, Optional<UrinaryInfection>> find(Long id) {
        return urinaryInfectionRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<UrinaryInfection>> findAll() {
        return urinaryInfectionRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, UrinaryInfection> save(UrinaryInfection urinaryInfection) {
        return urinaryInfectionRepository
                .save(urinaryInfection)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, UrinaryInfection>> retrieveById() {
        return id -> {
            Try<Optional<UrinaryInfection>> optionalTry = urinaryInfectionRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "UrinaryInfection entity was saved but could not be retrieved from db";
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