package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AttentionFocusAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AttentionFocusAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AttentionFocusAnswerServiceImpl implements AttentionFocusAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttentionFocusAnswerService.class);
    private final AttentionFocusAnswerRepository attentionFocusAnswerRepository;
    private final QuizService quizService;

    public AttentionFocusAnswerServiceImpl(
            AttentionFocusAnswerRepository attentionFocusAnswerRepository,
            QuizService quizService
    ) {
        this.attentionFocusAnswerRepository = attentionFocusAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<AttentionFocusAnswer>> find(Long id) {
        return attentionFocusAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<AttentionFocusAnswer>> find(UUID quizExternalReference) {
        return attentionFocusAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AttentionFocusAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildAttentionFocusAnswer(categorizedAnswer))
                .flatMap(attentionFocusAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, AttentionFocusAnswer> update(CategorizedAnswer categorizedAnswer) {
        return attentionFocusAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAttentionFocusAnswer(categorizedAnswer))
                .flatMap(attentionFocusAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AttentionFocusAnswer, AttentionFocusAnswer> buildUpdatedAttentionFocusAnswer(CategorizedAnswer categorizedAnswer) {
        return attentionFocusAnswer -> {
            final Long id = attentionFocusAnswer.getId();
            final Long quizId = attentionFocusAnswer.getQuizId();
            final Long attentionFocusId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = attentionFocusAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AttentionFocusAnswer(id, quizId, attentionFocusId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AttentionFocusAnswer> buildAttentionFocusAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long attentionFocusId = categorizedAnswer.getCategoryInternalId();
            return new AttentionFocusAnswer(null, quizId, attentionFocusId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AttentionFocusAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AttentionFocusAnswer>> optionalTry = attentionFocusAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AttentionFocusAnswer entity was saved but could not be retrieved from db";
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