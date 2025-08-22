package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.BingeEatingRepository;
import viteezy.domain.quiz.BingeEating;

import java.util.List;
import java.util.Optional;

public class BingeEatingRepositoryImpl implements BingeEatingRepository {

    private static final String SELECT_ALL = "SELECT binge_eatings.* FROM binge_eatings ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO binge_eatings (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public BingeEatingRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<BingeEating>> find(Long id) {
        final HandleCallback<Optional<BingeEating>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(BingeEating.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<BingeEating>> findAll() {
        final HandleCallback<List<BingeEating>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(BingeEating.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(BingeEating bingeEating) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(bingeEating)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}