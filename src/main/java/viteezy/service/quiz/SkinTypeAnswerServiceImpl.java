package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SkinTypeAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.SkinTypeAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class SkinTypeAnswerServiceImpl implements SkinTypeAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkinTypeAnswerService.class);
    private final SkinTypeAnswerRepository skinTypeAnswerRepository;
    private final QuizService quizService;

    public SkinTypeAnswerServiceImpl(
            SkinTypeAnswerRepository skinTypeAnswerRepository,
            QuizService quizService
    ) {
        this.skinTypeAnswerRepository = skinTypeAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<SkinTypeAnswer>> find(Long id) {
        return skinTypeAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<SkinTypeAnswer>> find(UUID quizExternalReference) {
        return skinTypeAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, SkinTypeAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildSkinTypeAnswer(categorizedAnswer))
                .flatMap(skinTypeAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, SkinTypeAnswer> update(CategorizedAnswer categorizedAnswer) {
        return skinTypeAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedSkinTypeAnswer(categorizedAnswer))
                .flatMap(skinTypeAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<SkinTypeAnswer, SkinTypeAnswer> buildUpdatedSkinTypeAnswer(CategorizedAnswer categorizedAnswer) {
        return skinTypeAnswer -> {
            final Long id = skinTypeAnswer.getId();
            final Long quizId = skinTypeAnswer.getQuizId();
            final Long skinTypeId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = skinTypeAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new SkinTypeAnswer(id, quizId, skinTypeId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, SkinTypeAnswer> buildSkinTypeAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long skinTypeId = categorizedAnswer.getCategoryInternalId();
            return new SkinTypeAnswer(null, quizId, skinTypeId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, SkinTypeAnswer>> retrieveById() {
        return id -> {
            Try<Optional<SkinTypeAnswer>> optionalTry = skinTypeAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "SkinTypeAnswer entity was saved but could not be retrieved from db";
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