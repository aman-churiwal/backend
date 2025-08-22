package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.VitaminOpinionAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.VitaminOpinionAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class VitaminOpinionAnswerServiceImpl implements VitaminOpinionAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VitaminOpinionAnswerService.class);
    private final VitaminOpinionAnswerRepository vitaminOpinionAnswerRepository;
    private final QuizService quizService;

    public VitaminOpinionAnswerServiceImpl(
            VitaminOpinionAnswerRepository vitaminOpinionAnswerRepository,
            QuizService quizService
    ) {
        this.vitaminOpinionAnswerRepository = vitaminOpinionAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<VitaminOpinionAnswer>> find(Long id) {
        return vitaminOpinionAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<VitaminOpinionAnswer>> find(UUID quizExternalReference) {
        return vitaminOpinionAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, VitaminOpinionAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildVitaminOpinionAnswer(categorizedAnswer))
                .flatMap(vitaminOpinionAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, VitaminOpinionAnswer> update(CategorizedAnswer categorizedAnswer) {
        return vitaminOpinionAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedVitaminOpinionAnswer(categorizedAnswer))
                .flatMap(vitaminOpinionAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<VitaminOpinionAnswer, VitaminOpinionAnswer> buildUpdatedVitaminOpinionAnswer(CategorizedAnswer categorizedAnswer) {
        return vitaminOpinionAnswer -> {
            final Long id = vitaminOpinionAnswer.getId();
            final Long quizId = vitaminOpinionAnswer.getQuizId();
            final Long vitaminOpinionId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = vitaminOpinionAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new VitaminOpinionAnswer(id, quizId, vitaminOpinionId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, VitaminOpinionAnswer> buildVitaminOpinionAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long vitaminOpinionId = categorizedAnswer.getCategoryInternalId();
            return new VitaminOpinionAnswer(null, quizId, vitaminOpinionId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, VitaminOpinionAnswer>> retrieveById() {
        return id -> {
            Try<Optional<VitaminOpinionAnswer>> optionalTry = vitaminOpinionAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "VitaminOpinionAnswer entity was saved but could not be retrieved from db";
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