package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.GenderAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.GenderAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenderAnswerServiceImpl implements GenderAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenderAnswerService.class);
    private final GenderAnswerRepository genderAnswerRepository;
    private final QuizService quizService;

    public GenderAnswerServiceImpl(
            GenderAnswerRepository genderAnswerRepository,
            QuizService quizService
    ) {
        this.genderAnswerRepository = genderAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<GenderAnswer>> find(Long id) {
        return genderAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<GenderAnswer>> find(UUID quizExternalReference) {
        return genderAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, GenderAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildGenderAnswer(categorizedAnswer))
                .flatMap(genderAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, GenderAnswer> update(CategorizedAnswer categorizedAnswer) {
        return genderAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedGenderAnswer(categorizedAnswer))
                .flatMap(genderAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<GenderAnswer, GenderAnswer> buildUpdatedGenderAnswer(CategorizedAnswer categorizedAnswer) {
        return genderAnswer -> {
            final Long id = genderAnswer.getId();
            final Long quizId = genderAnswer.getQuizId();
            final Long genderId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = genderAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new GenderAnswer(id, quizId, genderId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, GenderAnswer> buildGenderAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long genderId = categorizedAnswer.getCategoryInternalId();
            return new GenderAnswer(null, quizId, genderId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, GenderAnswer>> retrieveById() {
        return id -> {
            Try<Optional<GenderAnswer>> optionalTry = genderAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "GenderAnswer entity was saved but could not be retrieved from db";
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