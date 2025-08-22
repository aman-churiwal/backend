package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AcnePlaceAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AcnePlaceAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AcnePlaceAnswerServiceImpl implements AcnePlaceAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcnePlaceAnswerService.class);
    private final AcnePlaceAnswerRepository acnePlaceAnswerRepository;
    private final QuizService quizService;

    public AcnePlaceAnswerServiceImpl(
            AcnePlaceAnswerRepository acnePlaceAnswerRepository,
            QuizService quizService
    ) {
        this.acnePlaceAnswerRepository = acnePlaceAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<AcnePlaceAnswer>> find(Long id) {
        return acnePlaceAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<AcnePlaceAnswer>> find(UUID quizExternalReference) {
        return acnePlaceAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AcnePlaceAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildAcnePlaceAnswer(categorizedAnswer))
                .flatMap(acnePlaceAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, AcnePlaceAnswer> update(CategorizedAnswer categorizedAnswer) {
        return acnePlaceAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAcnePlaceAnswer(categorizedAnswer))
                .flatMap(acnePlaceAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AcnePlaceAnswer, AcnePlaceAnswer> buildUpdatedAcnePlaceAnswer(CategorizedAnswer categorizedAnswer) {
        return acnePlaceAnswer -> {
            final Long id = acnePlaceAnswer.getId();
            final Long quizId = acnePlaceAnswer.getQuizId();
            final Long acnePlaceId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = acnePlaceAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AcnePlaceAnswer(id, quizId, acnePlaceId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AcnePlaceAnswer> buildAcnePlaceAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long acnePlaceId = categorizedAnswer.getCategoryInternalId();
            return new AcnePlaceAnswer(null, quizId, acnePlaceId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AcnePlaceAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AcnePlaceAnswer>> optionalTry = acnePlaceAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AcnePlaceAnswer entity was saved but could not be retrieved from db";
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