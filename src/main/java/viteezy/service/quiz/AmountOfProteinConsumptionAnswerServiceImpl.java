package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AmountOfProteinConsumptionAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AmountOfProteinConsumptionAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class AmountOfProteinConsumptionAnswerServiceImpl implements AmountOfProteinConsumptionAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmountOfProteinConsumptionAnswerService.class);
    private final AmountOfProteinConsumptionAnswerRepository amountOfProteinConsumptionAnswerRepository;
    private final QuizService quizService;

    public AmountOfProteinConsumptionAnswerServiceImpl(
            AmountOfProteinConsumptionAnswerRepository amountOfProteinConsumptionAnswerRepository,
            QuizService quizService
    ) {
        this.amountOfProteinConsumptionAnswerRepository = amountOfProteinConsumptionAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<AmountOfProteinConsumptionAnswer>> find(Long id) {
        return amountOfProteinConsumptionAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<AmountOfProteinConsumptionAnswer>> find(UUID quizExternalReference) {
        return amountOfProteinConsumptionAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, AmountOfProteinConsumptionAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildAmountOfProteinConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfProteinConsumptionAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, AmountOfProteinConsumptionAnswer> update(CategorizedAnswer categorizedAnswer) {
        return amountOfProteinConsumptionAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedAmountOfProteinConsumptionAnswer(categorizedAnswer))
                .flatMap(amountOfProteinConsumptionAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<AmountOfProteinConsumptionAnswer, AmountOfProteinConsumptionAnswer> buildUpdatedAmountOfProteinConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return amountOfProteinConsumptionAnswer -> {
            final Long id = amountOfProteinConsumptionAnswer.getId();
            final Long quizId = amountOfProteinConsumptionAnswer.getQuizId();
            final Long amountOfProteinConsumptionId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = amountOfProteinConsumptionAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new AmountOfProteinConsumptionAnswer(id, quizId, amountOfProteinConsumptionId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, AmountOfProteinConsumptionAnswer> buildAmountOfProteinConsumptionAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long amountOfProteinConsumptionId = categorizedAnswer.getCategoryInternalId();
            return new AmountOfProteinConsumptionAnswer(null, quizId, amountOfProteinConsumptionId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, AmountOfProteinConsumptionAnswer>> retrieveById() {
        return id -> {
            Try<Optional<AmountOfProteinConsumptionAnswer>> optionalTry = amountOfProteinConsumptionAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AmountOfProteinConsumptionAnswer entity was saved but could not be retrieved from db";
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