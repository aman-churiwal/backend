package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.TiredWhenWakeUpAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.TiredWhenWakeUpAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class TiredWhenWakeUpAnswerServiceImpl implements TiredWhenWakeUpAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TiredWhenWakeUpAnswerService.class);
    private final TiredWhenWakeUpAnswerRepository tiredWhenWakeUpAnswerRepository;
    private final QuizService quizService;

    public TiredWhenWakeUpAnswerServiceImpl(
            TiredWhenWakeUpAnswerRepository tiredWhenWakeUpAnswerRepository,
            QuizService quizService
    ) {
        this.tiredWhenWakeUpAnswerRepository = tiredWhenWakeUpAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<TiredWhenWakeUpAnswer>> find(Long id) {
        return tiredWhenWakeUpAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<TiredWhenWakeUpAnswer>> find(UUID quizExternalReference) {
        return tiredWhenWakeUpAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, TiredWhenWakeUpAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildTiredWhenWakeUpAnswer(categorizedAnswer))
                .flatMap(tiredWhenWakeUpAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, TiredWhenWakeUpAnswer> update(CategorizedAnswer categorizedAnswer) {
        return tiredWhenWakeUpAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedTiredWhenWakeUpAnswer(categorizedAnswer))
                .flatMap(tiredWhenWakeUpAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<TiredWhenWakeUpAnswer, TiredWhenWakeUpAnswer> buildUpdatedTiredWhenWakeUpAnswer(CategorizedAnswer categorizedAnswer) {
        return tiredWhenWakeUpAnswer -> {
            final Long id = tiredWhenWakeUpAnswer.getId();
            final Long quizId = tiredWhenWakeUpAnswer.getQuizId();
            final Long tiredWhenWakeUpId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = tiredWhenWakeUpAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new TiredWhenWakeUpAnswer(id, quizId, tiredWhenWakeUpId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, TiredWhenWakeUpAnswer> buildTiredWhenWakeUpAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long tiredWhenWakeUpId = categorizedAnswer.getCategoryInternalId();
            return new TiredWhenWakeUpAnswer(null, quizId, tiredWhenWakeUpId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, TiredWhenWakeUpAnswer>> retrieveById() {
        return id -> {
            Try<Optional<TiredWhenWakeUpAnswer>> optionalTry = tiredWhenWakeUpAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "TiredWhenWakeUpAnswer entity was saved but could not be retrieved from db";
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