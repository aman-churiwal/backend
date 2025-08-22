package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.LibidoStressLevelRepository;
import viteezy.domain.quiz.LibidoStressLevel;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class LibidoStressLevelServiceImpl implements LibidoStressLevelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibidoStressLevelService.class);
    private final LibidoStressLevelRepository libidoStressLevelRepository;

    public LibidoStressLevelServiceImpl(LibidoStressLevelRepository libidoStressLevelRepository) {
        this.libidoStressLevelRepository = libidoStressLevelRepository;
    }

    @Override
    public Either<Throwable, Optional<LibidoStressLevel>> find(Long id) {
        return libidoStressLevelRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<LibidoStressLevel>> findAll() {
        return libidoStressLevelRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, LibidoStressLevel> save(LibidoStressLevel libidoStressLevel) {
        return libidoStressLevelRepository
                .save(libidoStressLevel)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, LibidoStressLevel>> retrieveById() {
        return id -> {
            Try<Optional<LibidoStressLevel>> optionalTry = libidoStressLevelRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "LibidoStressLevel entity was saved but could not be retrieved from db";
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