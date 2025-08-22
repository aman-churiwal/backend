package ${package}.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import ${package}.db.quiz.${moduleNamePascalCase}Repository;
import ${package}.domain.quiz.${moduleNamePascalCase};

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ${moduleNamePascalCase}ServiceImpl implements ${moduleNamePascalCase}Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(${moduleNamePascalCase}Service.class);
    private final ${moduleNamePascalCase}Repository ${moduleNameCamelCase}Repository;

    public ${moduleNamePascalCase}ServiceImpl(${moduleNamePascalCase}Repository ${moduleNameCamelCase}Repository) {
        this.${moduleNameCamelCase}Repository = ${moduleNameCamelCase}Repository;
    }

    @Override
    public Either<Throwable, Optional<${moduleNamePascalCase}>> find(Long id) {
        return ${moduleNameCamelCase}Repository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<${moduleNamePascalCase}>> findAll() {
        return ${moduleNameCamelCase}Repository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, ${moduleNamePascalCase}> save(${moduleNamePascalCase} ${moduleNameCamelCase}) {
        return ${moduleNameCamelCase}Repository
                .save(${moduleNameCamelCase})
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, ${moduleNamePascalCase}>> retrieveById() {
        return id -> {
            Try<Optional<${moduleNamePascalCase}>> optionalTry = ${moduleNameCamelCase}Repository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "${moduleNamePascalCase} entity was saved but could not be retrieved from db";
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
}