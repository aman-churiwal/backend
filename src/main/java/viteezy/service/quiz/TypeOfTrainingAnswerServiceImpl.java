package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.TypeOfTrainingAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.TypeOfTrainingAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class TypeOfTrainingAnswerServiceImpl implements TypeOfTrainingAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TypeOfTrainingAnswerService.class);
    private final TypeOfTrainingAnswerRepository typeOfTrainingAnswerRepository;
    private final QuizService quizService;

    public TypeOfTrainingAnswerServiceImpl(
            TypeOfTrainingAnswerRepository typeOfTrainingAnswerRepository,
            QuizService quizService
    ) {
        this.typeOfTrainingAnswerRepository = typeOfTrainingAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<TypeOfTrainingAnswer>> find(Long id) {
        return typeOfTrainingAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<TypeOfTrainingAnswer>> find(UUID quizExternalReference) {
        return typeOfTrainingAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, TypeOfTrainingAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildTypeOfTrainingAnswer(categorizedAnswer))
                .flatMap(typeOfTrainingAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, TypeOfTrainingAnswer> update(CategorizedAnswer categorizedAnswer) {
        return typeOfTrainingAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedTypeOfTrainingAnswer(categorizedAnswer))
                .flatMap(typeOfTrainingAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<TypeOfTrainingAnswer, TypeOfTrainingAnswer> buildUpdatedTypeOfTrainingAnswer(CategorizedAnswer categorizedAnswer) {
        return typeOfTrainingAnswer -> {
            final Long id = typeOfTrainingAnswer.getId();
            final Long quizId = typeOfTrainingAnswer.getQuizId();
            final Long typeOfTrainingId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = typeOfTrainingAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new TypeOfTrainingAnswer(id, quizId, typeOfTrainingId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, TypeOfTrainingAnswer> buildTypeOfTrainingAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long typeOfTrainingId = categorizedAnswer.getCategoryInternalId();
            return new TypeOfTrainingAnswer(null, quizId, typeOfTrainingId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, TypeOfTrainingAnswer>> retrieveById() {
        return id -> {
            Try<Optional<TypeOfTrainingAnswer>> optionalTry = typeOfTrainingAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "TypeOfTrainingAnswer entity was saved but could not be retrieved from db";
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