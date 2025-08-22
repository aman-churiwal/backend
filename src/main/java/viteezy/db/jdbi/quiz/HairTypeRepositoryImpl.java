package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.HairTypeRepository;
import viteezy.domain.quiz.HairType;

import java.util.List;
import java.util.Optional;

public class HairTypeRepositoryImpl implements HairTypeRepository {

    private static final String SELECT_ALL = "SELECT hair_types.* FROM hair_types ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO hair_types (name, code, subtitle) " +
            "VALUES (:name, :code, :subtitle)";

    private final Jdbi jdbi;

    public HairTypeRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<HairType>> find(Long id) {
        final HandleCallback<Optional<HairType>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(HairType.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<HairType>> findAll() {
        final HandleCallback<List<HairType>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(HairType.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(HairType hairType) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(hairType)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}