package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.PregnancyStateRepository;
import viteezy.domain.quiz.PregnancyState;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class PregnancyStateServiceImpl implements PregnancyStateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PregnancyStateService.class);
    private final PregnancyStateRepository pregnancyStateRepository;

    public PregnancyStateServiceImpl(PregnancyStateRepository pregnancyStateRepository) {
        this.pregnancyStateRepository = pregnancyStateRepository;
    }

    @Override
    public Either<Throwable, Optional<PregnancyState>> find(Long id) {
        return pregnancyStateRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<PregnancyState>> findAll() {
        return pregnancyStateRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, PregnancyState> save(PregnancyState pregnancyState) {
        return pregnancyStateRepository
                .save(pregnancyState)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, PregnancyState>> retrieveById() {
        return id -> {
            Try<Optional<PregnancyState>> optionalTry = pregnancyStateRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "PregnancyState entity was saved but could not be retrieved from db";
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