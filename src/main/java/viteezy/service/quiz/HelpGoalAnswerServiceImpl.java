package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.HelpGoalAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.HelpGoalAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class HelpGoalAnswerServiceImpl implements HelpGoalAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpGoalAnswerService.class);
    private final HelpGoalAnswerRepository helpGoalAnswerRepository;
    private final QuizService quizService;

    public HelpGoalAnswerServiceImpl(
            HelpGoalAnswerRepository helpGoalAnswerRepository,
            QuizService quizService
    ) {
        this.helpGoalAnswerRepository = helpGoalAnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<HelpGoalAnswer>> find(Long id) {
        return helpGoalAnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<HelpGoalAnswer>> find(UUID quizExternalReference) {
        return helpGoalAnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, HelpGoalAnswer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(buildHelpGoalAnswer(categorizedAnswer))
                .flatMap(helpGoalAnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, HelpGoalAnswer> update(CategorizedAnswer categorizedAnswer) {
        return helpGoalAnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdatedHelpGoalAnswer(categorizedAnswer))
                .flatMap(helpGoalAnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<HelpGoalAnswer, HelpGoalAnswer> buildUpdatedHelpGoalAnswer(CategorizedAnswer categorizedAnswer) {
        return helpGoalAnswer -> {
            final Long id = helpGoalAnswer.getId();
            final Long quizId = helpGoalAnswer.getQuizId();
            final Long helpGoalId = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = helpGoalAnswer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new HelpGoalAnswer(id, quizId, helpGoalId, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, HelpGoalAnswer> buildHelpGoalAnswer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long helpGoalId = categorizedAnswer.getCategoryInternalId();
            return new HelpGoalAnswer(null, quizId, helpGoalId, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, HelpGoalAnswer>> retrieveById() {
        return id -> {
            Try<Optional<HelpGoalAnswer>> optionalTry = helpGoalAnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "HelpGoalAnswer entity was saved but could not be retrieved from db";
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