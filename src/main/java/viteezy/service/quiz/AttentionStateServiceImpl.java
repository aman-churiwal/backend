package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AttentionStateRepository;
import viteezy.domain.quiz.AttentionState;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class AttentionStateServiceImpl implements AttentionStateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttentionStateService.class);
    private final AttentionStateRepository attentionStateRepository;

    public AttentionStateServiceImpl(AttentionStateRepository attentionStateRepository) {
        this.attentionStateRepository = attentionStateRepository;
    }

    @Override
    public Either<Throwable, Optional<AttentionState>> find(Long id) {
        return attentionStateRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<AttentionState>> findAll() {
        return attentionStateRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, AttentionState> save(AttentionState attentionState) {
        return attentionStateRepository
                .save(attentionState)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, AttentionState>> retrieveById() {
        return id -> {
            Try<Optional<AttentionState>> optionalTry = attentionStateRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AttentionState entity was saved but could not be retrieved from db";
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