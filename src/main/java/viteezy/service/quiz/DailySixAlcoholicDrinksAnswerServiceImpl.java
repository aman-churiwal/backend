package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DailySixAlcoholicDrinksAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DailySixAlcoholicDrinksAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class DailySixAlcoholicDrinksAnswerServiceImpl implements DailySixAlcoholicDrinksAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailySixAlcoholicDrinksAnswerService.class);
    private final DailySixAlcoholicDrinksAnswerRepository dailySixAlcoholicDrinksAnswerRepository;
    private final QuizService quizService;

    public DailySixAlcoholicDrinksAnswerServiceImpl(
            DailySixAlcoholicDrinksAnswerRepository dailySixAlcoholicDrinksAnswerRepository,
            QuizService quizService
    ) {
        this.dailySixAlcoholicDrinksAnswerRepository = dailySixAlcoholicDrinksAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<DailySixAlcoholicDrinksAnswer>> find(Long id) {
        return dailySixAlcoholicDrinksAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<DailySixAlcoholicDrinksAnswer>> find(UUID quizExternalReference) {
        return dailySixAlcoholicDrinksAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, DailySixAlcoholicDrinksAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildDailySixAlcoholicDrinksAnswer(categorizedAnswer))
                .flatMap(dailySixAlcoholicDrinksAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, DailySixAlcoholicDrinksAnswer> update(CategorizedAnswer categorizedAnswer) {
        return dailySixAlcoholicDrinksAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedDailySixAlcoholicDrinksAnswer(categorizedAnswer))
                .flatMap(dailySixAlcoholicDrinksAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<DailySixAlcoholicDrinksAnswer, DailySixAlcoholicDrinksAnswer> buildUpdatedDailySixAlcoholicDrinksAnswer(CategorizedAnswer categorizedAnswer) {
        return dailySixAlcoholicDrinksAnswer -> {
            final Long id = dailySixAlcoholicDrinksAnswer.getId();
            final Long quizId = dailySixAlcoholicDrinksAnswer.getQuizId();
            final Long dailySixAlcoholicDrinksId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = dailySixAlcoholicDrinksAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new DailySixAlcoholicDrinksAnswer(id, quizId, dailySixAlcoholicDrinksId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, DailySixAlcoholicDrinksAnswer> buildDailySixAlcoholicDrinksAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long dailySixAlcoholicDrinksId = categorizedAnswer.getCategoryInternalId();
            return new DailySixAlcoholicDrinksAnswer(null, quizId, dailySixAlcoholicDrinksId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, DailySixAlcoholicDrinksAnswer>> retrieveById() {
        return id -> {
            Try<Optional<DailySixAlcoholicDrinksAnswer>> optionalTry = dailySixAlcoholicDrinksAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DailySixAlcoholicDrinksAnswer entity was saved but could not be retrieved from db";
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