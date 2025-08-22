package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.ChildrenWishRepository;
import viteezy.domain.quiz.ChildrenWish;

import java.util.List;
import java.util.Optional;

public class ChildrenWishRepositoryImpl implements ChildrenWishRepository {

    private static final String SELECT_ALL = "SELECT children_wishs.* FROM children_wishs ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO children_wishs (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public ChildrenWishRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<ChildrenWish>> find(Long id) {
        final HandleCallback<Optional<ChildrenWish>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(ChildrenWish.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<ChildrenWish>> findAll() {
        final HandleCallback<List<ChildrenWish>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(ChildrenWish.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(ChildrenWish childrenWish) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(childrenWish)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}