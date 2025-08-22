package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.MenstruationIntervalRepository;
import viteezy.domain.quiz.MenstruationInterval;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class MenstruationIntervalServiceImpl implements MenstruationIntervalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenstruationIntervalService.class);
    private final MenstruationIntervalRepository menstruationIntervalRepository;

    public MenstruationIntervalServiceImpl(MenstruationIntervalRepository menstruationIntervalRepository) {
        this.menstruationIntervalRepository = menstruationIntervalRepository;
    }

    @Override
    public Either<Throwable, Optional<MenstruationInterval>> find(Long id) {
        return menstruationIntervalRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<MenstruationInterval>> findAll() {
        return menstruationIntervalRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, MenstruationInterval> save(MenstruationInterval menstruationInterval) {
        return menstruationIntervalRepository
                .save(menstruationInterval)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, MenstruationInterval>> retrieveById() {
        return id -> {
            Try<Optional<MenstruationInterval>> optionalTry = menstruationIntervalRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "MenstruationInterval entity was saved but could not be retrieved from db";
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