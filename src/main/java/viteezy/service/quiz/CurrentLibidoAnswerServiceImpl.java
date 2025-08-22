package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.CurrentLibidoAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.CurrentLibidoAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class CurrentLibidoAnswerServiceImpl implements CurrentLibidoAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentLibidoAnswerService.class);
    private final CurrentLibidoAnswerRepository currentLibidoAnswerRepository;
    private final QuizService quizService;

    public CurrentLibidoAnswerServiceImpl(
            CurrentLibidoAnswerRepository currentLibidoAnswerRepository,
            QuizService quizService
    ) {
        this.currentLibidoAnswerRepository = currentLibidoAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<CurrentLibidoAnswer>> find(Long id) {
        return currentLibidoAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<CurrentLibidoAnswer>> find(UUID quizExternalReference) {
        return currentLibidoAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, CurrentLibidoAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildCurrentLibidoAnswer(categorizedAnswer))
                .flatMap(currentLibidoAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, CurrentLibidoAnswer> update(CategorizedAnswer categorizedAnswer) {
        return currentLibidoAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedCurrentLibidoAnswer(categorizedAnswer))
                .flatMap(currentLibidoAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<CurrentLibidoAnswer, CurrentLibidoAnswer> buildUpdatedCurrentLibidoAnswer(CategorizedAnswer categorizedAnswer) {
        return currentLibidoAnswer -> {
            final Long id = currentLibidoAnswer.getId();
            final Long quizId = currentLibidoAnswer.getQuizId();
            final Long currentLibidoId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = currentLibidoAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new CurrentLibidoAnswer(id, quizId, currentLibidoId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, CurrentLibidoAnswer> buildCurrentLibidoAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long currentLibidoId = categorizedAnswer.getCategoryInternalId();
            return new CurrentLibidoAnswer(null, quizId, currentLibidoId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, CurrentLibidoAnswer>> retrieveById() {
        return id -> {
            Try<Optional<CurrentLibidoAnswer>> optionalTry = currentLibidoAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "CurrentLibidoAnswer entity was saved but could not be retrieved from db";
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