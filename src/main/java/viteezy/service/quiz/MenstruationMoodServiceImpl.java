package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.MenstruationMoodRepository;
import viteezy.domain.quiz.MenstruationMood;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class MenstruationMoodServiceImpl implements MenstruationMoodService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenstruationMoodService.class);
    private final MenstruationMoodRepository menstruationMoodRepository;

    public MenstruationMoodServiceImpl(MenstruationMoodRepository menstruationMoodRepository) {
        this.menstruationMoodRepository = menstruationMoodRepository;
    }

    @Override
    public Either<Throwable, Optional<MenstruationMood>> find(Long id) {
        return menstruationMoodRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<MenstruationMood>> findAll() {
        return menstruationMoodRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, MenstruationMood> save(MenstruationMood menstruationMood) {
        return menstruationMoodRepository
                .save(menstruationMood)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, MenstruationMood>> retrieveById() {
        return id -> {
            Try<Optional<MenstruationMood>> optionalTry = menstruationMoodRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "MenstruationMood entity was saved but could not be retrieved from db";
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