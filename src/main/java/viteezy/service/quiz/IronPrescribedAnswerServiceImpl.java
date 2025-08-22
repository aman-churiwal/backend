package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.IronPrescribedAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.IronPrescribedAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class IronPrescribedAnswerServiceImpl implements IronPrescribedAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IronPrescribedAnswerService.class);
    private final IronPrescribedAnswerRepository ironPrescribedAnswerRepository;
    private final QuizService quizService;

    public IronPrescribedAnswerServiceImpl(
            IronPrescribedAnswerRepository ironPrescribedAnswerRepository,
            QuizService quizService
    ) {
        this.ironPrescribedAnswerRepository = ironPrescribedAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<IronPrescribedAnswer>> find(Long id) {
        return ironPrescribedAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<IronPrescribedAnswer>> find(UUID quizExternalReference) {
        return ironPrescribedAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, IronPrescribedAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildIronPrescribedAnswer(categorizedAnswer))
                .flatMap(ironPrescribedAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, IronPrescribedAnswer> update(CategorizedAnswer categorizedAnswer) {
        return ironPrescribedAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedIronPrescribedAnswer(categorizedAnswer))
                .flatMap(ironPrescribedAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<IronPrescribedAnswer, IronPrescribedAnswer> buildUpdatedIronPrescribedAnswer(CategorizedAnswer categorizedAnswer) {
        return ironPrescribedAnswer -> {
            final Long id = ironPrescribedAnswer.getId();
            final Long quizId = ironPrescribedAnswer.getQuizId();
            final Long ironPrescribedId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = ironPrescribedAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new IronPrescribedAnswer(id, quizId, ironPrescribedId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, IronPrescribedAnswer> buildIronPrescribedAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long ironPrescribedId = categorizedAnswer.getCategoryInternalId();
            return new IronPrescribedAnswer(null, quizId, ironPrescribedId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, IronPrescribedAnswer>> retrieveById() {
        return id -> {
            Try<Optional<IronPrescribedAnswer>> optionalTry = ironPrescribedAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "IronPrescribedAnswer entity was saved but could not be retrieved from db";
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