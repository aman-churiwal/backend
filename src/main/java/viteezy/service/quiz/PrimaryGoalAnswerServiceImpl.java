package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.PrimaryGoalAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.PrimaryGoalAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PrimaryGoalAnswerServiceImpl implements PrimaryGoalAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrimaryGoalAnswerService.class);
    private final PrimaryGoalAnswerRepository primaryGoalAnswerRepository;
    private final QuizRepository quizRepository;

    public PrimaryGoalAnswerServiceImpl(
            PrimaryGoalAnswerRepository primaryGoalAnswerRepository,
            QuizRepository quizRepository
    ) {
        this.primaryGoalAnswerRepository = primaryGoalAnswerRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public Either<Throwable, Optional<PrimaryGoalAnswer>> find(Long id) {
        return primaryGoalAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<PrimaryGoalAnswer>> find(UUID quizExternalReference) {
        return primaryGoalAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, PrimaryGoalAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizRepository.find(categorizedAnswer.getQuizExternalReference())
                .toEither()
                .map(buildPrimaryGoalAnswer(categorizedAnswer))
                .flatMap(answer -> primaryGoalAnswerRepository.save(answer).toEither())
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, PrimaryGoalAnswer> update(CategorizedAnswer categorizedAnswer) {
        return primaryGoalAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedPrimaryGoalAnswer(categorizedAnswer))
                .flatMap(primaryGoalAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<PrimaryGoalAnswer, PrimaryGoalAnswer> buildUpdatedPrimaryGoalAnswer(CategorizedAnswer categorizedAnswer) {
        return primaryGoalAnswer -> {
            final Long id = primaryGoalAnswer.getId();
            final Long quizId = primaryGoalAnswer.getQuizId();
            final Long primaryGoalId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = primaryGoalAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new PrimaryGoalAnswer(id, quizId, primaryGoalId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, PrimaryGoalAnswer> buildPrimaryGoalAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long primaryGoalId = categorizedAnswer.getCategoryInternalId();
            return new PrimaryGoalAnswer(null, quizId, primaryGoalId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, PrimaryGoalAnswer>> retrieveById() {
        return id -> {
            Try<Optional<PrimaryGoalAnswer>> optionalTry = primaryGoalAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "PrimaryGoalAnswer entity was saved but could not be retrieved from db";
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