package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.MenstruationMoodAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.MenstruationMoodAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class MenstruationMoodAnswerServiceImpl implements MenstruationMoodAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenstruationMoodAnswerService.class);
    private final MenstruationMoodAnswerRepository menstruationMoodAnswerRepository;
    private final QuizService quizService;

    public MenstruationMoodAnswerServiceImpl(
            MenstruationMoodAnswerRepository menstruationMoodAnswerRepository,
            QuizService quizService
    ) {
        this.menstruationMoodAnswerRepository = menstruationMoodAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<MenstruationMoodAnswer>> find(Long id) {
        return menstruationMoodAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<MenstruationMoodAnswer>> find(UUID quizExternalReference) {
        return menstruationMoodAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, MenstruationMoodAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildMenstruationMoodAnswer(categorizedAnswer))
                .flatMap(menstruationMoodAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, MenstruationMoodAnswer> update(CategorizedAnswer categorizedAnswer) {
        return menstruationMoodAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedMenstruationMoodAnswer(categorizedAnswer))
                .flatMap(menstruationMoodAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<MenstruationMoodAnswer, MenstruationMoodAnswer> buildUpdatedMenstruationMoodAnswer(CategorizedAnswer categorizedAnswer) {
        return menstruationMoodAnswer -> {
            final Long id = menstruationMoodAnswer.getId();
            final Long quizId = menstruationMoodAnswer.getQuizId();
            final Long menstruationMoodId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = menstruationMoodAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new MenstruationMoodAnswer(id, quizId, menstruationMoodId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, MenstruationMoodAnswer> buildMenstruationMoodAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long menstruationMoodId = categorizedAnswer.getCategoryInternalId();
            return new MenstruationMoodAnswer(null, quizId, menstruationMoodId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, MenstruationMoodAnswer>> retrieveById() {
        return id -> {
            Try<Optional<MenstruationMoodAnswer>> optionalTry = menstruationMoodAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "MenstruationMoodAnswer entity was saved but could not be retrieved from db";
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