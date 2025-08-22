package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SportReasonAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.SportReasonAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class SportReasonAnswerServiceImpl implements SportReasonAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportReasonAnswerService.class);
    private final SportReasonAnswerRepository sportReasonAnswerRepository;
    private final QuizService quizService;

    public SportReasonAnswerServiceImpl(
            SportReasonAnswerRepository sportReasonAnswerRepository,
            QuizService quizService
    ) {
        this.sportReasonAnswerRepository = sportReasonAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<SportReasonAnswer>> find(Long id) {
        return sportReasonAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<SportReasonAnswer>> find(UUID quizExternalReference) {
        return sportReasonAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, SportReasonAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildSportReasonAnswer(categorizedAnswer))
                .flatMap(sportReasonAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, SportReasonAnswer> update(CategorizedAnswer categorizedAnswer) {
        return sportReasonAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedSportReasonAnswer(categorizedAnswer))
                .flatMap(sportReasonAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<SportReasonAnswer, SportReasonAnswer> buildUpdatedSportReasonAnswer(CategorizedAnswer categorizedAnswer) {
        return sportReasonAnswer -> {
            final Long id = sportReasonAnswer.getId();
            final Long quizId = sportReasonAnswer.getQuizId();
            final Long sportReasonId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = sportReasonAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new SportReasonAnswer(id, quizId, sportReasonId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, SportReasonAnswer> buildSportReasonAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long sportReasonId = categorizedAnswer.getCategoryInternalId();
            return new SportReasonAnswer(null, quizId, sportReasonId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, SportReasonAnswer>> retrieveById() {
        return id -> {
            Try<Optional<SportReasonAnswer>> optionalTry = sportReasonAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "SportReasonAnswer entity was saved but could not be retrieved from db";
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