package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.IronPrescribedRepository;
import viteezy.domain.quiz.IronPrescribed;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class IronPrescribedServiceImpl implements IronPrescribedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IronPrescribedService.class);
    private final IronPrescribedRepository ironPrescribedRepository;

    public IronPrescribedServiceImpl(IronPrescribedRepository ironPrescribedRepository) {
        this.ironPrescribedRepository = ironPrescribedRepository;
    }

    @Override
    public Either<Throwable, Optional<IronPrescribed>> find(Long id) {
        return ironPrescribedRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<IronPrescribed>> findAll() {
        return ironPrescribedRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, IronPrescribed> save(IronPrescribed ironPrescribed) {
        return ironPrescribedRepository
                .save(ironPrescribed)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, IronPrescribed>> retrieveById() {
        return id -> {
            Try<Optional<IronPrescribed>> optionalTry = ironPrescribedRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "IronPrescribed entity was saved but could not be retrieved from db";
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