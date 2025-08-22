package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.TroubleFallingAsleepAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.TroubleFallingAsleepAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class TroubleFallingAsleepAnswerServiceImpl implements TroubleFallingAsleepAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TroubleFallingAsleepAnswerService.class);
    private final TroubleFallingAsleepAnswerRepository troubleFallingAsleepAnswerRepository;
    private final QuizService quizService;

    public TroubleFallingAsleepAnswerServiceImpl(
            TroubleFallingAsleepAnswerRepository troubleFallingAsleepAnswerRepository,
            QuizService quizService
    ) {
        this.troubleFallingAsleepAnswerRepository = troubleFallingAsleepAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<TroubleFallingAsleepAnswer>> find(Long id) {
        return troubleFallingAsleepAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<TroubleFallingAsleepAnswer>> find(UUID quizExternalReference) {
        return troubleFallingAsleepAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, TroubleFallingAsleepAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildTroubleFallingAsleepAnswer(categorizedAnswer))
                .flatMap(troubleFallingAsleepAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, TroubleFallingAsleepAnswer> update(CategorizedAnswer categorizedAnswer) {
        return troubleFallingAsleepAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedTroubleFallingAsleepAnswer(categorizedAnswer))
                .flatMap(troubleFallingAsleepAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<TroubleFallingAsleepAnswer, TroubleFallingAsleepAnswer> buildUpdatedTroubleFallingAsleepAnswer(CategorizedAnswer categorizedAnswer) {
        return troubleFallingAsleepAnswer -> {
            final Long id = troubleFallingAsleepAnswer.getId();
            final Long quizId = troubleFallingAsleepAnswer.getQuizId();
            final Long troubleFallingAsleepId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = troubleFallingAsleepAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new TroubleFallingAsleepAnswer(id, quizId, troubleFallingAsleepId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, TroubleFallingAsleepAnswer> buildTroubleFallingAsleepAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long troubleFallingAsleepId = categorizedAnswer.getCategoryInternalId();
            return new TroubleFallingAsleepAnswer(null, quizId, troubleFallingAsleepId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, TroubleFallingAsleepAnswer>> retrieveById() {
        return id -> {
            Try<Optional<TroubleFallingAsleepAnswer>> optionalTry = troubleFallingAsleepAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "TroubleFallingAsleepAnswer entity was saved but could not be retrieved from db";
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