package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AttentionStateAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AttentionStateAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AttentionStateAnswerServiceImpl implements AttentionStateAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttentionStateAnswerService.class);
    private final AttentionStateAnswerRepository attentionStateAnswerRepository;
    private final QuizService quizService;

    public AttentionStateAnswerServiceImpl(
            AttentionStateAnswerRepository attentionStateAnswerRepository,
            QuizService quizService
    ) {
        this.attentionStateAnswerRepository = attentionStateAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<AttentionStateAnswer>> find(Long id) {
        return attentionStateAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<AttentionStateAnswer>> find(UUID quizExternalReference) {
        return attentionStateAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AttentionStateAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildAttentionStateAnswer(categorizedAnswer))
                .flatMap(attentionStateAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, AttentionStateAnswer> update(CategorizedAnswer categorizedAnswer) {
        return attentionStateAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAttentionStateAnswer(categorizedAnswer))
                .flatMap(attentionStateAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AttentionStateAnswer, AttentionStateAnswer> buildUpdatedAttentionStateAnswer(CategorizedAnswer categorizedAnswer) {
        return attentionStateAnswer -> {
            final Long id = attentionStateAnswer.getId();
            final Long quizId = attentionStateAnswer.getQuizId();
            final Long attentionStateId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = attentionStateAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AttentionStateAnswer(id, quizId, attentionStateId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AttentionStateAnswer> buildAttentionStateAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long attentionStateId = categorizedAnswer.getCategoryInternalId();
            return new AttentionStateAnswer(null, quizId, attentionStateId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AttentionStateAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AttentionStateAnswer>> optionalTry = attentionStateAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AttentionStateAnswer entity was saved but could not be retrieved from db";
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