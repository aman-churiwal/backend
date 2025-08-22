package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.StressLevelConditionRepository;
import viteezy.domain.quiz.StressLevelCondition;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class StressLevelConditionServiceImpl implements StressLevelConditionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StressLevelConditionService.class);
    private final StressLevelConditionRepository stressLevelConditionRepository;

    public StressLevelConditionServiceImpl(StressLevelConditionRepository stressLevelConditionRepository) {
        this.stressLevelConditionRepository = stressLevelConditionRepository;
    }

    @Override
    public Either<Throwable, Optional<StressLevelCondition>> find(Long id) {
        return stressLevelConditionRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<StressLevelCondition>> findAll() {
        return stressLevelConditionRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, StressLevelCondition> save(StressLevelCondition stressLevelCondition) {
        return stressLevelConditionRepository
                .save(stressLevelCondition)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, StressLevelCondition>> retrieveById() {
        return id -> {
            Try<Optional<StressLevelCondition>> optionalTry = stressLevelConditionRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "StressLevelCondition entity was saved but could not be retrieved from db";
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