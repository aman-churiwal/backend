package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DietIntoleranceAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DietIntoleranceAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class DietIntoleranceAnswerServiceImpl implements DietIntoleranceAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DietIntoleranceAnswerService.class);
    private final DietIntoleranceAnswerRepository dietIntoleranceAnswerRepository;
    private final QuizService quizService;

    public DietIntoleranceAnswerServiceImpl(
            DietIntoleranceAnswerRepository dietIntoleranceAnswerRepository,
            QuizService quizService
    ) {
        this.dietIntoleranceAnswerRepository = dietIntoleranceAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<DietIntoleranceAnswer>> find(Long id) {
        return dietIntoleranceAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<DietIntoleranceAnswer>> find(UUID quizExternalReference) {
        return dietIntoleranceAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, DietIntoleranceAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildDietIntoleranceAnswer(categorizedAnswer))
                .flatMap(dietIntoleranceAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, Void> delete(CategorizedAnswer categorizedAnswer) {
        return dietIntoleranceAnswerRepository.find(categorizedAnswer.getQuizExternalReference(), categorizedAnswer.getCategoryInternalId())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedDietIntoleranceAnswer(categorizedAnswer))
                .flatMap(allergyTypeAnswer -> dietIntoleranceAnswerRepository.delete(allergyTypeAnswer.getId()))
                .toEither()
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<DietIntoleranceAnswer, DietIntoleranceAnswer> buildUpdatedDietIntoleranceAnswer(CategorizedAnswer categorizedAnswer) {
        return dietIntoleranceAnswer -> {
            final Long id = dietIntoleranceAnswer.getId();
            final Long quizId = dietIntoleranceAnswer.getQuizId();
            final Long dietIntoleranceId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = dietIntoleranceAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new DietIntoleranceAnswer(id, quizId, dietIntoleranceId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, DietIntoleranceAnswer> buildDietIntoleranceAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long dietIntoleranceId = categorizedAnswer.getCategoryInternalId();
            return new DietIntoleranceAnswer(null, quizId, dietIntoleranceId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, DietIntoleranceAnswer>> retrieveById() {
        return id -> {
            Try<Optional<DietIntoleranceAnswer>> optionalTry = dietIntoleranceAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DietIntoleranceAnswer entity was saved but could not be retrieved from db";
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