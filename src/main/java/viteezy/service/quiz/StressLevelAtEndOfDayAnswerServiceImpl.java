package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.StressLevelAtEndOfDayAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.StressLevelAtEndOfDayAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class StressLevelAtEndOfDayAnswerServiceImpl implements StressLevelAtEndOfDayAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StressLevelAtEndOfDayAnswerService.class);
    private final StressLevelAtEndOfDayAnswerRepository stressLevelAtEndOfDayAnswerRepository;
    private final QuizService quizService;

    public StressLevelAtEndOfDayAnswerServiceImpl(
            StressLevelAtEndOfDayAnswerRepository stressLevelAtEndOfDayAnswerRepository,
            QuizService quizService
    ) {
        this.stressLevelAtEndOfDayAnswerRepository = stressLevelAtEndOfDayAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<StressLevelAtEndOfDayAnswer>> find(Long id) {
        return stressLevelAtEndOfDayAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<StressLevelAtEndOfDayAnswer>> find(UUID quizExternalReference) {
        return stressLevelAtEndOfDayAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, StressLevelAtEndOfDayAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildStressLevelAtEndOfDayAnswer(categorizedAnswer))
                .flatMap(stressLevelAtEndOfDayAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, StressLevelAtEndOfDayAnswer> update(CategorizedAnswer categorizedAnswer) {
        return stressLevelAtEndOfDayAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedStressLevelAtEndOfDayAnswer(categorizedAnswer))
                .flatMap(stressLevelAtEndOfDayAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<StressLevelAtEndOfDayAnswer, StressLevelAtEndOfDayAnswer> buildUpdatedStressLevelAtEndOfDayAnswer(CategorizedAnswer categorizedAnswer) {
        return stressLevelAtEndOfDayAnswer -> {
            final Long id = stressLevelAtEndOfDayAnswer.getId();
            final Long quizId = stressLevelAtEndOfDayAnswer.getQuizId();
            final Long stressLevelAtEndOfDayId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = stressLevelAtEndOfDayAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new StressLevelAtEndOfDayAnswer(id, quizId, stressLevelAtEndOfDayId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, StressLevelAtEndOfDayAnswer> buildStressLevelAtEndOfDayAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long stressLevelAtEndOfDayId = categorizedAnswer.getCategoryInternalId();
            return new StressLevelAtEndOfDayAnswer(null, quizId, stressLevelAtEndOfDayId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, StressLevelAtEndOfDayAnswer>> retrieveById() {
        return id -> {
            Try<Optional<StressLevelAtEndOfDayAnswer>> optionalTry = stressLevelAtEndOfDayAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "StressLevelAtEndOfDayAnswer entity was saved but could not be retrieved from db";
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