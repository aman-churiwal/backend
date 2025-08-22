package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SleepHoursLessThanSevenAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.SleepHoursLessThanSevenAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class SleepHoursLessThanSevenAnswerServiceImpl implements SleepHoursLessThanSevenAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SleepHoursLessThanSevenAnswerService.class);
    private final SleepHoursLessThanSevenAnswerRepository sleepHoursLessThanSevenAnswerRepository;
    private final QuizService quizService;

    public SleepHoursLessThanSevenAnswerServiceImpl(
            SleepHoursLessThanSevenAnswerRepository sleepHoursLessThanSevenAnswerRepository,
            QuizService quizService
    ) {
        this.sleepHoursLessThanSevenAnswerRepository = sleepHoursLessThanSevenAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<SleepHoursLessThanSevenAnswer>> find(Long id) {
        return sleepHoursLessThanSevenAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<SleepHoursLessThanSevenAnswer>> find(UUID quizExternalReference) {
        return sleepHoursLessThanSevenAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, SleepHoursLessThanSevenAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildSleepHoursLessThanSevenAnswer(categorizedAnswer))
                .flatMap(sleepHoursLessThanSevenAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, SleepHoursLessThanSevenAnswer> update(CategorizedAnswer categorizedAnswer) {
        return sleepHoursLessThanSevenAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedSleepHoursLessThanSevenAnswer(categorizedAnswer))
                .flatMap(sleepHoursLessThanSevenAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<SleepHoursLessThanSevenAnswer, SleepHoursLessThanSevenAnswer> buildUpdatedSleepHoursLessThanSevenAnswer(CategorizedAnswer categorizedAnswer) {
        return sleepHoursLessThanSevenAnswer -> {
            final Long id = sleepHoursLessThanSevenAnswer.getId();
            final Long quizId = sleepHoursLessThanSevenAnswer.getQuizId();
            final Long sleepHoursLessThanSevenId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = sleepHoursLessThanSevenAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new SleepHoursLessThanSevenAnswer(id, quizId, sleepHoursLessThanSevenId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, SleepHoursLessThanSevenAnswer> buildSleepHoursLessThanSevenAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long sleepHoursLessThanSevenId = categorizedAnswer.getCategoryInternalId();
            return new SleepHoursLessThanSevenAnswer(null, quizId, sleepHoursLessThanSevenId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, SleepHoursLessThanSevenAnswer>> retrieveById() {
        return id -> {
            Try<Optional<SleepHoursLessThanSevenAnswer>> optionalTry = sleepHoursLessThanSevenAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "SleepHoursLessThanSevenAnswer entity was saved but could not be retrieved from db";
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