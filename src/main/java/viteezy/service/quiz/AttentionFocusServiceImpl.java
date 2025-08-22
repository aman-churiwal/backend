package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AttentionFocusRepository;
import viteezy.domain.quiz.AttentionFocus;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class AttentionFocusServiceImpl implements AttentionFocusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttentionFocusService.class);
    private final AttentionFocusRepository attentionFocusRepository;

    public AttentionFocusServiceImpl(AttentionFocusRepository attentionFocusRepository) {
        this.attentionFocusRepository = attentionFocusRepository;
    }

    @Override
    public Either<Throwable, Optional<AttentionFocus>> find(Long id) {
        return attentionFocusRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<AttentionFocus>> findAll() {
        return attentionFocusRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, AttentionFocus> save(AttentionFocus attentionFocus) {
        return attentionFocusRepository
                .save(attentionFocus)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, AttentionFocus>> retrieveById() {
        return id -> {
            Try<Optional<AttentionFocus>> optionalTry = attentionFocusRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AttentionFocus entity was saved but could not be retrieved from db";
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