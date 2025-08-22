package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.MentalFitnessAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.MentalFitnessAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class MentalFitnessAnswerServiceImpl implements MentalFitnessAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MentalFitnessAnswerService.class);
    private final MentalFitnessAnswerRepository mentalFitnessAnswerRepository;
    private final QuizService quizService;

    public MentalFitnessAnswerServiceImpl(
            MentalFitnessAnswerRepository mentalFitnessAnswerRepository,
            QuizService quizService
    ) {
        this.mentalFitnessAnswerRepository = mentalFitnessAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<MentalFitnessAnswer>> find(Long id) {
        return mentalFitnessAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<MentalFitnessAnswer>> find(UUID quizExternalReference) {
        return mentalFitnessAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, MentalFitnessAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildMentalFitnessAnswer(categorizedAnswer))
                .flatMap(mentalFitnessAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, MentalFitnessAnswer> update(CategorizedAnswer categorizedAnswer) {
        return mentalFitnessAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedMentalFitnessAnswer(categorizedAnswer))
                .flatMap(mentalFitnessAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<MentalFitnessAnswer, MentalFitnessAnswer> buildUpdatedMentalFitnessAnswer(CategorizedAnswer categorizedAnswer) {
        return mentalFitnessAnswer -> {
            final Long id = mentalFitnessAnswer.getId();
            final Long quizId = mentalFitnessAnswer.getQuizId();
            final Long mentalFitnessId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = mentalFitnessAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new MentalFitnessAnswer(id, quizId, mentalFitnessId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, MentalFitnessAnswer> buildMentalFitnessAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long mentalFitnessId = categorizedAnswer.getCategoryInternalId();
            return new MentalFitnessAnswer(null, quizId, mentalFitnessId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, MentalFitnessAnswer>> retrieveById() {
        return id -> {
            Try<Optional<MentalFitnessAnswer>> optionalTry = mentalFitnessAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "MentalFitnessAnswer entity was saved but could not be retrieved from db";
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