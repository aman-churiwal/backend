package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.HealthyLifestyleAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.HealthyLifestyleAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class HealthyLifestyleAnswerServiceImpl implements HealthyLifestyleAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthyLifestyleAnswerService.class);
    private final HealthyLifestyleAnswerRepository healthyLifestyleAnswerRepository;
    private final QuizService quizService;

    public HealthyLifestyleAnswerServiceImpl(
            HealthyLifestyleAnswerRepository healthyLifestyleAnswerRepository,
            QuizService quizService
    ) {
        this.healthyLifestyleAnswerRepository = healthyLifestyleAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<HealthyLifestyleAnswer>> find(Long id) {
        return healthyLifestyleAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<HealthyLifestyleAnswer>> find(UUID quizExternalReference) {
        return healthyLifestyleAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, HealthyLifestyleAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildHealthyLifestyleAnswer(categorizedAnswer))
                .flatMap(healthyLifestyleAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, HealthyLifestyleAnswer> update(CategorizedAnswer categorizedAnswer) {
        return healthyLifestyleAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedHealthyLifestyleAnswer(categorizedAnswer))
                .flatMap(healthyLifestyleAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<HealthyLifestyleAnswer, HealthyLifestyleAnswer> buildUpdatedHealthyLifestyleAnswer(CategorizedAnswer categorizedAnswer) {
        return healthyLifestyleAnswer -> {
            final Long id = healthyLifestyleAnswer.getId();
            final Long quizId = healthyLifestyleAnswer.getQuizId();
            final Long healthyLifestyleId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = healthyLifestyleAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new HealthyLifestyleAnswer(id, quizId, healthyLifestyleId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, HealthyLifestyleAnswer> buildHealthyLifestyleAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long healthyLifestyleId = categorizedAnswer.getCategoryInternalId();
            return new HealthyLifestyleAnswer(null, quizId, healthyLifestyleId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, HealthyLifestyleAnswer>> retrieveById() {
        return id -> {
            Try<Optional<HealthyLifestyleAnswer>> optionalTry = healthyLifestyleAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "HealthyLifestyleAnswer entity was saved but could not be retrieved from db";
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