package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.BirthHealthAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.BirthHealthAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class BirthHealthAnswerServiceImpl implements BirthHealthAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BirthHealthAnswerService.class);
    private final BirthHealthAnswerRepository birthHealthAnswerRepository;
    private final QuizService quizService;

    public BirthHealthAnswerServiceImpl(
            BirthHealthAnswerRepository birthHealthAnswerRepository,
            QuizService quizService
    ) {
        this.birthHealthAnswerRepository = birthHealthAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<BirthHealthAnswer>> find(Long id) {
        return birthHealthAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<BirthHealthAnswer>> find(UUID quizExternalReference) {
        return birthHealthAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, BirthHealthAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildBirthHealthAnswer(categorizedAnswer))
                .flatMap(birthHealthAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, BirthHealthAnswer> update(CategorizedAnswer categorizedAnswer) {
        return birthHealthAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedBirthHealthAnswer(categorizedAnswer))
                .flatMap(birthHealthAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<BirthHealthAnswer, BirthHealthAnswer> buildUpdatedBirthHealthAnswer(CategorizedAnswer categorizedAnswer) {
        return birthHealthAnswer -> {
            final Long id = birthHealthAnswer.getId();
            final Long quizId = birthHealthAnswer.getQuizId();
            final Long birthHealthId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = birthHealthAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new BirthHealthAnswer(id, quizId, birthHealthId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, BirthHealthAnswer> buildBirthHealthAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long birthHealthId = categorizedAnswer.getCategoryInternalId();
            return new BirthHealthAnswer(null, quizId, birthHealthId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, BirthHealthAnswer>> retrieveById() {
        return id -> {
            Try<Optional<BirthHealthAnswer>> optionalTry = birthHealthAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "BirthHealthAnswer entity was saved but could not be retrieved from db";
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