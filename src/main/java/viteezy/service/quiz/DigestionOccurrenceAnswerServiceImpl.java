package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DigestionOccurrenceAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DigestionOccurrenceAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class DigestionOccurrenceAnswerServiceImpl implements DigestionOccurrenceAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigestionOccurrenceAnswerService.class);
    private final DigestionOccurrenceAnswerRepository digestionOccurrenceAnswerRepository;
    private final QuizService quizService;

    public DigestionOccurrenceAnswerServiceImpl(
            DigestionOccurrenceAnswerRepository digestionOccurrenceAnswerRepository,
            QuizService quizService
    ) {
        this.digestionOccurrenceAnswerRepository = digestionOccurrenceAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<DigestionOccurrenceAnswer>> find(Long id) {
        return digestionOccurrenceAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<DigestionOccurrenceAnswer>> find(UUID quizExternalReference) {
        return digestionOccurrenceAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, DigestionOccurrenceAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildDigestionOccurrenceAnswer(categorizedAnswer))
                .flatMap(digestionOccurrenceAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, DigestionOccurrenceAnswer> update(CategorizedAnswer categorizedAnswer) {
        return digestionOccurrenceAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedDigestionOccurrenceAnswer(categorizedAnswer))
                .flatMap(digestionOccurrenceAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<DigestionOccurrenceAnswer, DigestionOccurrenceAnswer> buildUpdatedDigestionOccurrenceAnswer(CategorizedAnswer categorizedAnswer) {
        return digestionOccurrenceAnswer -> {
            final Long id = digestionOccurrenceAnswer.getId();
            final Long quizId = digestionOccurrenceAnswer.getQuizId();
            final Long digestionOccurrenceId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = digestionOccurrenceAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new DigestionOccurrenceAnswer(id, quizId, digestionOccurrenceId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, DigestionOccurrenceAnswer> buildDigestionOccurrenceAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long digestionOccurrenceId = categorizedAnswer.getCategoryInternalId();
            return new DigestionOccurrenceAnswer(null, quizId, digestionOccurrenceId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, DigestionOccurrenceAnswer>> retrieveById() {
        return id -> {
            Try<Optional<DigestionOccurrenceAnswer>> optionalTry = digestionOccurrenceAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DigestionOccurrenceAnswer entity was saved but could not be retrieved from db";
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