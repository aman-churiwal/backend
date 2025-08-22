package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.BingeEatingAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.BingeEatingAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class BingeEatingAnswerServiceImpl implements BingeEatingAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BingeEatingAnswerService.class);
    private final BingeEatingAnswerRepository bingeEatingAnswerRepository;
    private final QuizService quizService;

    public BingeEatingAnswerServiceImpl(
            BingeEatingAnswerRepository bingeEatingAnswerRepository,
            QuizService quizService
    ) {
        this.bingeEatingAnswerRepository = bingeEatingAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<BingeEatingAnswer>> find(Long id) {
        return bingeEatingAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<BingeEatingAnswer>> find(UUID quizExternalReference) {
        return bingeEatingAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, BingeEatingAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildBingeEatingAnswer(categorizedAnswer))
                .flatMap(bingeEatingAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, BingeEatingAnswer> update(CategorizedAnswer categorizedAnswer) {
        return bingeEatingAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedBingeEatingAnswer(categorizedAnswer))
                .flatMap(bingeEatingAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<BingeEatingAnswer, BingeEatingAnswer> buildUpdatedBingeEatingAnswer(CategorizedAnswer categorizedAnswer) {
        return bingeEatingAnswer -> {
            final Long id = bingeEatingAnswer.getId();
            final Long quizId = bingeEatingAnswer.getQuizId();
            final Long bingeEatingId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = bingeEatingAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new BingeEatingAnswer(id, quizId, bingeEatingId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, BingeEatingAnswer> buildBingeEatingAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long bingeEatingId = categorizedAnswer.getCategoryInternalId();
            return new BingeEatingAnswer(null, quizId, bingeEatingId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, BingeEatingAnswer>> retrieveById() {
        return id -> {
            Try<Optional<BingeEatingAnswer>> optionalTry = bingeEatingAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "BingeEatingAnswer entity was saved but could not be retrieved from db";
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