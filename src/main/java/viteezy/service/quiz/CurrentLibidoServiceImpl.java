package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.CurrentLibidoRepository;
import viteezy.domain.quiz.CurrentLibido;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class CurrentLibidoServiceImpl implements CurrentLibidoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentLibidoService.class);
    private final CurrentLibidoRepository currentLibidoRepository;

    public CurrentLibidoServiceImpl(CurrentLibidoRepository currentLibidoRepository) {
        this.currentLibidoRepository = currentLibidoRepository;
    }

    @Override
    public Either<Throwable, Optional<CurrentLibido>> find(Long id) {
        return currentLibidoRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<CurrentLibido>> findAll() {
        return currentLibidoRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, CurrentLibido> save(CurrentLibido currentLibido) {
        return currentLibidoRepository
                .save(currentLibido)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, CurrentLibido>> retrieveById() {
        return id -> {
            Try<Optional<CurrentLibido>> optionalTry = currentLibidoRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "CurrentLibido entity was saved but could not be retrieved from db";
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