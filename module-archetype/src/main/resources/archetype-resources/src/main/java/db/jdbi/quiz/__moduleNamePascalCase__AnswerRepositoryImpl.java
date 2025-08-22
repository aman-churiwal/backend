package ${package}.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import ${package}.db.quiz.${moduleNamePascalCase}AnswerRepository;
import ${package}.domain.quiz.${moduleNamePascalCase}Answer;

import java.util.Optional;
import java.util.UUID;

public class ${moduleNamePascalCase}AnswerRepositoryImpl implements ${moduleNamePascalCase}AnswerRepository {

    private static final String SELECT_ALL = "SELECT ${sqlAnswerTableName}.* FROM ${sqlAnswerTableName} ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on ${sqlAnswerTableName}.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE ${sqlAnswerTableName} " +
            "SET ${moduleNameSnakeCase}_id = :${moduleNameCamelCase}Id, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO ${sqlAnswerTableName}(quiz_id, ${moduleNameSnakeCase}_id) " +
            "VALUES(:quizId, :${moduleNameCamelCase}Id);";

    private final Jdbi jdbi;

    public ${moduleNamePascalCase}AnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<${moduleNamePascalCase}Answer>> find(Long id) {
        final HandleCallback<Optional<${moduleNamePascalCase}Answer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(${moduleNamePascalCase}Answer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<${moduleNamePascalCase}Answer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<${moduleNamePascalCase}Answer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(${moduleNamePascalCase}Answer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(${moduleNamePascalCase}Answer ${moduleNameCamelCase}Answer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(${moduleNameCamelCase}Answer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(${moduleNamePascalCase}Answer ${moduleNameCamelCase}Answer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(${moduleNameCamelCase}Answer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> ${moduleNameCamelCase}Answer.getId());
    }
}