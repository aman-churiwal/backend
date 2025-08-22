package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.EnergyStateRepository;
import viteezy.domain.quiz.EnergyState;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class EnergyStateServiceImpl implements EnergyStateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyStateService.class);
    private final EnergyStateRepository energyStateRepository;

    public EnergyStateServiceImpl(EnergyStateRepository energyStateRepository) {
        this.energyStateRepository = energyStateRepository;
    }

    @Override
    public Either<Throwable, Optional<EnergyState>> find(Long id) {
        return energyStateRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<EnergyState>> findAll() {
        return energyStateRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, EnergyState> save(EnergyState energyState) {
        return energyStateRepository
                .save(energyState)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, EnergyState>> retrieveById() {
        return id -> {
            Try<Optional<EnergyState>> optionalTry = energyStateRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "EnergyState entity was saved but could not be retrieved from db";
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