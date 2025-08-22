package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.SportReasonRepository;
import viteezy.domain.quiz.SportReason;

import java.util.List;
import java.util.Optional;

public class SportReasonRepositoryImpl implements SportReasonRepository {

    private static final String SELECT_ALL = "SELECT sport_reasons.* FROM sport_reasons ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO sport_reasons (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public SportReasonRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<SportReason>> find(Long id) {
        final HandleCallback<Optional<SportReason>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(SportReason.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<SportReason>> findAll() {
        final HandleCallback<List<SportReason>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(SportReason.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(SportReason sportReason) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(sportReason)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}