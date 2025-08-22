package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SleepQualityRepository;
import viteezy.domain.quiz.SleepQuality;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SleepQualityServiceImpl implements SleepQualityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SleepQualityService.class);
    private final SleepQualityRepository sleepQualityRepository;

    public SleepQualityServiceImpl(SleepQualityRepository sleepQualityRepository) {
        this.sleepQualityRepository = sleepQualityRepository;
    }

    @Override
    public Either<Throwable, Optional<SleepQuality>> find(Long id) {
        return sleepQualityRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<SleepQuality>> findAll() {
        return sleepQualityRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, SleepQuality> save(SleepQuality sleepQuality) {
        return sleepQualityRepository
                .save(sleepQuality)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, SleepQuality>> retrieveById() {
        return id -> {
            Try<Optional<SleepQuality>> optionalTry = sleepQualityRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "SleepQuality entity was saved but could not be retrieved from db";
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