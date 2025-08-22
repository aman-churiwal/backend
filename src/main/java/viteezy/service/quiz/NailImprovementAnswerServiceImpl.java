package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.NailImprovementAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.NailImprovementAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class NailImprovementAnswerServiceImpl implements NailImprovementAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NailImprovementAnswerService.class);
    private final NailImprovementAnswerRepository nailImprovementAnswerRepository;
    private final QuizService quizService;

    public NailImprovementAnswerServiceImpl(
            NailImprovementAnswerRepository nailImprovementAnswerRepository,
            QuizService quizService
    ) {
        this.nailImprovementAnswerRepository = nailImprovementAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<NailImprovementAnswer>> find(Long id) {
        return nailImprovementAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<NailImprovementAnswer>> find(UUID quizExternalReference) {
        return nailImprovementAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, NailImprovementAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildNailImprovementAnswer(categorizedAnswer))
                .flatMap(nailImprovementAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, NailImprovementAnswer> update(CategorizedAnswer categorizedAnswer) {
        return nailImprovementAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedNailImprovementAnswer(categorizedAnswer))
                .flatMap(nailImprovementAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<NailImprovementAnswer, NailImprovementAnswer> buildUpdatedNailImprovementAnswer(CategorizedAnswer categorizedAnswer) {
        return nailImprovementAnswer -> {
            final Long id = nailImprovementAnswer.getId();
            final Long quizId = nailImprovementAnswer.getQuizId();
            final Long nailImprovementId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = nailImprovementAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new NailImprovementAnswer(id, quizId, nailImprovementId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, NailImprovementAnswer> buildNailImprovementAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long nailImprovementId = categorizedAnswer.getCategoryInternalId();
            return new NailImprovementAnswer(null, quizId, nailImprovementId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, NailImprovementAnswer>> retrieveById() {
        return id -> {
            Try<Optional<NailImprovementAnswer>> optionalTry = nailImprovementAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "NailImprovementAnswer entity was saved but could not be retrieved from db";
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