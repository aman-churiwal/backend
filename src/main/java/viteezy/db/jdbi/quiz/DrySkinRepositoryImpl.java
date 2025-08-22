package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DrySkinRepository;
import viteezy.domain.quiz.DrySkin;

import java.util.List;
import java.util.Optional;

public class DrySkinRepositoryImpl implements DrySkinRepository {

    private static final String SELECT_ALL = "SELECT dry_skins.* FROM dry_skins ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO dry_skins (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public DrySkinRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DrySkin>> find(Long id) {
        final HandleCallback<Optional<DrySkin>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DrySkin.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<DrySkin>> findAll() {
        final HandleCallback<List<DrySkin>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(DrySkin.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DrySkin drySkin) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(drySkin)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}