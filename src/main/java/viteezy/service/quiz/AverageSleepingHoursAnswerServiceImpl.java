package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AverageSleepingHoursAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AverageSleepingHoursAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AverageSleepingHoursAnswerServiceImpl implements AverageSleepingHoursAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AverageSleepingHoursAnswerService.class);
    private final AverageSleepingHoursAnswerRepository averageSleepingHoursAnswerRepository;
    private final QuizService quizService;

    public AverageSleepingHoursAnswerServiceImpl(
            AverageSleepingHoursAnswerRepository averageSleepingHoursAnswerRepository,
            QuizService quizService
    ) {
        this.averageSleepingHoursAnswerRepository = averageSleepingHoursAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<AverageSleepingHoursAnswer>> find(Long id) {
        return averageSleepingHoursAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<AverageSleepingHoursAnswer>> find(UUID quizExternalReference) {
        return averageSleepingHoursAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AverageSleepingHoursAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildAverageSleepingHoursAnswer(categorizedAnswer))
                .flatMap(averageSleepingHoursAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, AverageSleepingHoursAnswer> update(CategorizedAnswer categorizedAnswer) {
        return averageSleepingHoursAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAverageSleepingHoursAnswer(categorizedAnswer))
                .flatMap(averageSleepingHoursAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AverageSleepingHoursAnswer, AverageSleepingHoursAnswer> buildUpdatedAverageSleepingHoursAnswer(CategorizedAnswer categorizedAnswer) {
        return averageSleepingHoursAnswer -> {
            final Long id = averageSleepingHoursAnswer.getId();
            final Long quizId = averageSleepingHoursAnswer.getQuizId();
            final Long averageSleepingHoursId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = averageSleepingHoursAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AverageSleepingHoursAnswer(id, quizId, averageSleepingHoursId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AverageSleepingHoursAnswer> buildAverageSleepingHoursAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long averageSleepingHoursId = categorizedAnswer.getCategoryInternalId();
            return new AverageSleepingHoursAnswer(null, quizId, averageSleepingHoursId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AverageSleepingHoursAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AverageSleepingHoursAnswer>> optionalTry = averageSleepingHoursAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AverageSleepingHoursAnswer entity was saved but could not be retrieved from db";
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