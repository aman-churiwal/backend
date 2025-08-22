package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.TrainingIntensivelyRepository;
import viteezy.domain.quiz.TrainingIntensively;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class TrainingIntensivelyServiceImpl implements TrainingIntensivelyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingIntensivelyService.class);
    private final TrainingIntensivelyRepository trainingIntensivelyRepository;

    public TrainingIntensivelyServiceImpl(TrainingIntensivelyRepository trainingIntensivelyRepository) {
        this.trainingIntensivelyRepository = trainingIntensivelyRepository;
    }

    @Override
    public Either<Throwable, Optional<TrainingIntensively>> find(Long id) {
        return trainingIntensivelyRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<TrainingIntensively>> findAll() {
        return trainingIntensivelyRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, TrainingIntensively> save(TrainingIntensively trainingIntensively) {
        return trainingIntensivelyRepository
                .save(trainingIntensively)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, TrainingIntensively>> retrieveById() {
        return id -> {
            Try<Optional<TrainingIntensively>> optionalTry = trainingIntensivelyRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "TrainingIntensively entity was saved but could not be retrieved from db";
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