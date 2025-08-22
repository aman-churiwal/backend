package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SmokeAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.SmokeAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class SmokeAnswerServiceImpl implements SmokeAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmokeAnswerService.class);
    private final SmokeAnswerRepository smokeAnswerRepository;
    private final QuizService quizService;

    public SmokeAnswerServiceImpl(
            SmokeAnswerRepository smokeAnswerRepository,
            QuizService quizService
    ) {
        this.smokeAnswerRepository = smokeAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<SmokeAnswer>> find(Long id) {
        return smokeAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<SmokeAnswer>> find(UUID quizExternalReference) {
        return smokeAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, SmokeAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildSmokeAnswer(categorizedAnswer))
                .flatMap(smokeAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, SmokeAnswer> update(CategorizedAnswer categorizedAnswer) {
        return smokeAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedSmokeAnswer(categorizedAnswer))
                .flatMap(smokeAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<SmokeAnswer, SmokeAnswer> buildUpdatedSmokeAnswer(CategorizedAnswer categorizedAnswer) {
        return smokeAnswer -> {
            final Long id = smokeAnswer.getId();
            final Long quizId = smokeAnswer.getQuizId();
            final Long smokeId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = smokeAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new SmokeAnswer(id, quizId, smokeId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, SmokeAnswer> buildSmokeAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long smokeId = categorizedAnswer.getCategoryInternalId();
            return new SmokeAnswer(null, quizId, smokeId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, SmokeAnswer>> retrieveById() {
        return id -> {
            Try<Optional<SmokeAnswer>> optionalTry = smokeAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "SmokeAnswer entity was saved but could not be retrieved from db";
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