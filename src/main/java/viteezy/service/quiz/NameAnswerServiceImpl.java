package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.NameAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.quiz.NameAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class NameAnswerServiceImpl implements NameAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NameAnswerService.class);
    private final NameAnswerRepository nameAnswerRepository;
    private final QuizRepository quizRepository;

    public NameAnswerServiceImpl(
            NameAnswerRepository nameAnswerRepository, QuizRepository quizRepository
    ) {
        this.nameAnswerRepository = nameAnswerRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public Either<Throwable, Optional<NameAnswer>> find(Long id) {
        return nameAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<NameAnswer>> find(UUID quizExternalReference) {
        return nameAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, NameAnswer> save(UUID quizExternalReference, String name) {
        return quizRepository.find(quizExternalReference)
                .toEither()
                .map(buildNameAnswer(name))
                .flatMap(answer -> nameAnswerRepository.save(answer).toEither())
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, NameAnswer> update(UUID quizExternalReference, String name) {
        return nameAnswerRepository.find(quizExternalReference)
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedNameAnswer(name))
                .flatMap(nameAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<NameAnswer, NameAnswer> buildUpdatedNameAnswer(String name) {
        return nameAnswer -> {
            final Long id = nameAnswer.getId();
            final Long quizId = nameAnswer.getQuizId();
            final LocalDateTime creationTimestamp = nameAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new NameAnswer(id, quizId, name, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, NameAnswer> buildNameAnswer(String name) {
        return quiz -> {
            final Long quizId = quiz.getId();
            return new NameAnswer(null, quizId, name, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, NameAnswer>> retrieveById() {
        return id -> {
            Try<Optional<NameAnswer>> optionalTry = nameAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "NameAnswer entity was saved but could not be retrieved from db";
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