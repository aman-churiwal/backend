package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.ChildrenWishRepository;
import viteezy.domain.quiz.ChildrenWish;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChildrenWishServiceImpl implements ChildrenWishService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChildrenWishService.class);
    private final ChildrenWishRepository childrenWishRepository;

    public ChildrenWishServiceImpl(ChildrenWishRepository childrenWishRepository) {
        this.childrenWishRepository = childrenWishRepository;
    }

    @Override
    public Either<Throwable, Optional<ChildrenWish>> find(Long id) {
        return childrenWishRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<ChildrenWish>> findAll() {
        return childrenWishRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, ChildrenWish> save(ChildrenWish childrenWish) {
        return childrenWishRepository
                .save(childrenWish)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, ChildrenWish>> retrieveById() {
        return id -> {
            Try<Optional<ChildrenWish>> optionalTry = childrenWishRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "ChildrenWish entity was saved but could not be retrieved from db";
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