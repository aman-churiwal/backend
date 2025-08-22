package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AmountOfFruitConsumptionAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfFruitConsumptionAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AmountOfFruitConsumptionAnswerServiceImpl implements AmountOfFruitConsumptionAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmountOfFruitConsumptionAnswerService.class);
    private final AmountOfFruitConsumptionAnswerRepository amountOfFruitConsumptionAnswerRepository;
    private final QuizService quizService;

    public AmountOfFruitConsumptionAnswerServiceImpl(
            AmountOfFruitConsumptionAnswerRepository amountOfFruitConsumptionAnswerRepository,
            QuizService quizService
    ) {
        this.amountOfFruitConsumptionAnswerRepository = amountOfFruitConsumptionAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<AmountOfFruitConsumptionAnswer>> find(Long id) {
        return amountOfFruitConsumptionAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<AmountOfFruitConsumptionAnswer>> find(UUID quizExternalReference) {
        return amountOfFruitConsumptionAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AmountOfFruitConsumptionAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildAmountOfFruitConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfFruitConsumptionAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, AmountOfFruitConsumptionAnswer> update(CategorizedAnswer categorizedAnswer) {
        return amountOfFruitConsumptionAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAmountOfFruitConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfFruitConsumptionAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AmountOfFruitConsumptionAnswer, AmountOfFruitConsumptionAnswer> buildUpdatedAmountOfFruitConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return amountOfFruitConsumptionAnswer -> {
            final Long id = amountOfFruitConsumptionAnswer.getId();
            final Long quizId = amountOfFruitConsumptionAnswer.getQuizId();
            final Long amountOfFruitConsumptionId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = amountOfFruitConsumptionAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AmountOfFruitConsumptionAnswer(id, quizId, amountOfFruitConsumptionId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AmountOfFruitConsumptionAnswer> buildAmountOfFruitConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long amountOfFruitConsumptionId = categorizedAnswer.getCategoryInternalId();
            return new AmountOfFruitConsumptionAnswer(null, quizId, amountOfFruitConsumptionId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AmountOfFruitConsumptionAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AmountOfFruitConsumptionAnswer>> optionalTry = amountOfFruitConsumptionAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AmountOfFruitConsumptionAnswer entity was saved but could not be retrieved from db";
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