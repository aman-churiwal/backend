package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.LoseWeightChallengeAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.LoseWeightChallengeAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class LoseWeightChallengeAnswerServiceImpl implements LoseWeightChallengeAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoseWeightChallengeAnswerService.class);
    private final LoseWeightChallengeAnswerRepository loseWeightChallengeAnswerRepository;
    private final QuizService quizService;

    public LoseWeightChallengeAnswerServiceImpl(
            LoseWeightChallengeAnswerRepository loseWeightChallengeAnswerRepository,
            QuizService quizService
    ) {
        this.loseWeightChallengeAnswerRepository = loseWeightChallengeAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<LoseWeightChallengeAnswer>> find(Long id) {
        return loseWeightChallengeAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<LoseWeightChallengeAnswer>> find(UUID quizExternalReference) {
        return loseWeightChallengeAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, LoseWeightChallengeAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildLoseWeightChallengeAnswer(categorizedAnswer))
                .flatMap(loseWeightChallengeAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, LoseWeightChallengeAnswer> update(CategorizedAnswer categorizedAnswer) {
        return loseWeightChallengeAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedLoseWeightChallengeAnswer(categorizedAnswer))
                .flatMap(loseWeightChallengeAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<LoseWeightChallengeAnswer, LoseWeightChallengeAnswer> buildUpdatedLoseWeightChallengeAnswer(CategorizedAnswer categorizedAnswer) {
        return loseWeightChallengeAnswer -> {
            final Long id = loseWeightChallengeAnswer.getId();
            final Long quizId = loseWeightChallengeAnswer.getQuizId();
            final Long loseWeightChallengeId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = loseWeightChallengeAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new LoseWeightChallengeAnswer(id, quizId, loseWeightChallengeId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, LoseWeightChallengeAnswer> buildLoseWeightChallengeAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long loseWeightChallengeId = categorizedAnswer.getCategoryInternalId();
            return new LoseWeightChallengeAnswer(null, quizId, loseWeightChallengeId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, LoseWeightChallengeAnswer>> retrieveById() {
        return id -> {
            Try<Optional<LoseWeightChallengeAnswer>> optionalTry = loseWeightChallengeAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "LoseWeightChallengeAnswer entity was saved but could not be retrieved from db";
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