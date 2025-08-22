package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.EnergyStateAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.EnergyStateAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class EnergyStateAnswerServiceImpl implements EnergyStateAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyStateAnswerService.class);
    private final EnergyStateAnswerRepository energyStateAnswerRepository;
    private final QuizService quizService;

    public EnergyStateAnswerServiceImpl(
            EnergyStateAnswerRepository energyStateAnswerRepository,
            QuizService quizService
    ) {
        this.energyStateAnswerRepository = energyStateAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<EnergyStateAnswer>> find(Long id) {
        return energyStateAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<EnergyStateAnswer>> find(UUID quizExternalReference) {
        return energyStateAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, EnergyStateAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildEnergyStateAnswer(categorizedAnswer))
                .flatMap(energyStateAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, EnergyStateAnswer> update(CategorizedAnswer categorizedAnswer) {
        return energyStateAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedEnergyStateAnswer(categorizedAnswer))
                .flatMap(energyStateAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<EnergyStateAnswer, EnergyStateAnswer> buildUpdatedEnergyStateAnswer(CategorizedAnswer categorizedAnswer) {
        return energyStateAnswer -> {
            final Long id = energyStateAnswer.getId();
            final Long quizId = energyStateAnswer.getQuizId();
            final Long energyStateId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = energyStateAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new EnergyStateAnswer(id, quizId, energyStateId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, EnergyStateAnswer> buildEnergyStateAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long energyStateId = categorizedAnswer.getCategoryInternalId();
            return new EnergyStateAnswer(null, quizId, energyStateId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, EnergyStateAnswer>> retrieveById() {
        return id -> {
            Try<Optional<EnergyStateAnswer>> optionalTry = energyStateAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "EnergyStateAnswer entity was saved but could not be retrieved from db";
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