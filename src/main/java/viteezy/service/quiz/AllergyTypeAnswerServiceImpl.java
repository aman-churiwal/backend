package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AllergyTypeAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AllergyTypeAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AllergyTypeAnswerServiceImpl implements AllergyTypeAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllergyTypeAnswerService.class);
    private final AllergyTypeAnswerRepository allergyTypeAnswerRepository;
    private final QuizRepository quizRepository;

    public AllergyTypeAnswerServiceImpl(
            AllergyTypeAnswerRepository allergyTypeAnswerRepository,
            QuizRepository quizRepository
    ) {
        this.allergyTypeAnswerRepository = allergyTypeAnswerRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public Either<Throwable, Optional<AllergyTypeAnswer>> find(Long id) {
        return allergyTypeAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<AllergyTypeAnswer>> find(UUID quizExternalReference) {
        return allergyTypeAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AllergyTypeAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizRepository.find(categorizedAnswer.getQuizExternalReference())
                .toEither()
                .map(buildAllergyTypeAnswer(categorizedAnswer))
                .flatMap(answer -> allergyTypeAnswerRepository.save(answer).toEither())
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, Void> delete(CategorizedAnswer categorizedAnswer) {
        return allergyTypeAnswerRepository.find(categorizedAnswer.getQuizExternalReference(), categorizedAnswer.getCategoryInternalId())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAllergyTypeAnswer(categorizedAnswer))
                .flatMap(allergyTypeAnswer -> allergyTypeAnswerRepository.delete(allergyTypeAnswer.getId()))
                .toEither()
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AllergyTypeAnswer, AllergyTypeAnswer> buildUpdatedAllergyTypeAnswer(CategorizedAnswer categorizedAnswer) {
        return allergyTypeAnswer -> {
            final Long id = allergyTypeAnswer.getId();
            final Long quizId = allergyTypeAnswer.getQuizId();
            final Long allergyTypeId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = allergyTypeAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AllergyTypeAnswer(id, quizId, allergyTypeId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AllergyTypeAnswer> buildAllergyTypeAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long allergyTypeId = categorizedAnswer.getCategoryInternalId();
            return new AllergyTypeAnswer(null, quizId, allergyTypeId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AllergyTypeAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AllergyTypeAnswer>> optionalTry = allergyTypeAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AllergyTypeAnswer entity was saved but could not be retrieved from db";
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