package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.WeeklyTwelveAlcoholicDrinksAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.WeeklyTwelveAlcoholicDrinksAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class WeeklyTwelveAlcoholicDrinksAnswerServiceImpl implements WeeklyTwelveAlcoholicDrinksAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeeklyTwelveAlcoholicDrinksAnswerService.class);
    private final WeeklyTwelveAlcoholicDrinksAnswerRepository weeklyTwelveAlcoholicDrinksAnswerRepository;
    private final QuizService quizService;

    public WeeklyTwelveAlcoholicDrinksAnswerServiceImpl(
            WeeklyTwelveAlcoholicDrinksAnswerRepository weeklyTwelveAlcoholicDrinksAnswerRepository,
            QuizService quizService
    ) {
        this.weeklyTwelveAlcoholicDrinksAnswerRepository = weeklyTwelveAlcoholicDrinksAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<WeeklyTwelveAlcoholicDrinksAnswer>> find(Long id) {
        return weeklyTwelveAlcoholicDrinksAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<WeeklyTwelveAlcoholicDrinksAnswer>> find(UUID quizExternalReference) {
        return weeklyTwelveAlcoholicDrinksAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, WeeklyTwelveAlcoholicDrinksAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildWeeklyTwelveAlcoholicDrinksAnswer(categorizedAnswer))
                .flatMap(weeklyTwelveAlcoholicDrinksAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, WeeklyTwelveAlcoholicDrinksAnswer> update(CategorizedAnswer categorizedAnswer) {
        return weeklyTwelveAlcoholicDrinksAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedWeeklyTwelveAlcoholicDrinksAnswer(categorizedAnswer))
                .flatMap(weeklyTwelveAlcoholicDrinksAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<WeeklyTwelveAlcoholicDrinksAnswer, WeeklyTwelveAlcoholicDrinksAnswer> buildUpdatedWeeklyTwelveAlcoholicDrinksAnswer(CategorizedAnswer categorizedAnswer) {
        return weeklyTwelveAlcoholicDrinksAnswer -> {
            final Long id = weeklyTwelveAlcoholicDrinksAnswer.getId();
            final Long quizId = weeklyTwelveAlcoholicDrinksAnswer.getQuizId();
            final Long weeklyTwelveAlcoholicDrinksId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = weeklyTwelveAlcoholicDrinksAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new WeeklyTwelveAlcoholicDrinksAnswer(id, quizId, weeklyTwelveAlcoholicDrinksId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, WeeklyTwelveAlcoholicDrinksAnswer> buildWeeklyTwelveAlcoholicDrinksAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long weeklyTwelveAlcoholicDrinksId = categorizedAnswer.getCategoryInternalId();
            return new WeeklyTwelveAlcoholicDrinksAnswer(null, quizId, weeklyTwelveAlcoholicDrinksId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, WeeklyTwelveAlcoholicDrinksAnswer>> retrieveById() {
        return id -> {
            Try<Optional<WeeklyTwelveAlcoholicDrinksAnswer>> optionalTry = weeklyTwelveAlcoholicDrinksAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "WeeklyTwelveAlcoholicDrinksAnswer entity was saved but could not be retrieved from db";
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