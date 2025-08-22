package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.PresentAtCrowdedPlacesRepository;
import viteezy.domain.quiz.PresentAtCrowdedPlaces;

import java.util.List;
import java.util.Optional;

public class PresentAtCrowdedPlacesRepositoryImpl implements PresentAtCrowdedPlacesRepository {

    private static final String SELECT_ALL = "SELECT present_at_crowded_places.* FROM present_at_crowded_places ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO present_at_crowded_places (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public PresentAtCrowdedPlacesRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<PresentAtCrowdedPlaces>> find(Long id) {
        final HandleCallback<Optional<PresentAtCrowdedPlaces>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(PresentAtCrowdedPlaces.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<PresentAtCrowdedPlaces>> findAll() {
        final HandleCallback<List<PresentAtCrowdedPlaces>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(PresentAtCrowdedPlaces.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(PresentAtCrowdedPlaces presentAtCrowdedPlaces) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(presentAtCrowdedPlaces)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}