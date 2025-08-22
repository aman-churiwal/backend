package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.TransitionPeriodComplaintsAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.TransitionPeriodComplaintsAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class TransitionPeriodComplaintsAnswerServiceImpl implements TransitionPeriodComplaintsAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransitionPeriodComplaintsAnswerService.class);
    private final TransitionPeriodComplaintsAnswerRepository transitionPeriodComplaintsAnswerRepository;
    private final QuizService quizService;

    public TransitionPeriodComplaintsAnswerServiceImpl(
            TransitionPeriodComplaintsAnswerRepository transitionPeriodComplaintsAnswerRepository,
            QuizService quizService
    ) {
        this.transitionPeriodComplaintsAnswerRepository = transitionPeriodComplaintsAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<TransitionPeriodComplaintsAnswer>> find(Long id) {
        return transitionPeriodComplaintsAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<TransitionPeriodComplaintsAnswer>> find(UUID quizExternalReference) {
        return transitionPeriodComplaintsAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, TransitionPeriodComplaintsAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildTransitionPeriodComplaintsAnswer(categorizedAnswer))
                .flatMap(transitionPeriodComplaintsAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, TransitionPeriodComplaintsAnswer> update(CategorizedAnswer categorizedAnswer) {
        return transitionPeriodComplaintsAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedTransitionPeriodComplaintsAnswer(categorizedAnswer))
                .flatMap(transitionPeriodComplaintsAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<TransitionPeriodComplaintsAnswer, TransitionPeriodComplaintsAnswer> buildUpdatedTransitionPeriodComplaintsAnswer(CategorizedAnswer categorizedAnswer) {
        return transitionPeriodComplaintsAnswer -> {
            final Long id = transitionPeriodComplaintsAnswer.getId();
            final Long quizId = transitionPeriodComplaintsAnswer.getQuizId();
            final Long transitionPeriodComplaintsId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = transitionPeriodComplaintsAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new TransitionPeriodComplaintsAnswer(id, quizId, transitionPeriodComplaintsId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, TransitionPeriodComplaintsAnswer> buildTransitionPeriodComplaintsAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long transitionPeriodComplaintsId = categorizedAnswer.getCategoryInternalId();
            return new TransitionPeriodComplaintsAnswer(null, quizId, transitionPeriodComplaintsId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, TransitionPeriodComplaintsAnswer>> retrieveById() {
        return id -> {
            Try<Optional<TransitionPeriodComplaintsAnswer>> optionalTry = transitionPeriodComplaintsAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "TransitionPeriodComplaintsAnswer entity was saved but could not be retrieved from db";
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