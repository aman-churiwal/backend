package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.LibidoStressLevelAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.LibidoStressLevelAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class LibidoStressLevelAnswerServiceImpl implements LibidoStressLevelAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibidoStressLevelAnswerService.class);
    private final LibidoStressLevelAnswerRepository libidoStressLevelAnswerRepository;
    private final QuizService quizService;

    public LibidoStressLevelAnswerServiceImpl(
            LibidoStressLevelAnswerRepository libidoStressLevelAnswerRepository,
            QuizService quizService
    ) {
        this.libidoStressLevelAnswerRepository = libidoStressLevelAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<LibidoStressLevelAnswer>> find(Long id) {
        return libidoStressLevelAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<LibidoStressLevelAnswer>> find(UUID quizExternalReference) {
        return libidoStressLevelAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, LibidoStressLevelAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildLibidoStressLevelAnswer(categorizedAnswer))
                .flatMap(libidoStressLevelAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, LibidoStressLevelAnswer> update(CategorizedAnswer categorizedAnswer) {
        return libidoStressLevelAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedLibidoStressLevelAnswer(categorizedAnswer))
                .flatMap(libidoStressLevelAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<LibidoStressLevelAnswer, LibidoStressLevelAnswer> buildUpdatedLibidoStressLevelAnswer(CategorizedAnswer categorizedAnswer) {
        return libidoStressLevelAnswer -> {
            final Long id = libidoStressLevelAnswer.getId();
            final Long quizId = libidoStressLevelAnswer.getQuizId();
            final Long libidoStressLevelId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = libidoStressLevelAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new LibidoStressLevelAnswer(id, quizId, libidoStressLevelId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, LibidoStressLevelAnswer> buildLibidoStressLevelAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long libidoStressLevelId = categorizedAnswer.getCategoryInternalId();
            return new LibidoStressLevelAnswer(null, quizId, libidoStressLevelId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, LibidoStressLevelAnswer>> retrieveById() {
        return id -> {
            Try<Optional<LibidoStressLevelAnswer>> optionalTry = libidoStressLevelAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "LibidoStressLevelAnswer entity was saved but could not be retrieved from db";
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