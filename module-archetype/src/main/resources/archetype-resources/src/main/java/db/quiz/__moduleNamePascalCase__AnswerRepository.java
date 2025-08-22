package ${package}.db.quiz;

import io.vavr.control.Try;
import ${package}.domain.quiz.${moduleNamePascalCase}Answer;

import java.util.Optional;
import java.util.UUID;

public interface ${moduleNamePascalCase}AnswerRepository {

    Try<Optional<${moduleNamePascalCase}Answer>> find(Long id);

    Try<Optional<${moduleNamePascalCase}Answer>> find(UUID quizExternalReference);

    Try<Long> save(${moduleNamePascalCase}Answer ${moduleNameCamelCase}Answer);

    Try<Long> update(${moduleNamePascalCase}Answer ${moduleNameCamelCase}Answer);
}