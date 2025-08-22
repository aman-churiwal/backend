package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.HealthComplaintsAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.HealthComplaintsAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class HealthComplaintsAnswerServiceImpl implements HealthComplaintsAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthComplaintsAnswerService.class);
    private final HealthComplaintsAnswerRepository healthComplaintsAnswerRepository;
    private final QuizService quizService;

    public HealthComplaintsAnswerServiceImpl(
            HealthComplaintsAnswerRepository healthComplaintsAnswerRepository,
            QuizService quizService
    ) {
        this.healthComplaintsAnswerRepository = healthComplaintsAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<HealthComplaintsAnswer>> find(Long id) {
        return healthComplaintsAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<HealthComplaintsAnswer>> find(UUID quizExternalReference) {
        return healthComplaintsAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, HealthComplaintsAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildHealthComplaintsAnswer(categorizedAnswer))
                .flatMap(healthComplaintsAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, HealthComplaintsAnswer> update(CategorizedAnswer categorizedAnswer) {
        return healthComplaintsAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedHealthComplaintsAnswer(categorizedAnswer))
                .flatMap(healthComplaintsAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<HealthComplaintsAnswer, HealthComplaintsAnswer> buildUpdatedHealthComplaintsAnswer(CategorizedAnswer categorizedAnswer) {
        return healthComplaintsAnswer -> {
            final Long id = healthComplaintsAnswer.getId();
            final Long quizId = healthComplaintsAnswer.getQuizId();
            final Long healthComplaintsId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = healthComplaintsAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new HealthComplaintsAnswer(id, quizId, healthComplaintsId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, HealthComplaintsAnswer> buildHealthComplaintsAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long healthComplaintsId = categorizedAnswer.getCategoryInternalId();
            return new HealthComplaintsAnswer(null, quizId, healthComplaintsId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, HealthComplaintsAnswer>> retrieveById() {
        return id -> {
            Try<Optional<HealthComplaintsAnswer>> optionalTry = healthComplaintsAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "HealthComplaintsAnswer entity was saved but could not be retrieved from db";
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