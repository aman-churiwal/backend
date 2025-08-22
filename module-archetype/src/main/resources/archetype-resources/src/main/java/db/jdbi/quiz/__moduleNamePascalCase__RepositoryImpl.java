package ${package}.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import ${package}.db.quiz.${moduleNamePascalCase}Repository;
import ${package}.domain.quiz.${moduleNamePascalCase};

import java.util.List;
import java.util.Optional;

public class ${moduleNamePascalCase}RepositoryImpl implements ${moduleNamePascalCase}Repository {

    private static final String SELECT_ALL = "SELECT ${sqlTableName}.* FROM ${sqlTableName} ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO ${sqlTableName} (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public ${moduleNamePascalCase}RepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<${moduleNamePascalCase}>> find(Long id) {
        final HandleCallback<Optional<${moduleNamePascalCase}>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(${moduleNamePascalCase}.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<${moduleNamePascalCase}>> findAll() {
        final HandleCallback<List<${moduleNamePascalCase}>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(${moduleNamePascalCase}.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(${moduleNamePascalCase} ${moduleNameCamelCase}) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(${moduleNameCamelCase})
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}