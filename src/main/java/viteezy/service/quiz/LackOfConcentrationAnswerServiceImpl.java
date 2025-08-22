package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.LackOfConcentrationAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.LackOfConcentrationAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class LackOfConcentrationAnswerServiceImpl implements LackOfConcentrationAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LackOfConcentrationAnswerService.class);
    private final LackOfConcentrationAnswerRepository lackOfConcentrationAnswerRepository;
    private final QuizService quizService;

    public LackOfConcentrationAnswerServiceImpl(
            LackOfConcentrationAnswerRepository lackOfConcentrationAnswerRepository,
            QuizService quizService
    ) {
        this.lackOfConcentrationAnswerRepository = lackOfConcentrationAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<LackOfConcentrationAnswer>> find(Long id) {
        return lackOfConcentrationAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<LackOfConcentrationAnswer>> find(UUID quizExternalReference) {
        return lackOfConcentrationAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, LackOfConcentrationAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildLackOfConcentrationAnswer(categorizedAnswer))
                .flatMap(lackOfConcentrationAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, LackOfConcentrationAnswer> update(CategorizedAnswer categorizedAnswer) {
        return lackOfConcentrationAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedLackOfConcentrationAnswer(categorizedAnswer))
                .flatMap(lackOfConcentrationAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<LackOfConcentrationAnswer, LackOfConcentrationAnswer> buildUpdatedLackOfConcentrationAnswer(CategorizedAnswer categorizedAnswer) {
        return lackOfConcentrationAnswer -> {
            final Long id = lackOfConcentrationAnswer.getId();
            final Long quizId = lackOfConcentrationAnswer.getQuizId();
            final Long lackOfConcentrationId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = lackOfConcentrationAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new LackOfConcentrationAnswer(id, quizId, lackOfConcentrationId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, LackOfConcentrationAnswer> buildLackOfConcentrationAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long lackOfConcentrationId = categorizedAnswer.getCategoryInternalId();
            return new LackOfConcentrationAnswer(null, quizId, lackOfConcentrationId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, LackOfConcentrationAnswer>> retrieveById() {
        return id -> {
            Try<Optional<LackOfConcentrationAnswer>> optionalTry = lackOfConcentrationAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "LackOfConcentrationAnswer entity was saved but could not be retrieved from db";
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