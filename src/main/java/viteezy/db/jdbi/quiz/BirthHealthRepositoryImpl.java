package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.BirthHealthRepository;
import viteezy.domain.quiz.BirthHealth;

import java.util.List;
import java.util.Optional;

public class BirthHealthRepositoryImpl implements BirthHealthRepository {

    private static final String SELECT_ALL = "SELECT birth_healths.* FROM birth_healths ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO birth_healths (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public BirthHealthRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<BirthHealth>> find(Long id) {
        final HandleCallback<Optional<BirthHealth>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(BirthHealth.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<BirthHealth>> findAll() {
        final HandleCallback<List<BirthHealth>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(BirthHealth.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(BirthHealth birthHealth) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(birthHealth)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}