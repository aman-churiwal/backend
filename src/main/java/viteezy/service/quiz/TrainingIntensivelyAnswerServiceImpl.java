package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.TrainingIntensivelyAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.TrainingIntensivelyAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class TrainingIntensivelyAnswerServiceImpl implements TrainingIntensivelyAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingIntensivelyAnswerService.class);
    private final TrainingIntensivelyAnswerRepository trainingIntensivelyAnswerRepository;
    private final QuizService quizService;

    public TrainingIntensivelyAnswerServiceImpl(
            TrainingIntensivelyAnswerRepository trainingIntensivelyAnswerRepository,
            QuizService quizService
    ) {
        this.trainingIntensivelyAnswerRepository = trainingIntensivelyAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<TrainingIntensivelyAnswer>> find(Long id) {
        return trainingIntensivelyAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<TrainingIntensivelyAnswer>> find(UUID quizExternalReference) {
        return trainingIntensivelyAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, TrainingIntensivelyAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildTrainingIntensivelyAnswer(categorizedAnswer))
                .flatMap(trainingIntensivelyAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, TrainingIntensivelyAnswer> update(CategorizedAnswer categorizedAnswer) {
        return trainingIntensivelyAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedTrainingIntensivelyAnswer(categorizedAnswer))
                .flatMap(trainingIntensivelyAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<TrainingIntensivelyAnswer, TrainingIntensivelyAnswer> buildUpdatedTrainingIntensivelyAnswer(CategorizedAnswer categorizedAnswer) {
        return trainingIntensivelyAnswer -> {
            final Long id = trainingIntensivelyAnswer.getId();
            final Long quizId = trainingIntensivelyAnswer.getQuizId();
            final Long trainingIntensivelyId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = trainingIntensivelyAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new TrainingIntensivelyAnswer(id, quizId, trainingIntensivelyId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, TrainingIntensivelyAnswer> buildTrainingIntensivelyAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long trainingIntensivelyId = categorizedAnswer.getCategoryInternalId();
            return new TrainingIntensivelyAnswer(null, quizId, trainingIntensivelyId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, TrainingIntensivelyAnswer>> retrieveById() {
        return id -> {
            Try<Optional<TrainingIntensivelyAnswer>> optionalTry = trainingIntensivelyAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "TrainingIntensivelyAnswer entity was saved but could not be retrieved from db";
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