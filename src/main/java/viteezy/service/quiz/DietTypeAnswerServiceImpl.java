package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DietTypeAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DietTypeAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class DietTypeAnswerServiceImpl implements DietTypeAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DietTypeAnswerService.class);
    private final DietTypeAnswerRepository dietTypeAnswerRepository;
    private final QuizRepository quizRepository;

    public DietTypeAnswerServiceImpl(
            DietTypeAnswerRepository dietTypeAnswerRepository,
            QuizRepository quizRepository
    ) {
        this.dietTypeAnswerRepository = dietTypeAnswerRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public Either<Throwable, Optional<DietTypeAnswer>> find(Long id) {
        return dietTypeAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<DietTypeAnswer>> find(UUID quizExternalReference) {
        return dietTypeAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, DietTypeAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizRepository.find(categorizedAnswer.getQuizExternalReference())
                .toEither()
                .map(buildDietTypeAnswer(categorizedAnswer))
                .flatMap(answer -> dietTypeAnswerRepository.save(answer).toEither())
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, DietTypeAnswer> update(CategorizedAnswer categorizedAnswer) {
        return dietTypeAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedDietTypeAnswer(categorizedAnswer))
                .flatMap(dietTypeAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<DietTypeAnswer, DietTypeAnswer> buildUpdatedDietTypeAnswer(CategorizedAnswer categorizedAnswer) {
        return dietTypeAnswer -> {
            final Long id = dietTypeAnswer.getId();
            final Long quizId = dietTypeAnswer.getQuizId();
            final Long dietTypeId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = dietTypeAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new DietTypeAnswer(id, quizId, dietTypeId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, DietTypeAnswer> buildDietTypeAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long dietTypeId = categorizedAnswer.getCategoryInternalId();
            return new DietTypeAnswer(null, quizId, dietTypeId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, DietTypeAnswer>> retrieveById() {
        return id -> {
            Try<Optional<DietTypeAnswer>> optionalTry = dietTypeAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DietTypeAnswer entity was saved but could not be retrieved from db";
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