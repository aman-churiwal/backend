package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.StressLevelConditionAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.StressLevelConditionAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class StressLevelConditionAnswerServiceImpl implements StressLevelConditionAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StressLevelConditionAnswerService.class);
    private final StressLevelConditionAnswerRepository stressLevelConditionAnswerRepository;
    private final QuizService quizService;

    public StressLevelConditionAnswerServiceImpl(
            StressLevelConditionAnswerRepository stressLevelConditionAnswerRepository,
            QuizService quizService
    ) {
        this.stressLevelConditionAnswerRepository = stressLevelConditionAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<StressLevelConditionAnswer>> find(Long id) {
        return stressLevelConditionAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<StressLevelConditionAnswer>> find(UUID quizExternalReference) {
        return stressLevelConditionAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, StressLevelConditionAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildStressLevelConditionAnswer(categorizedAnswer))
                .flatMap(stressLevelConditionAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, StressLevelConditionAnswer> update(CategorizedAnswer categorizedAnswer) {
        return stressLevelConditionAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedStressLevelConditionAnswer(categorizedAnswer))
                .flatMap(stressLevelConditionAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<StressLevelConditionAnswer, StressLevelConditionAnswer> buildUpdatedStressLevelConditionAnswer(CategorizedAnswer categorizedAnswer) {
        return stressLevelConditionAnswer -> {
            final Long id = stressLevelConditionAnswer.getId();
            final Long quizId = stressLevelConditionAnswer.getQuizId();
            final Long stressLevelConditionId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = stressLevelConditionAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new StressLevelConditionAnswer(id, quizId, stressLevelConditionId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, StressLevelConditionAnswer> buildStressLevelConditionAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long stressLevelConditionId = categorizedAnswer.getCategoryInternalId();
            return new StressLevelConditionAnswer(null, quizId, stressLevelConditionId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, StressLevelConditionAnswer>> retrieveById() {
        return id -> {
            Try<Optional<StressLevelConditionAnswer>> optionalTry = stressLevelConditionAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "StressLevelConditionAnswer entity was saved but could not be retrieved from db";
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