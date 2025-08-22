package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.UrinaryInfectionRepository;
import viteezy.domain.quiz.UrinaryInfection;

import java.util.List;
import java.util.Optional;

public class UrinaryInfectionRepositoryImpl implements UrinaryInfectionRepository {

    private static final String SELECT_ALL = "SELECT urinary_infections.* FROM urinary_infections ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO urinary_infections (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public UrinaryInfectionRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<UrinaryInfection>> find(Long id) {
        final HandleCallback<Optional<UrinaryInfection>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(UrinaryInfection.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<UrinaryInfection>> findAll() {
        final HandleCallback<List<UrinaryInfection>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(UrinaryInfection.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(UrinaryInfection urinaryInfection) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(urinaryInfection)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}