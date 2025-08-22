package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AmountOfFishConsumptionAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfFishConsumptionAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AmountOfFishConsumptionAnswerServiceImpl implements AmountOfFishConsumptionAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmountOfFishConsumptionAnswerService.class);
    private final AmountOfFishConsumptionAnswerRepository amountOfFishConsumptionAnswerRepository;
    private final QuizService quizService;

    public AmountOfFishConsumptionAnswerServiceImpl(
            AmountOfFishConsumptionAnswerRepository amountOfFishConsumptionAnswerRepository,
            QuizService quizService
    ) {
        this.amountOfFishConsumptionAnswerRepository = amountOfFishConsumptionAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<AmountOfFishConsumptionAnswer>> find(Long id) {
        return amountOfFishConsumptionAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<AmountOfFishConsumptionAnswer>> find(UUID quizExternalReference) {
        return amountOfFishConsumptionAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AmountOfFishConsumptionAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildAmountOfFishConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfFishConsumptionAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, AmountOfFishConsumptionAnswer> update(CategorizedAnswer categorizedAnswer) {
        return amountOfFishConsumptionAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAmountOfFishConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfFishConsumptionAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AmountOfFishConsumptionAnswer, AmountOfFishConsumptionAnswer> buildUpdatedAmountOfFishConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return amountOfFishConsumptionAnswer -> {
            final Long id = amountOfFishConsumptionAnswer.getId();
            final Long quizId = amountOfFishConsumptionAnswer.getQuizId();
            final Long amountOfFishConsumptionId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = amountOfFishConsumptionAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AmountOfFishConsumptionAnswer(id, quizId, amountOfFishConsumptionId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AmountOfFishConsumptionAnswer> buildAmountOfFishConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long amountOfFishConsumptionId = categorizedAnswer.getCategoryInternalId();
            return new AmountOfFishConsumptionAnswer(null, quizId, amountOfFishConsumptionId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AmountOfFishConsumptionAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AmountOfFishConsumptionAnswer>> optionalTry = amountOfFishConsumptionAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AmountOfFishConsumptionAnswer entity was saved but could not be retrieved from db";
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