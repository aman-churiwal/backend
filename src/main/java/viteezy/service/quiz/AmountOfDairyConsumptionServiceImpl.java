package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AmountOfDairyConsumptionRepository;
import viteezy.domain.quiz.AmountOfDairyConsumption;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class AmountOfDairyConsumptionServiceImpl implements AmountOfDairyConsumptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmountOfDairyConsumptionService.class);
    private final AmountOfDairyConsumptionRepository amountOfDairyConsumptionRepository;

    public AmountOfDairyConsumptionServiceImpl(AmountOfDairyConsumptionRepository amountOfDairyConsumptionRepository) {
        this.amountOfDairyConsumptionRepository = amountOfDairyConsumptionRepository;
    }

    @Override
    public Either<Throwable, Optional<AmountOfDairyConsumption>> find(Long id) {
        return amountOfDairyConsumptionRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<AmountOfDairyConsumption>> findAll() {
        return amountOfDairyConsumptionRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, AmountOfDairyConsumption> save(AmountOfDairyConsumption amountOfDairyConsumption) {
        return amountOfDairyConsumptionRepository
                .save(amountOfDairyConsumption)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, AmountOfDairyConsumption>> retrieveById() {
        return id -> {
            Try<Optional<AmountOfDairyConsumption>> optionalTry = amountOfDairyConsumptionRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AmountOfDairyConsumption entity was saved but could not be retrieved from db";
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