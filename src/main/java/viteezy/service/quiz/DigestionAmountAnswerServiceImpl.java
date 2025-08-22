package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DigestionAmountAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DigestionAmountAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class DigestionAmountAnswerServiceImpl implements DigestionAmountAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigestionAmountAnswerService.class);
    private final DigestionAmountAnswerRepository digestionAmountAnswerRepository;
    private final QuizService quizService;

    public DigestionAmountAnswerServiceImpl(
            DigestionAmountAnswerRepository digestionAmountAnswerRepository,
            QuizService quizService
    ) {
        this.digestionAmountAnswerRepository = digestionAmountAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<DigestionAmountAnswer>> find(Long id) {
        return digestionAmountAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<DigestionAmountAnswer>> find(UUID quizExternalReference) {
        return digestionAmountAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, DigestionAmountAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildDigestionAmountAnswer(categorizedAnswer))
                .flatMap(digestionAmountAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, DigestionAmountAnswer> update(CategorizedAnswer categorizedAnswer) {
        return digestionAmountAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedDigestionAmountAnswer(categorizedAnswer))
                .flatMap(digestionAmountAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<DigestionAmountAnswer, DigestionAmountAnswer> buildUpdatedDigestionAmountAnswer(CategorizedAnswer categorizedAnswer) {
        return digestionAmountAnswer -> {
            final Long id = digestionAmountAnswer.getId();
            final Long quizId = digestionAmountAnswer.getQuizId();
            final Long digestionAmountId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = digestionAmountAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new DigestionAmountAnswer(id, quizId, digestionAmountId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, DigestionAmountAnswer> buildDigestionAmountAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long digestionAmountId = categorizedAnswer.getCategoryInternalId();
            return new DigestionAmountAnswer(null, quizId, digestionAmountId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, DigestionAmountAnswer>> retrieveById() {
        return id -> {
            Try<Optional<DigestionAmountAnswer>> optionalTry = digestionAmountAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DigestionAmountAnswer entity was saved but could not be retrieved from db";
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