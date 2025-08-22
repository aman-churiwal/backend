package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DateOfBirthAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.quiz.DateOfBirthAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class DateOfBirthAnswerServiceImpl implements DateOfBirthAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateOfBirthAnswerService.class);
    private final DateOfBirthAnswerRepository dateOfBirthAnswerRepository;
    private final QuizRepository quizRepository;

    public DateOfBirthAnswerServiceImpl(
            DateOfBirthAnswerRepository dateOfBirthAnswerRepository, QuizRepository quizRepository
    ) {
        this.dateOfBirthAnswerRepository = dateOfBirthAnswerRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public Either<Throwable, Optional<DateOfBirthAnswer>> find(Long id) {
        return dateOfBirthAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<DateOfBirthAnswer>> find(UUID quizExternalReference) {
        return dateOfBirthAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, DateOfBirthAnswer> save(UUID quizExternalReference, LocalDate date) {
        return quizRepository.find(quizExternalReference)
                .toEither()
                .map(buildDateOfBirthAnswer(date))
                .flatMap(answer -> dateOfBirthAnswerRepository.save(answer).toEither())
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, DateOfBirthAnswer> update(UUID quizExternalReference, LocalDate date) {
        return dateOfBirthAnswerRepository.find(quizExternalReference)
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedDateOfBirthAnswer(date))
                .flatMap(dateOfBirthAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<DateOfBirthAnswer, DateOfBirthAnswer> buildUpdatedDateOfBirthAnswer(LocalDate date) {
        return dateOfBirthAnswer -> {
            final Long id = dateOfBirthAnswer.getId();
            final Long quizId = dateOfBirthAnswer.getQuizId();
            final LocalDateTime creationTimestamp = dateOfBirthAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new DateOfBirthAnswer(id, quizId, date, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, DateOfBirthAnswer> buildDateOfBirthAnswer(LocalDate date) {
        return quiz -> {
            final Long quizId = quiz.getId();
            return new DateOfBirthAnswer(null, quizId, date, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, DateOfBirthAnswer>> retrieveById() {
        return id -> {
            Try<Optional<DateOfBirthAnswer>> optionalTry = dateOfBirthAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DateOfBirthAnswer entity was saved but could not be retrieved from db";
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