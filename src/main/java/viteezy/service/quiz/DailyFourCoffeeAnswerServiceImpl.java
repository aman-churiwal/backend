package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DailyFourCoffeeAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DailyFourCoffeeAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class DailyFourCoffeeAnswerServiceImpl implements DailyFourCoffeeAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyFourCoffeeAnswerService.class);
    private final DailyFourCoffeeAnswerRepository dailyFourCoffeeAnswerRepository;
    private final QuizService quizService;

    public DailyFourCoffeeAnswerServiceImpl(
            DailyFourCoffeeAnswerRepository dailyFourCoffeeAnswerRepository,
            QuizService quizService
    ) {
        this.dailyFourCoffeeAnswerRepository = dailyFourCoffeeAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<DailyFourCoffeeAnswer>> find(Long id) {
        return dailyFourCoffeeAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<DailyFourCoffeeAnswer>> find(UUID quizExternalReference) {
        return dailyFourCoffeeAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, DailyFourCoffeeAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildDailyFourCoffeeAnswer(categorizedAnswer))
                .flatMap(dailyFourCoffeeAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, DailyFourCoffeeAnswer> update(CategorizedAnswer categorizedAnswer) {
        return dailyFourCoffeeAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedDailyFourCoffeeAnswer(categorizedAnswer))
                .flatMap(dailyFourCoffeeAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<DailyFourCoffeeAnswer, DailyFourCoffeeAnswer> buildUpdatedDailyFourCoffeeAnswer(CategorizedAnswer categorizedAnswer) {
        return dailyFourCoffeeAnswer -> {
            final Long id = dailyFourCoffeeAnswer.getId();
            final Long quizId = dailyFourCoffeeAnswer.getQuizId();
            final Long dailyFourCoffeeId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = dailyFourCoffeeAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new DailyFourCoffeeAnswer(id, quizId, dailyFourCoffeeId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, DailyFourCoffeeAnswer> buildDailyFourCoffeeAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long dailyFourCoffeeId = categorizedAnswer.getCategoryInternalId();
            return new DailyFourCoffeeAnswer(null, quizId, dailyFourCoffeeId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, DailyFourCoffeeAnswer>> retrieveById() {
        return id -> {
            Try<Optional<DailyFourCoffeeAnswer>> optionalTry = dailyFourCoffeeAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DailyFourCoffeeAnswer entity was saved but could not be retrieved from db";
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