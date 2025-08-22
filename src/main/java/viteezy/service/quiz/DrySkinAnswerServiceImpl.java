package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.DrySkinAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DrySkinAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class DrySkinAnswerServiceImpl implements DrySkinAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrySkinAnswerService.class);
    private final DrySkinAnswerRepository drySkinAnswerRepository;
    private final QuizService quizService;

    public DrySkinAnswerServiceImpl(
            DrySkinAnswerRepository drySkinAnswerRepository,
            QuizService quizService
    ) {
        this.drySkinAnswerRepository = drySkinAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<DrySkinAnswer>> find(Long id) {
        return drySkinAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<DrySkinAnswer>> find(UUID quizExternalReference) {
        return drySkinAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, DrySkinAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildDrySkinAnswer(categorizedAnswer))
                .flatMap(drySkinAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, DrySkinAnswer> update(CategorizedAnswer categorizedAnswer) {
        return drySkinAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedDrySkinAnswer(categorizedAnswer))
                .flatMap(drySkinAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<DrySkinAnswer, DrySkinAnswer> buildUpdatedDrySkinAnswer(CategorizedAnswer categorizedAnswer) {
        return drySkinAnswer -> {
            final Long id = drySkinAnswer.getId();
            final Long quizId = drySkinAnswer.getQuizId();
            final Long drySkinId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = drySkinAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new DrySkinAnswer(id, quizId, drySkinId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, DrySkinAnswer> buildDrySkinAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long drySkinId = categorizedAnswer.getCategoryInternalId();
            return new DrySkinAnswer(null, quizId, drySkinId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, DrySkinAnswer>> retrieveById() {
        return id -> {
            Try<Optional<DrySkinAnswer>> optionalTry = drySkinAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "DrySkinAnswer entity was saved but could not be retrieved from db";
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