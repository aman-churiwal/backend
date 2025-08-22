package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.PresentAtCrowdedPlacesAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.PresentAtCrowdedPlacesAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PresentAtCrowdedPlacesAnswerServiceImpl implements PresentAtCrowdedPlacesAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PresentAtCrowdedPlacesAnswerService.class);
    private final PresentAtCrowdedPlacesAnswerRepository presentAtCrowdedPlacesAnswerRepository;
    private final QuizService quizService;

    public PresentAtCrowdedPlacesAnswerServiceImpl(
            PresentAtCrowdedPlacesAnswerRepository presentAtCrowdedPlacesAnswerRepository,
            QuizService quizService
    ) {
        this.presentAtCrowdedPlacesAnswerRepository = presentAtCrowdedPlacesAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<PresentAtCrowdedPlacesAnswer>> find(Long id) {
        return presentAtCrowdedPlacesAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<PresentAtCrowdedPlacesAnswer>> find(UUID quizExternalReference) {
        return presentAtCrowdedPlacesAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, PresentAtCrowdedPlacesAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildPresentAtCrowdedPlacesAnswer(categorizedAnswer))
                .flatMap(presentAtCrowdedPlacesAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, PresentAtCrowdedPlacesAnswer> update(CategorizedAnswer categorizedAnswer) {
        return presentAtCrowdedPlacesAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedPresentAtCrowdedPlacesAnswer(categorizedAnswer))
                .flatMap(presentAtCrowdedPlacesAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<PresentAtCrowdedPlacesAnswer, PresentAtCrowdedPlacesAnswer> buildUpdatedPresentAtCrowdedPlacesAnswer(CategorizedAnswer categorizedAnswer) {
        return presentAtCrowdedPlacesAnswer -> {
            final Long id = presentAtCrowdedPlacesAnswer.getId();
            final Long quizId = presentAtCrowdedPlacesAnswer.getQuizId();
            final Long presentAtCrowdedPlacesId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = presentAtCrowdedPlacesAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new PresentAtCrowdedPlacesAnswer(id, quizId, presentAtCrowdedPlacesId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, PresentAtCrowdedPlacesAnswer> buildPresentAtCrowdedPlacesAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long presentAtCrowdedPlacesId = categorizedAnswer.getCategoryInternalId();
            return new PresentAtCrowdedPlacesAnswer(null, quizId, presentAtCrowdedPlacesId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, PresentAtCrowdedPlacesAnswer>> retrieveById() {
        return id -> {
            Try<Optional<PresentAtCrowdedPlacesAnswer>> optionalTry = presentAtCrowdedPlacesAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "PresentAtCrowdedPlacesAnswer entity was saved but could not be retrieved from db";
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