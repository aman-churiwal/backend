package ${package}.db.quiz;

import io.vavr.control.Try;
import ${package}.domain.quiz.${moduleNamePascalCase};

import java.util.List;
import java.util.Optional;

public interface ${moduleNamePascalCase}Repository {

    Try<Optional<${moduleNamePascalCase}>> find(Long id);

    Try<List<${moduleNamePascalCase}>> findAll();

    Try<Long> save(${moduleNamePascalCase} ${moduleNameCamelCase});

}