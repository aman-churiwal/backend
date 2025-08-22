package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.EasternMedicineOpinionAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.EasternMedicineOpinionAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class EasternMedicineOpinionAnswerServiceImpl implements EasternMedicineOpinionAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasternMedicineOpinionAnswerService.class);
    private final EasternMedicineOpinionAnswerRepository easternMedicineOpinionAnswerRepository;
    private final QuizService quizService;

    public EasternMedicineOpinionAnswerServiceImpl(
            EasternMedicineOpinionAnswerRepository easternMedicineOpinionAnswerRepository,
            QuizService quizService
    ) {
        this.easternMedicineOpinionAnswerRepository = easternMedicineOpinionAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<EasternMedicineOpinionAnswer>> find(Long id) {
        return easternMedicineOpinionAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<EasternMedicineOpinionAnswer>> find(UUID quizExternalReference) {
        return easternMedicineOpinionAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, EasternMedicineOpinionAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildEasternMedicineOpinionAnswer(categorizedAnswer))
                .flatMap(easternMedicineOpinionAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, EasternMedicineOpinionAnswer> update(CategorizedAnswer categorizedAnswer) {
        return easternMedicineOpinionAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedEasternMedicineOpinionAnswer(categorizedAnswer))
                .flatMap(easternMedicineOpinionAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<EasternMedicineOpinionAnswer, EasternMedicineOpinionAnswer> buildUpdatedEasternMedicineOpinionAnswer(CategorizedAnswer categorizedAnswer) {
        return easternMedicineOpinionAnswer -> {
            final Long id = easternMedicineOpinionAnswer.getId();
            final Long quizId = easternMedicineOpinionAnswer.getQuizId();
            final Long easternMedicineOpinionId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = easternMedicineOpinionAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new EasternMedicineOpinionAnswer(id, quizId, easternMedicineOpinionId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, EasternMedicineOpinionAnswer> buildEasternMedicineOpinionAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long easternMedicineOpinionId = categorizedAnswer.getCategoryInternalId();
            return new EasternMedicineOpinionAnswer(null, quizId, easternMedicineOpinionId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, EasternMedicineOpinionAnswer>> retrieveById() {
        return id -> {
            Try<Optional<EasternMedicineOpinionAnswer>> optionalTry = easternMedicineOpinionAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "EasternMedicineOpinionAnswer entity was saved but could not be retrieved from db";
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