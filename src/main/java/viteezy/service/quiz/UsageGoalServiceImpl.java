package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.UsageGoalRepository;
import viteezy.domain.quiz.UsageGoal;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UsageGoalServiceImpl implements UsageGoalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsageGoalService.class);
    private final UsageGoalRepository usageGoalRepository;

    public UsageGoalServiceImpl(UsageGoalRepository usageGoalRepository) {
        this.usageGoalRepository = usageGoalRepository;
    }

    @Override
    public Either<Throwable, Optional<UsageGoal>> find(Long id) {
        return usageGoalRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<UsageGoal>> findAll() {
        return usageGoalRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, List<UsageGoal>> findActive() {
        return usageGoalRepository.findAll()
                .map(usageGoals -> usageGoals.stream()
                        .filter(UsageGoal::isActive)
                        .collect(Collectors.toList())
                ).toEither();
    }

    @Override
    public Either<Throwable, UsageGoal> save(UsageGoal usageGoal) {
        return usageGoalRepository
                .save(usageGoal)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, UsageGoal>> retrieveById() {
        return id -> {
            Try<Optional<UsageGoal>> optionalTry = usageGoalRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "UsageGoal entity was saved but could not be retrieved from db";
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