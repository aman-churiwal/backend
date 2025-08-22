package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.OftenHavingFluAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.OftenHavingFluAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class OftenHavingFluAnswerServiceImpl implements OftenHavingFluAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OftenHavingFluAnswerService.class);
    private final OftenHavingFluAnswerRepository oftenHavingFluAnswerRepository;
    private final QuizService quizService;

    public OftenHavingFluAnswerServiceImpl(
            OftenHavingFluAnswerRepository oftenHavingFluAnswerRepository,
            QuizService quizService
    ) {
        this.oftenHavingFluAnswerRepository = oftenHavingFluAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<OftenHavingFluAnswer>> find(Long id) {
        return oftenHavingFluAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<OftenHavingFluAnswer>> find(UUID quizExternalReference) {
        return oftenHavingFluAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, OftenHavingFluAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildOftenHavingFluAnswer(categorizedAnswer))
                .flatMap(oftenHavingFluAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, OftenHavingFluAnswer> update(CategorizedAnswer categorizedAnswer) {
        return oftenHavingFluAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedOftenHavingFluAnswer(categorizedAnswer))
                .flatMap(oftenHavingFluAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<OftenHavingFluAnswer, OftenHavingFluAnswer> buildUpdatedOftenHavingFluAnswer(CategorizedAnswer categorizedAnswer) {
        return oftenHavingFluAnswer -> {
            final Long id = oftenHavingFluAnswer.getId();
            final Long quizId = oftenHavingFluAnswer.getQuizId();
            final Long oftenHavingFluId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = oftenHavingFluAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new OftenHavingFluAnswer(id, quizId, oftenHavingFluId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, OftenHavingFluAnswer> buildOftenHavingFluAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long oftenHavingFluId = categorizedAnswer.getCategoryInternalId();
            return new OftenHavingFluAnswer(null, quizId, oftenHavingFluId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, OftenHavingFluAnswer>> retrieveById() {
        return id -> {
            Try<Optional<OftenHavingFluAnswer>> optionalTry = oftenHavingFluAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "OftenHavingFluAnswer entity was saved but could not be retrieved from db";
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