package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.ChildrenWishAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.ChildrenWishAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChildrenWishAnswerServiceImpl implements ChildrenWishAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChildrenWishAnswerService.class);
    private final ChildrenWishAnswerRepository childrenWishAnswerRepository;
    private final QuizService quizService;

    public ChildrenWishAnswerServiceImpl(
            ChildrenWishAnswerRepository childrenWishAnswerRepository,
            QuizService quizService
    ) {
        this.childrenWishAnswerRepository = childrenWishAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<ChildrenWishAnswer>> find(Long id) {
        return childrenWishAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<ChildrenWishAnswer>> find(UUID quizExternalReference) {
        return childrenWishAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, ChildrenWishAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildChildrenWishAnswer(categorizedAnswer))
                .flatMap(childrenWishAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, ChildrenWishAnswer> update(CategorizedAnswer categorizedAnswer) {
        return childrenWishAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedChildrenWishAnswer(categorizedAnswer))
                .flatMap(childrenWishAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<ChildrenWishAnswer, ChildrenWishAnswer> buildUpdatedChildrenWishAnswer(CategorizedAnswer categorizedAnswer) {
        return childrenWishAnswer -> {
            final Long id = childrenWishAnswer.getId();
            final Long quizId = childrenWishAnswer.getQuizId();
            final Long childrenWishId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = childrenWishAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new ChildrenWishAnswer(id, quizId, childrenWishId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, ChildrenWishAnswer> buildChildrenWishAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long childrenWishId = categorizedAnswer.getCategoryInternalId();
            return new ChildrenWishAnswer(null, quizId, childrenWishId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, ChildrenWishAnswer>> retrieveById() {
        return id -> {
            Try<Optional<ChildrenWishAnswer>> optionalTry = childrenWishAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "ChildrenWishAnswer entity was saved but could not be retrieved from db";
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