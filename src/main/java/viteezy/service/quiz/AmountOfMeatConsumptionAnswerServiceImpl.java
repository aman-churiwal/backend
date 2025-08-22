package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AmountOfMeatConsumptionAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfMeatConsumptionAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AmountOfMeatConsumptionAnswerServiceImpl implements AmountOfMeatConsumptionAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmountOfMeatConsumptionAnswerService.class);
    private final AmountOfMeatConsumptionAnswerRepository amountOfMeatConsumptionAnswerRepository;
    private final QuizService quizService;

    public AmountOfMeatConsumptionAnswerServiceImpl(
            AmountOfMeatConsumptionAnswerRepository amountOfMeatConsumptionAnswerRepository,
            QuizService quizService
    ) {
        this.amountOfMeatConsumptionAnswerRepository = amountOfMeatConsumptionAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<AmountOfMeatConsumptionAnswer>> find(Long id) {
        return amountOfMeatConsumptionAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<AmountOfMeatConsumptionAnswer>> find(UUID quizExternalReference) {
        return amountOfMeatConsumptionAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AmountOfMeatConsumptionAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildAmountOfMeatConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfMeatConsumptionAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, AmountOfMeatConsumptionAnswer> update(CategorizedAnswer categorizedAnswer) {
        return amountOfMeatConsumptionAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAmountOfMeatConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfMeatConsumptionAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AmountOfMeatConsumptionAnswer, AmountOfMeatConsumptionAnswer> buildUpdatedAmountOfMeatConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return amountOfMeatConsumptionAnswer -> {
            final Long id = amountOfMeatConsumptionAnswer.getId();
            final Long quizId = amountOfMeatConsumptionAnswer.getQuizId();
            final Long amountOfMeatConsumptionId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = amountOfMeatConsumptionAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AmountOfMeatConsumptionAnswer(id, quizId, amountOfMeatConsumptionId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AmountOfMeatConsumptionAnswer> buildAmountOfMeatConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long amountOfMeatConsumptionId = categorizedAnswer.getCategoryInternalId();
            return new AmountOfMeatConsumptionAnswer(null, quizId, amountOfMeatConsumptionId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AmountOfMeatConsumptionAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AmountOfMeatConsumptionAnswer>> optionalTry = amountOfMeatConsumptionAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AmountOfMeatConsumptionAnswer entity was saved but could not be retrieved from db";
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