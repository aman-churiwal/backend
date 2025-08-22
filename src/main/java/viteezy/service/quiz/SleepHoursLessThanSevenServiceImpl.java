package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SleepHoursLessThanSevenRepository;
import viteezy.domain.quiz.SleepHoursLessThanSeven;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SleepHoursLessThanSevenServiceImpl implements SleepHoursLessThanSevenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SleepHoursLessThanSevenService.class);
    private final SleepHoursLessThanSevenRepository sleepHoursLessThanSevenRepository;

    public SleepHoursLessThanSevenServiceImpl(SleepHoursLessThanSevenRepository sleepHoursLessThanSevenRepository) {
        this.sleepHoursLessThanSevenRepository = sleepHoursLessThanSevenRepository;
    }

    @Override
    public Either<Throwable, Optional<SleepHoursLessThanSeven>> find(Long id) {
        return sleepHoursLessThanSevenRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<SleepHoursLessThanSeven>> findAll() {
        return sleepHoursLessThanSevenRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, SleepHoursLessThanSeven> save(SleepHoursLessThanSeven sleepHoursLessThanSeven) {
        return sleepHoursLessThanSevenRepository
                .save(sleepHoursLessThanSeven)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, SleepHoursLessThanSeven>> retrieveById() {
        return id -> {
            Try<Optional<SleepHoursLessThanSeven>> optionalTry = sleepHoursLessThanSevenRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "SleepHoursLessThanSeven entity was saved but could not be retrieved from db";
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