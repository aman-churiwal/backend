package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.MentalFitnessRepository;
import viteezy.domain.quiz.MentalFitness;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class MentalFitnessServiceImpl implements MentalFitnessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MentalFitnessService.class);
    private final MentalFitnessRepository mentalFitnessRepository;

    public MentalFitnessServiceImpl(MentalFitnessRepository mentalFitnessRepository) {
        this.mentalFitnessRepository = mentalFitnessRepository;
    }

    @Override
    public Either<Throwable, Optional<MentalFitness>> find(Long id) {
        return mentalFitnessRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<MentalFitness>> findAll() {
        return mentalFitnessRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, MentalFitness> save(MentalFitness mentalFitness) {
        return mentalFitnessRepository
                .save(mentalFitness)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, MentalFitness>> retrieveById() {
        return id -> {
            Try<Optional<MentalFitness>> optionalTry = mentalFitnessRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "MentalFitness entity was saved but could not be retrieved from db";
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