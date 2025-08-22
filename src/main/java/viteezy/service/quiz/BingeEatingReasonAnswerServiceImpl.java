package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.BingeEatingReasonAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.BingeEatingReasonAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class BingeEatingReasonAnswerServiceImpl implements BingeEatingReasonAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BingeEatingReasonAnswerService.class);
    private final BingeEatingReasonAnswerRepository bingeEatingReasonAnswerRepository;
    private final QuizService quizService;

    public BingeEatingReasonAnswerServiceImpl(
            BingeEatingReasonAnswerRepository bingeEatingReasonAnswerRepository,
            QuizService quizService
    ) {
        this.bingeEatingReasonAnswerRepository = bingeEatingReasonAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<BingeEatingReasonAnswer>> find(Long id) {
        return bingeEatingReasonAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<BingeEatingReasonAnswer>> find(UUID quizExternalReference) {
        return bingeEatingReasonAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, BingeEatingReasonAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildBingeEatingReasonAnswer(categorizedAnswer))
                .flatMap(bingeEatingReasonAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, BingeEatingReasonAnswer> update(CategorizedAnswer categorizedAnswer) {
        return bingeEatingReasonAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedBingeEatingReasonAnswer(categorizedAnswer))
                .flatMap(bingeEatingReasonAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<BingeEatingReasonAnswer, BingeEatingReasonAnswer> buildUpdatedBingeEatingReasonAnswer(CategorizedAnswer categorizedAnswer) {
        return bingeEatingReasonAnswer -> {
            final Long id = bingeEatingReasonAnswer.getId();
            final Long quizId = bingeEatingReasonAnswer.getQuizId();
            final Long bingeEatingReasonId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = bingeEatingReasonAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new BingeEatingReasonAnswer(id, quizId, bingeEatingReasonId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, BingeEatingReasonAnswer> buildBingeEatingReasonAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long bingeEatingReasonId = categorizedAnswer.getCategoryInternalId();
            return new BingeEatingReasonAnswer(null, quizId, bingeEatingReasonId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, BingeEatingReasonAnswer>> retrieveById() {
        return id -> {
            Try<Optional<BingeEatingReasonAnswer>> optionalTry = bingeEatingReasonAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "BingeEatingReasonAnswer entity was saved but could not be retrieved from db";
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