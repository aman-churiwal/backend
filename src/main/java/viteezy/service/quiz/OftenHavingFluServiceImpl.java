package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.OftenHavingFluRepository;
import viteezy.domain.quiz.OftenHavingFlu;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class OftenHavingFluServiceImpl implements OftenHavingFluService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OftenHavingFluService.class);
    private final OftenHavingFluRepository oftenHavingFluRepository;

    public OftenHavingFluServiceImpl(OftenHavingFluRepository oftenHavingFluRepository) {
        this.oftenHavingFluRepository = oftenHavingFluRepository;
    }

    @Override
    public Either<Throwable, Optional<OftenHavingFlu>> find(Long id) {
        return oftenHavingFluRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<OftenHavingFlu>> findAll() {
        return oftenHavingFluRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, OftenHavingFlu> save(OftenHavingFlu oftenHavingFlu) {
        return oftenHavingFluRepository
                .save(oftenHavingFlu)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, OftenHavingFlu>> retrieveById() {
        return id -> {
            Try<Optional<OftenHavingFlu>> optionalTry = oftenHavingFluRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "OftenHavingFlu entity was saved but could not be retrieved from db";
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