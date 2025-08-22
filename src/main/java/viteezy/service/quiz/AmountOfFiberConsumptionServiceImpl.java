package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AmountOfFiberConsumptionRepository;
import viteezy.domain.quiz.AmountOfFiberConsumption;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class AmountOfFiberConsumptionServiceImpl implements AmountOfFiberConsumptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmountOfFiberConsumptionService.class);
    private final AmountOfFiberConsumptionRepository amountOfFiberConsumptionRepository;

    public AmountOfFiberConsumptionServiceImpl(AmountOfFiberConsumptionRepository amountOfFiberConsumptionRepository) {
        this.amountOfFiberConsumptionRepository = amountOfFiberConsumptionRepository;
    }

    @Override
    public Either<Throwable, Optional<AmountOfFiberConsumption>> find(Long id) {
        return amountOfFiberConsumptionRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<AmountOfFiberConsumption>> findAll() {
        return amountOfFiberConsumptionRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, AmountOfFiberConsumption> save(AmountOfFiberConsumption amountOfFiberConsumption) {
        return amountOfFiberConsumptionRepository
                .save(amountOfFiberConsumption)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, AmountOfFiberConsumption>> retrieveById() {
        return id -> {
            Try<Optional<AmountOfFiberConsumption>> optionalTry = amountOfFiberConsumptionRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AmountOfFiberConsumption entity was saved but could not be retrieved from db";
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