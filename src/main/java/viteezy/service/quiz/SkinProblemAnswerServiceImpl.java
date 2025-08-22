package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SkinProblemAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.SkinProblemAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class SkinProblemAnswerServiceImpl implements SkinProblemAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkinProblemAnswerService.class);
    private final SkinProblemAnswerRepository skinProblemAnswerRepository;
    private final QuizService quizService;

    public SkinProblemAnswerServiceImpl(
            SkinProblemAnswerRepository skinProblemAnswerRepository,
            QuizService quizService
    ) {
        this.skinProblemAnswerRepository = skinProblemAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<SkinProblemAnswer>> find(Long id) {
        return skinProblemAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<SkinProblemAnswer>> find(UUID quizExternalReference) {
        return skinProblemAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, SkinProblemAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildSkinProblemAnswer(categorizedAnswer))
                .flatMap(skinProblemAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, SkinProblemAnswer> update(CategorizedAnswer categorizedAnswer) {
        return skinProblemAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedSkinProblemAnswer(categorizedAnswer))
                .flatMap(skinProblemAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<SkinProblemAnswer, SkinProblemAnswer> buildUpdatedSkinProblemAnswer(CategorizedAnswer categorizedAnswer) {
        return skinProblemAnswer -> {
            final Long id = skinProblemAnswer.getId();
            final Long quizId = skinProblemAnswer.getQuizId();
            final Long skinProblemId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = skinProblemAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new SkinProblemAnswer(id, quizId, skinProblemId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, SkinProblemAnswer> buildSkinProblemAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long skinProblemId = categorizedAnswer.getCategoryInternalId();
            return new SkinProblemAnswer(null, quizId, skinProblemId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, SkinProblemAnswer>> retrieveById() {
        return id -> {
            Try<Optional<SkinProblemAnswer>> optionalTry = skinProblemAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "SkinProblemAnswer entity was saved but could not be retrieved from db";
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