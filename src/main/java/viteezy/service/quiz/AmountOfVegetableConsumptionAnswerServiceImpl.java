package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AmountOfVegetableConsumptionAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfVegetableConsumptionAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AmountOfVegetableConsumptionAnswerServiceImpl implements AmountOfVegetableConsumptionAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmountOfVegetableConsumptionAnswerService.class);
    private final AmountOfVegetableConsumptionAnswerRepository amountOfVegetableConsumptionAnswerRepository;
    private final QuizService quizService;

    public AmountOfVegetableConsumptionAnswerServiceImpl(
            AmountOfVegetableConsumptionAnswerRepository amountOfVegetableConsumptionAnswerRepository,
            QuizService quizService
    ) {
        this.amountOfVegetableConsumptionAnswerRepository = amountOfVegetableConsumptionAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<AmountOfVegetableConsumptionAnswer>> find(Long id) {
        return amountOfVegetableConsumptionAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<AmountOfVegetableConsumptionAnswer>> find(UUID quizExternalReference) {
        return amountOfVegetableConsumptionAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AmountOfVegetableConsumptionAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildAmountOfVegetableConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfVegetableConsumptionAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, AmountOfVegetableConsumptionAnswer> update(CategorizedAnswer categorizedAnswer) {
        return amountOfVegetableConsumptionAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAmountOfVegetableConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfVegetableConsumptionAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AmountOfVegetableConsumptionAnswer, AmountOfVegetableConsumptionAnswer> buildUpdatedAmountOfVegetableConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return amountOfVegetableConsumptionAnswer -> {
            final Long id = amountOfVegetableConsumptionAnswer.getId();
            final Long quizId = amountOfVegetableConsumptionAnswer.getQuizId();
            final Long amountOfVegetableConsumptionId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = amountOfVegetableConsumptionAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AmountOfVegetableConsumptionAnswer(id, quizId, amountOfVegetableConsumptionId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AmountOfVegetableConsumptionAnswer> buildAmountOfVegetableConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long amountOfVegetableConsumptionId = categorizedAnswer.getCategoryInternalId();
            return new AmountOfVegetableConsumptionAnswer(null, quizId, amountOfVegetableConsumptionId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AmountOfVegetableConsumptionAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AmountOfVegetableConsumptionAnswer>> optionalTry = amountOfVegetableConsumptionAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AmountOfVegetableConsumptionAnswer entity was saved but could not be retrieved from db";
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