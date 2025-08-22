package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.NewProductAvailableAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.NewProductAvailableAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class NewProductAvailableAnswerServiceImpl implements NewProductAvailableAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewProductAvailableAnswerService.class);
    private final NewProductAvailableAnswerRepository newProductAvailableAnswerRepository;
    private final QuizService quizService;

    public NewProductAvailableAnswerServiceImpl(
            NewProductAvailableAnswerRepository newProductAvailableAnswerRepository,
            QuizService quizService
    ) {
        this.newProductAvailableAnswerRepository = newProductAvailableAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<NewProductAvailableAnswer>> find(Long id) {
        return newProductAvailableAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<NewProductAvailableAnswer>> find(UUID quizExternalReference) {
        return newProductAvailableAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, NewProductAvailableAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildNewProductAvailableAnswer(categorizedAnswer))
                .flatMap(newProductAvailableAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, NewProductAvailableAnswer> update(CategorizedAnswer categorizedAnswer) {
        return newProductAvailableAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedNewProductAvailableAnswer(categorizedAnswer))
                .flatMap(newProductAvailableAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<NewProductAvailableAnswer, NewProductAvailableAnswer> buildUpdatedNewProductAvailableAnswer(CategorizedAnswer categorizedAnswer) {
        return newProductAvailableAnswer -> {
            final Long id = newProductAvailableAnswer.getId();
            final Long quizId = newProductAvailableAnswer.getQuizId();
            final Long newProductAvailableId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = newProductAvailableAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new NewProductAvailableAnswer(id, quizId, newProductAvailableId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, NewProductAvailableAnswer> buildNewProductAvailableAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long newProductAvailableId = categorizedAnswer.getCategoryInternalId();
            return new NewProductAvailableAnswer(null, quizId, newProductAvailableId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, NewProductAvailableAnswer>> retrieveById() {
        return id -> {
            Try<Optional<NewProductAvailableAnswer>> optionalTry = newProductAvailableAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "NewProductAvailableAnswer entity was saved but could not be retrieved from db";
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