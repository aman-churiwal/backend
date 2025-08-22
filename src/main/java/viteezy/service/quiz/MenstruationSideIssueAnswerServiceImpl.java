package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.MenstruationSideIssueAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.MenstruationSideIssueAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class MenstruationSideIssueAnswerServiceImpl implements MenstruationSideIssueAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenstruationSideIssueAnswerService.class);
    private final MenstruationSideIssueAnswerRepository menstruationSideIssueAnswerRepository;
    private final QuizService quizService;

    public MenstruationSideIssueAnswerServiceImpl(
            MenstruationSideIssueAnswerRepository menstruationSideIssueAnswerRepository,
            QuizService quizService
    ) {
        this.menstruationSideIssueAnswerRepository = menstruationSideIssueAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<MenstruationSideIssueAnswer>> find(Long id) {
        return menstruationSideIssueAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<MenstruationSideIssueAnswer>> find(UUID quizExternalReference) {
        return menstruationSideIssueAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, MenstruationSideIssueAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildMenstruationSideIssueAnswer(categorizedAnswer))
                .flatMap(menstruationSideIssueAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, MenstruationSideIssueAnswer> update(CategorizedAnswer categorizedAnswer) {
        return menstruationSideIssueAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedMenstruationSideIssueAnswer(categorizedAnswer))
                .flatMap(menstruationSideIssueAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<MenstruationSideIssueAnswer, MenstruationSideIssueAnswer> buildUpdatedMenstruationSideIssueAnswer(CategorizedAnswer categorizedAnswer) {
        return menstruationSideIssueAnswer -> {
            final Long id = menstruationSideIssueAnswer.getId();
            final Long quizId = menstruationSideIssueAnswer.getQuizId();
            final Long menstruationSideIssueId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = menstruationSideIssueAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new MenstruationSideIssueAnswer(id, quizId, menstruationSideIssueId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, MenstruationSideIssueAnswer> buildMenstruationSideIssueAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long menstruationSideIssueId = categorizedAnswer.getCategoryInternalId();
            return new MenstruationSideIssueAnswer(null, quizId, menstruationSideIssueId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, MenstruationSideIssueAnswer>> retrieveById() {
        return id -> {
            Try<Optional<MenstruationSideIssueAnswer>> optionalTry = menstruationSideIssueAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "MenstruationSideIssueAnswer entity was saved but could not be retrieved from db";
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