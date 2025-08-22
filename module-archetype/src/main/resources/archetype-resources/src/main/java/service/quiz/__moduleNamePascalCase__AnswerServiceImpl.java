package ${package}.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import ${package}.db.quiz.${moduleNamePascalCase}AnswerRepository;
import ${package}.domain.quiz.${moduleNamePascalCase}Answer;
import ${package}.domain.quiz.Quiz;
import ${package}.domain.dto.CategorizedAnswer;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class ${moduleNamePascalCase}AnswerServiceImpl implements ${moduleNamePascalCase}AnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(${moduleNamePascalCase}AnswerService.class);
    private final ${moduleNamePascalCase}AnswerRepository ${moduleNameCamelCase}AnswerRepository;
    private final QuizService quizService;

    public ${moduleNamePascalCase}AnswerServiceImpl(
            ${moduleNamePascalCase}AnswerRepository ${moduleNameCamelCase}AnswerRepository,
            QuizService quizService
    ) {
        this.${moduleNameCamelCase}AnswerRepository = ${moduleNameCamelCase}AnswerRepository;
        this.quizService = quizService;
    }

    @Override
    public Either<Throwable, Optional<${moduleNamePascalCase}Answer>> find(Long id) {
        return ${moduleNameCamelCase}AnswerRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, Optional<${moduleNamePascalCase}Answer>> find(UUID quizExternalReference) {
        return ${moduleNameCamelCase}AnswerRepository.find(quizExternalReference)
                .toEither();
    }

    @Override
    public Either<Throwable, ${moduleNamePascalCase}Answer> save(CategorizedAnswer categorizedAnswer) {
        return quizService.find(categorizedAnswer.getQuizExternalReference())
                .map(build${moduleNamePascalCase}Answer(categorizedAnswer))
                .flatMap(${moduleNameCamelCase}AnswerRepository::save)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    @Override
    public Either<Throwable, ${moduleNamePascalCase}Answer> update(CategorizedAnswer categorizedAnswer) {
        return ${moduleNameCamelCase}AnswerRepository.find(categorizedAnswer.getQuizExternalReference())
                .flatMap(enforceEntityToBePresentTry())
                .map(buildUpdated${moduleNamePascalCase}Answer(categorizedAnswer))
                .flatMap(${moduleNameCamelCase}AnswerRepository::update)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<${moduleNamePascalCase}Answer, ${moduleNamePascalCase}Answer> buildUpdated${moduleNamePascalCase}Answer(CategorizedAnswer categorizedAnswer) {
        return ${moduleNameCamelCase}Answer -> {
            final Long id = ${moduleNameCamelCase}Answer.getId();
            final Long quizId = ${moduleNameCamelCase}Answer.getQuizId();
            final Long ${moduleNameCamelCase}Id = categorizedAnswer.getCategoryInternalId();
            final LocalDateTime creationTimestamp = ${moduleNameCamelCase}Answer.getCreationTimestamp();
            final LocalDateTime modificationTimestamp = LocalDateTime.now();
            return new ${moduleNamePascalCase}Answer(id, quizId, ${moduleNameCamelCase}Id, creationTimestamp, modificationTimestamp);
        };
    }

    private Function<Quiz, ${moduleNamePascalCase}Answer> build${moduleNamePascalCase}Answer(CategorizedAnswer categorizedAnswer) {
        return quiz -> {
            final Long quizId = quiz.getId();
            final Long ${moduleNameCamelCase}Id = categorizedAnswer.getCategoryInternalId();
            return new ${moduleNamePascalCase}Answer(null, quizId, ${moduleNameCamelCase}Id, LocalDateTime.now(), LocalDateTime.now());
        };
    }

    private Function<Long, Either<Throwable, ${moduleNamePascalCase}Answer>> retrieveById() {
        return id -> {
            Try<Optional<${moduleNamePascalCase}Answer>> optionalTry = ${moduleNameCamelCase}AnswerRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "${moduleNamePascalCase}Answer entity was saved but could not be retrieved from db";
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