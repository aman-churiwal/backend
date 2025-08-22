package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.StressLevelAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.StressLevelAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class StressLevelAnswerServiceImpl implements StressLevelAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StressLevelAnswerService.class);
    private final StressLevelAnswerRepository stressLevelAnswerRepository;
    private final QuizService quizService;

    public StressLevelAnswerServiceImpl(
            StressLevelAnswerRepository stressLevelAnswerRepository,
            QuizService quizService
    ) {
        this.stressLevelAnswerRepository = stressLevelAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<StressLevelAnswer>> find(Long id) {
        return stressLevelAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<StressLevelAnswer>> find(UUID quizExternalReference) {
        return stressLevelAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, StressLevelAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildStressLevelAnswer(categorizedAnswer))
                .flatMap(stressLevelAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, StressLevelAnswer> update(CategorizedAnswer categorizedAnswer) {
        return stressLevelAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedStressLevelAnswer(categorizedAnswer))
                .flatMap(stressLevelAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<StressLevelAnswer, StressLevelAnswer> buildUpdatedStressLevelAnswer(CategorizedAnswer categorizedAnswer) {
        return stressLevelAnswer -> {
            final Long id = stressLevelAnswer.getId();
            final Long quizId = stressLevelAnswer.getQuizId();
            final Long stressLevelId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = stressLevelAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new StressLevelAnswer(id, quizId, stressLevelId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, StressLevelAnswer> buildStressLevelAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long stressLevelId = categorizedAnswer.getCategoryInternalId();
            return new StressLevelAnswer(null, quizId, stressLevelId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, StressLevelAnswer>> retrieveById() {
        return id -> {
            Try<Optional<StressLevelAnswer>> optionalTry = stressLevelAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "StressLevelAnswer entity was saved but could not be retrieved from db";
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