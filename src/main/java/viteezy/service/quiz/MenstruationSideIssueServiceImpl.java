package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.MenstruationSideIssueRepository;
import viteezy.domain.quiz.MenstruationSideIssue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class MenstruationSideIssueServiceImpl implements MenstruationSideIssueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenstruationSideIssueService.class);
    private final MenstruationSideIssueRepository menstruationSideIssueRepository;

    public MenstruationSideIssueServiceImpl(MenstruationSideIssueRepository menstruationSideIssueRepository) {
        this.menstruationSideIssueRepository = menstruationSideIssueRepository;
    }

    @Override
    public Either<Throwable, Optional<MenstruationSideIssue>> find(Long id) {
        return menstruationSideIssueRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<MenstruationSideIssue>> findAll() {
        return menstruationSideIssueRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, MenstruationSideIssue> save(MenstruationSideIssue menstruationSideIssue) {
        return menstruationSideIssueRepository
                .save(menstruationSideIssue)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, MenstruationSideIssue>> retrieveById() {
        return id -> {
            Try<Optional<MenstruationSideIssue>> optionalTry = menstruationSideIssueRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "MenstruationSideIssue entity was saved but could not be retrieved from db";
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