package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DietTypeRepository;
import viteezy.domain.quiz.DietType;

import java.util.List;
import java.util.Optional;

public class DietTypeRepositoryImpl implements DietTypeRepository {

    private static final String SELECT_ALL = "SELECT diet_types.* FROM diet_types ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO diet_types (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public DietTypeRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DietType>> find(Long id) {
        final HandleCallback<Optional<DietType>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DietType.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<DietType>> findAll() {
        final HandleCallback<List<DietType>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(DietType.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DietType dietType) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(dietType)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}