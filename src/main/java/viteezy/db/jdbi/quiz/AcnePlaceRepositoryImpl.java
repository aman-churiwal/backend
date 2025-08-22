package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AcnePlaceRepository;
import viteezy.domain.quiz.AcnePlace;

import java.util.List;
import java.util.Optional;

public class AcnePlaceRepositoryImpl implements AcnePlaceRepository {

    private static final String SELECT_ALL = "SELECT acne_places.* FROM acne_places ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO acne_places (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AcnePlaceRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AcnePlace>> find(Long id) {
        final HandleCallback<Optional<AcnePlace>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AcnePlace.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AcnePlace>> findAll() {
        final HandleCallback<List<AcnePlace>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AcnePlace.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AcnePlace acnePlace) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(acnePlace)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}