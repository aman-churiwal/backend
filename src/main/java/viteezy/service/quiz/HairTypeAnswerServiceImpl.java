package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.HairTypeAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.HairTypeAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class HairTypeAnswerServiceImpl implements HairTypeAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HairTypeAnswerService.class);
    private final HairTypeAnswerRepository hairTypeAnswerRepository;
    private final QuizService quizService;

    public HairTypeAnswerServiceImpl(
            HairTypeAnswerRepository hairTypeAnswerRepository,
            QuizService quizService
    ) {
        this.hairTypeAnswerRepository = hairTypeAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<HairTypeAnswer>> find(Long id) {
        return hairTypeAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<HairTypeAnswer>> find(UUID quizExternalReference) {
        return hairTypeAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, HairTypeAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildHairTypeAnswer(categorizedAnswer))
                .flatMap(hairTypeAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, HairTypeAnswer> update(CategorizedAnswer categorizedAnswer) {
        return hairTypeAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedHairTypeAnswer(categorizedAnswer))
                .flatMap(hairTypeAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<HairTypeAnswer, HairTypeAnswer> buildUpdatedHairTypeAnswer(CategorizedAnswer categorizedAnswer) {
        return hairTypeAnswer -> {
            final Long id = hairTypeAnswer.getId();
            final Long quizId = hairTypeAnswer.getQuizId();
            final Long hairTypeId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = hairTypeAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new HairTypeAnswer(id, quizId, hairTypeId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, HairTypeAnswer> buildHairTypeAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long hairTypeId = categorizedAnswer.getCategoryInternalId();
            return new HairTypeAnswer(null, quizId, hairTypeId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, HairTypeAnswer>> retrieveById() {
        return id -> {
            Try<Optional<HairTypeAnswer>> optionalTry = hairTypeAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "HairTypeAnswer entity was saved but could not be retrieved from db";
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