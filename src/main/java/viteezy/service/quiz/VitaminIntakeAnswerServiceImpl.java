package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.VitaminIntakeAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.VitaminIntakeAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class VitaminIntakeAnswerServiceImpl implements VitaminIntakeAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VitaminIntakeAnswerService.class);
    private final VitaminIntakeAnswerRepository vitaminIntakeAnswerRepository;
    private final QuizService quizService;

    public VitaminIntakeAnswerServiceImpl(
            VitaminIntakeAnswerRepository vitaminIntakeAnswerRepository,
            QuizService quizService
    ) {
        this.vitaminIntakeAnswerRepository = vitaminIntakeAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<VitaminIntakeAnswer>> find(Long id) {
        return vitaminIntakeAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<VitaminIntakeAnswer>> find(UUID quizExternalReference) {
        return vitaminIntakeAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, VitaminIntakeAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildVitaminIntakeAnswer(categorizedAnswer))
                .flatMap(vitaminIntakeAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, VitaminIntakeAnswer> update(CategorizedAnswer categorizedAnswer) {
        return vitaminIntakeAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedVitaminIntakeAnswer(categorizedAnswer))
                .flatMap(vitaminIntakeAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<VitaminIntakeAnswer, VitaminIntakeAnswer> buildUpdatedVitaminIntakeAnswer(CategorizedAnswer categorizedAnswer) {
        return vitaminIntakeAnswer -> {
            final Long id = vitaminIntakeAnswer.getId();
            final Long quizId = vitaminIntakeAnswer.getQuizId();
            final Long vitaminIntakeId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = vitaminIntakeAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new VitaminIntakeAnswer(id, quizId, vitaminIntakeId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, VitaminIntakeAnswer> buildVitaminIntakeAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long vitaminIntakeId = categorizedAnswer.getCategoryInternalId();
            return new VitaminIntakeAnswer(null, quizId, vitaminIntakeId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, VitaminIntakeAnswer>> retrieveById() {
        return id -> {
            Try<Optional<VitaminIntakeAnswer>> optionalTry = vitaminIntakeAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "VitaminIntakeAnswer entity was saved but could not be retrieved from db";
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