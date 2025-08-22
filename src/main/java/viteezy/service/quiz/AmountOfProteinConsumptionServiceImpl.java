package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AmountOfProteinConsumptionRepository;
import viteezy.domain.quiz.AmountOfProteinConsumption;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class AmountOfProteinConsumptionServiceImpl implements AmountOfProteinConsumptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmountOfProteinConsumptionService.class);
    private final AmountOfProteinConsumptionRepository amountOfProteinConsumptionRepository;

    public AmountOfProteinConsumptionServiceImpl(AmountOfProteinConsumptionRepository amountOfProteinConsumptionRepository) {
        this.amountOfProteinConsumptionRepository = amountOfProteinConsumptionRepository;
    }

    @Override
    public Either<Throwable, Optional<AmountOfProteinConsumption>> find(Long id) {
        return amountOfProteinConsumptionRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<AmountOfProteinConsumption>> findAll() {
        return amountOfProteinConsumptionRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, AmountOfProteinConsumption> save(AmountOfProteinConsumption amountOfProteinConsumption) {
        return amountOfProteinConsumptionRepository
                .save(amountOfProteinConsumption)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, AmountOfProteinConsumption>> retrieveById() {
        return id -> {
            Try<Optional<AmountOfProteinConsumption>> optionalTry = amountOfProteinConsumptionRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AmountOfProteinConsumption entity was saved but could not be retrieved from db";
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