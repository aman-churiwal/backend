package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AmountOfFiberConsumptionAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfFiberConsumptionAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AmountOfFiberConsumptionAnswerServiceImpl implements AmountOfFiberConsumptionAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmountOfFiberConsumptionAnswerService.class);
    private final AmountOfFiberConsumptionAnswerRepository amountOfFiberConsumptionAnswerRepository;
    private final QuizService quizService;

    public AmountOfFiberConsumptionAnswerServiceImpl(
            AmountOfFiberConsumptionAnswerRepository amountOfFiberConsumptionAnswerRepository,
            QuizService quizService
    ) {
        this.amountOfFiberConsumptionAnswerRepository = amountOfFiberConsumptionAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<AmountOfFiberConsumptionAnswer>> find(Long id) {
        return amountOfFiberConsumptionAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<AmountOfFiberConsumptionAnswer>> find(UUID quizExternalReference) {
        return amountOfFiberConsumptionAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AmountOfFiberConsumptionAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildAmountOfFiberConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfFiberConsumptionAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, AmountOfFiberConsumptionAnswer> update(CategorizedAnswer categorizedAnswer) {
        return amountOfFiberConsumptionAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAmountOfFiberConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfFiberConsumptionAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AmountOfFiberConsumptionAnswer, AmountOfFiberConsumptionAnswer> buildUpdatedAmountOfFiberConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return amountOfFiberConsumptionAnswer -> {
            final Long id = amountOfFiberConsumptionAnswer.getId();
            final Long quizId = amountOfFiberConsumptionAnswer.getQuizId();
            final Long amountOfFiberConsumptionId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = amountOfFiberConsumptionAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AmountOfFiberConsumptionAnswer(id, quizId, amountOfFiberConsumptionId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AmountOfFiberConsumptionAnswer> buildAmountOfFiberConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long amountOfFiberConsumptionId = categorizedAnswer.getCategoryInternalId();
            return new AmountOfFiberConsumptionAnswer(null, quizId, amountOfFiberConsumptionId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AmountOfFiberConsumptionAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AmountOfFiberConsumptionAnswer>> optionalTry = amountOfFiberConsumptionAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AmountOfFiberConsumptionAnswer entity was saved but could not be retrieved from db";
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