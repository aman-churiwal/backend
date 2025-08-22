package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.UrinaryInfectionAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.UrinaryInfectionAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class UrinaryInfectionAnswerServiceImpl implements UrinaryInfectionAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrinaryInfectionAnswerService.class);
    private final UrinaryInfectionAnswerRepository urinaryInfectionAnswerRepository;
    private final QuizService quizService;

    public UrinaryInfectionAnswerServiceImpl(
            UrinaryInfectionAnswerRepository urinaryInfectionAnswerRepository,
            QuizService quizService
    ) {
        this.urinaryInfectionAnswerRepository = urinaryInfectionAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<UrinaryInfectionAnswer>> find(Long id) {
        return urinaryInfectionAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<UrinaryInfectionAnswer>> find(UUID quizExternalReference) {
        return urinaryInfectionAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, UrinaryInfectionAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildUrinaryInfectionAnswer(categorizedAnswer))
                .flatMap(urinaryInfectionAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, UrinaryInfectionAnswer> update(CategorizedAnswer categorizedAnswer) {
        return urinaryInfectionAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedUrinaryInfectionAnswer(categorizedAnswer))
                .flatMap(urinaryInfectionAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<UrinaryInfectionAnswer, UrinaryInfectionAnswer> buildUpdatedUrinaryInfectionAnswer(CategorizedAnswer categorizedAnswer) {
        return urinaryInfectionAnswer -> {
            final Long id = urinaryInfectionAnswer.getId();
            final Long quizId = urinaryInfectionAnswer.getQuizId();
            final Long urinaryInfectionId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = urinaryInfectionAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new UrinaryInfectionAnswer(id, quizId, urinaryInfectionId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, UrinaryInfectionAnswer> buildUrinaryInfectionAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long urinaryInfectionId = categorizedAnswer.getCategoryInternalId();
            return new UrinaryInfectionAnswer(null, quizId, urinaryInfectionId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, UrinaryInfectionAnswer>> retrieveById() {
        return id -> {
            Try<Optional<UrinaryInfectionAnswer>> optionalTry = urinaryInfectionAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "UrinaryInfectionAnswer entity was saved but could not be retrieved from db";
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