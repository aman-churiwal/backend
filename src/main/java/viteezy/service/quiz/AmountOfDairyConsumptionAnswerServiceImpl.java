package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AmountOfDairyConsumptionAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfDairyConsumptionAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AmountOfDairyConsumptionAnswerServiceImpl implements AmountOfDairyConsumptionAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmountOfDairyConsumptionAnswerService.class);
    private final AmountOfDairyConsumptionAnswerRepository amountOfDairyConsumptionAnswerRepository;
    private final QuizService quizService;

    public AmountOfDairyConsumptionAnswerServiceImpl(
            AmountOfDairyConsumptionAnswerRepository amountOfDairyConsumptionAnswerRepository,
            QuizService quizService
    ) {
        this.amountOfDairyConsumptionAnswerRepository = amountOfDairyConsumptionAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<AmountOfDairyConsumptionAnswer>> find(Long id) {
        return amountOfDairyConsumptionAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<AmountOfDairyConsumptionAnswer>> find(UUID quizExternalReference) {
        return amountOfDairyConsumptionAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AmountOfDairyConsumptionAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildAmountOfDairyConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfDairyConsumptionAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, AmountOfDairyConsumptionAnswer> update(CategorizedAnswer categorizedAnswer) {
        return amountOfDairyConsumptionAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAmountOfDairyConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfDairyConsumptionAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AmountOfDairyConsumptionAnswer, AmountOfDairyConsumptionAnswer> buildUpdatedAmountOfDairyConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return amountOfDairyConsumptionAnswer -> {
            final Long id = amountOfDairyConsumptionAnswer.getId();
            final Long quizId = amountOfDairyConsumptionAnswer.getQuizId();
            final Long amountOfDairyConsumptionId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = amountOfDairyConsumptionAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AmountOfDairyConsumptionAnswer(id, quizId, amountOfDairyConsumptionId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AmountOfDairyConsumptionAnswer> buildAmountOfDairyConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long amountOfDairyConsumptionId = categorizedAnswer.getCategoryInternalId();
            return new AmountOfDairyConsumptionAnswer(null, quizId, amountOfDairyConsumptionId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AmountOfDairyConsumptionAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AmountOfDairyConsumptionAnswer>> optionalTry = amountOfDairyConsumptionAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AmountOfDairyConsumptionAnswer entity was saved but could not be retrieved from db";
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