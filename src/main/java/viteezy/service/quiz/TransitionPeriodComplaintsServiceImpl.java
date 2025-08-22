package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.TransitionPeriodComplaintsRepository;
import viteezy.domain.quiz.TransitionPeriodComplaints;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class TransitionPeriodComplaintsServiceImpl implements TransitionPeriodComplaintsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransitionPeriodComplaintsService.class);
    private final TransitionPeriodComplaintsRepository transitionPeriodComplaintsRepository;

    public TransitionPeriodComplaintsServiceImpl(TransitionPeriodComplaintsRepository transitionPeriodComplaintsRepository) {
        this.transitionPeriodComplaintsRepository = transitionPeriodComplaintsRepository;
    }

    @Override
    public Either<Throwable, Optional<TransitionPeriodComplaints>> find(Long id) {
        return transitionPeriodComplaintsRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<TransitionPeriodComplaints>> findAll() {
        return transitionPeriodComplaintsRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, TransitionPeriodComplaints> save(TransitionPeriodComplaints transitionPeriodComplaints) {
        return transitionPeriodComplaintsRepository
                .save(transitionPeriodComplaints)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, TransitionPeriodComplaints>> retrieveById() {
        return id -> {
            Try<Optional<TransitionPeriodComplaints>> optionalTry = transitionPeriodComplaintsRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "TransitionPeriodComplaints entity was saved but could not be retrieved from db";
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