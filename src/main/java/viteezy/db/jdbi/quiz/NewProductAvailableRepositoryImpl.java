package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.NewProductAvailableRepository;
import viteezy.domain.quiz.NewProductAvailable;

import java.util.List;
import java.util.Optional;

public class NewProductAvailableRepositoryImpl implements NewProductAvailableRepository {

    private static final String SELECT_ALL = "SELECT new_product_availables.* FROM new_product_availables ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO new_product_availables (name, code, subtitle) " +
            "VALUES (:name, :code, :subtitle)";

    private final Jdbi jdbi;

    public NewProductAvailableRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<NewProductAvailable>> find(Long id) {
        final HandleCallback<Optional<NewProductAvailable>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(NewProductAvailable.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<NewProductAvailable>> findAll() {
        final HandleCallback<List<NewProductAvailable>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(NewProductAvailable.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(NewProductAvailable newProductAvailable) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(newProductAvailable)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}