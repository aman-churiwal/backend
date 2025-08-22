package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SkinProblemRepository;
import viteezy.domain.quiz.SkinProblem;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SkinProblemServiceImpl implements SkinProblemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkinProblemService.class);
    private final SkinProblemRepository skinProblemRepository;

    public SkinProblemServiceImpl(SkinProblemRepository skinProblemRepository) {
        this.skinProblemRepository = skinProblemRepository;
    }

    @Override
    public Either<Throwable, Optional<SkinProblem>> find(Long id) {
        return skinProblemRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<SkinProblem>> findAll() {
        return skinProblemRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, SkinProblem> save(SkinProblem skinProblem) {
        return skinProblemRepository
                .save(skinProblem)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, SkinProblem>> retrieveById() {
        return id -> {
            Try<Optional<SkinProblem>> optionalTry = skinProblemRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "SkinProblem entity was saved but could not be retrieved from db";
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