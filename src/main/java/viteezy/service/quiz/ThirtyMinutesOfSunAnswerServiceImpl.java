package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.ThirtyMinutesOfSunAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.ThirtyMinutesOfSunAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class ThirtyMinutesOfSunAnswerServiceImpl implements ThirtyMinutesOfSunAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirtyMinutesOfSunAnswerService.class);
    private final ThirtyMinutesOfSunAnswerRepository thirtyMinutesOfSunAnswerRepository;
    private final QuizService quizService;

    public ThirtyMinutesOfSunAnswerServiceImpl(
            ThirtyMinutesOfSunAnswerRepository thirtyMinutesOfSunAnswerRepository,
            QuizService quizService
    ) {
        this.thirtyMinutesOfSunAnswerRepository = thirtyMinutesOfSunAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<ThirtyMinutesOfSunAnswer>> find(Long id) {
        return thirtyMinutesOfSunAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<ThirtyMinutesOfSunAnswer>> find(UUID quizExternalReference) {
        return thirtyMinutesOfSunAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, ThirtyMinutesOfSunAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildThirtyMinutesOfSunAnswer(categorizedAnswer))
                .flatMap(thirtyMinutesOfSunAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, ThirtyMinutesOfSunAnswer> update(CategorizedAnswer categorizedAnswer) {
        return thirtyMinutesOfSunAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedThirtyMinutesOfSunAnswer(categorizedAnswer))
                .flatMap(thirtyMinutesOfSunAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<ThirtyMinutesOfSunAnswer, ThirtyMinutesOfSunAnswer> buildUpdatedThirtyMinutesOfSunAnswer(CategorizedAnswer categorizedAnswer) {
        return thirtyMinutesOfSunAnswer -> {
            final Long id = thirtyMinutesOfSunAnswer.getId();
            final Long quizId = thirtyMinutesOfSunAnswer.getQuizId();
            final Long thirtyMinutesOfSunId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = thirtyMinutesOfSunAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new ThirtyMinutesOfSunAnswer(id, quizId, thirtyMinutesOfSunId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, ThirtyMinutesOfSunAnswer> buildThirtyMinutesOfSunAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long thirtyMinutesOfSunId = categorizedAnswer.getCategoryInternalId();
            return new ThirtyMinutesOfSunAnswer(null, quizId, thirtyMinutesOfSunId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, ThirtyMinutesOfSunAnswer>> retrieveById() {
        return id -> {
            Try<Optional<ThirtyMinutesOfSunAnswer>> optionalTry = thirtyMinutesOfSunAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "ThirtyMinutesOfSunAnswer entity was saved but could not be retrieved from db";
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