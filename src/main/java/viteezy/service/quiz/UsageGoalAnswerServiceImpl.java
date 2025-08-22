package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.QuizRepository;
import viteezy.db.quiz.UsageGoalAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.UsageGoalAnswer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class UsageGoalAnswerServiceImpl implements UsageGoalAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsageGoalAnswerService.class);
    private final UsageGoalAnswerRepository usageGoalAnswerRepository;
    private final QuizRepository quizRepository;

    public UsageGoalAnswerServiceImpl(
            UsageGoalAnswerRepository usageGoalAnswerRepository,
            QuizRepository quizRepository
    ) {
        this.usageGoalAnswerRepository = usageGoalAnswerRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public Either<Throwable, Optional<UsageGoalAnswer>> find(Long id) {
        return usageGoalAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<UsageGoalAnswer>> find(UUID quizExternalReference) {
        return usageGoalAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, UsageGoalAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizRepository.find(categorizedAnswer.getQuizExternalReference())
                .toEither()
                .map(buildUsageGoalAnswer(categorizedAnswer))
                .flatMap(answer -> usageGoalAnswerRepository.save(answer).toEither())
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, Void> delete(CategorizedAnswer categorizedAnswer) {
        return usageGoalAnswerRepository.find(categorizedAnswer.getQuizExternalReference(), categorizedAnswer.getCategoryInternalId())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedUsageGoalAnswer(categorizedAnswer))
                .flatMap(usageGoalAnswer -> usageGoalAnswerRepository.delete(usageGoalAnswer.getId()))
                .toEither()
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<UsageGoalAnswer, UsageGoalAnswer> buildUpdatedUsageGoalAnswer(CategorizedAnswer categorizedAnswer) {
        return usageGoalAnswer -> {
            final Long id = usageGoalAnswer.getId();
            final Long quizId = usageGoalAnswer.getQuizId();
            final Long usageGoalId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = usageGoalAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new UsageGoalAnswer(id, quizId, usageGoalId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, UsageGoalAnswer> buildUsageGoalAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long usageGoalId = categorizedAnswer.getCategoryInternalId();
            return new UsageGoalAnswer(null, quizId, usageGoalId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, UsageGoalAnswer>> retrieveById() {
        return id -> {
            Try<Optional<UsageGoalAnswer>> optionalTry = usageGoalAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "UsageGoalAnswer entity was saved but could not be retrieved from db";
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

    private <T> Function<Optional<T>, Try<T>> enforceEntityToBePresentTry() {
        return optional -> optional
                .map(entity -> Try.of(() -> entity))
                .orElseGet(() -> Try.failure(new NoSuchElementException()));
    }

}