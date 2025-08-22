package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.HelpGoalRepository;
import viteezy.domain.quiz.HelpGoal;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class HelpGoalServiceImpl implements HelpGoalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpGoalService.class);
    private final HelpGoalRepository helpGoalRepository;

    public HelpGoalServiceImpl(HelpGoalRepository helpGoalRepository) {
        this.helpGoalRepository = helpGoalRepository;
    }

    @Override
    public Either<Throwable, Optional<HelpGoal>> find(Long id) {
        return helpGoalRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<HelpGoal>> findAll() {
        return helpGoalRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, HelpGoal> save(HelpGoal helpGoal) {
        return helpGoalRepository
                .save(helpGoal)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, HelpGoal>> retrieveById() {
        return id -> {
            Try<Optional<HelpGoal>> optionalTry = helpGoalRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "HelpGoal entity was saved but could not be retrieved from db";
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