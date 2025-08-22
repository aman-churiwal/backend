package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SleepQualityAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.SleepQualityAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class SleepQualityAnswerServiceImpl implements SleepQualityAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SleepQualityAnswerService.class);
    private final SleepQualityAnswerRepository sleepQualityAnswerRepository;
    private final QuizService quizService;

    public SleepQualityAnswerServiceImpl(
            SleepQualityAnswerRepository sleepQualityAnswerRepository,
            QuizService quizService
    ) {
        this.sleepQualityAnswerRepository = sleepQualityAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<SleepQualityAnswer>> find(Long id) {
        return sleepQualityAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<SleepQualityAnswer>> find(UUID quizExternalReference) {
        return sleepQualityAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, SleepQualityAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildSleepQualityAnswer(categorizedAnswer))
                .flatMap(sleepQualityAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, SleepQualityAnswer> update(CategorizedAnswer categorizedAnswer) {
        return sleepQualityAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedSleepQualityAnswer(categorizedAnswer))
                .flatMap(sleepQualityAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<SleepQualityAnswer, SleepQualityAnswer> buildUpdatedSleepQualityAnswer(CategorizedAnswer categorizedAnswer) {
        return sleepQualityAnswer -> {
            final Long id = sleepQualityAnswer.getId();
            final Long quizId = sleepQualityAnswer.getQuizId();
            final Long sleepQualityId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = sleepQualityAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new SleepQualityAnswer(id, quizId, sleepQualityId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, SleepQualityAnswer> buildSleepQualityAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long sleepQualityId = categorizedAnswer.getCategoryInternalId();
            return new SleepQualityAnswer(null, quizId, sleepQualityId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, SleepQualityAnswer>> retrieveById() {
        return id -> {
            Try<Optional<SleepQualityAnswer>> optionalTry = sleepQualityAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "SleepQualityAnswer entity was saved but could not be retrieved from db";
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