package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.GenderRepository;
import viteezy.domain.quiz.Gender;

import java.util.List;
import java.util.Optional;

public class GenderRepositoryImpl implements GenderRepository {

    private static final String SELECT_ALL = "SELECT genders.* FROM genders ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO genders (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public GenderRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<Gender>> find(Long id) {
        final HandleCallback<Optional<Gender>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Gender.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Gender>> findAll() {
        final HandleCallback<List<Gender>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(Gender.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(Gender gender) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(gender)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}