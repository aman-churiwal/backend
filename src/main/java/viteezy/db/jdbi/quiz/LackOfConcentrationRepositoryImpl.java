package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.LackOfConcentrationRepository;
import viteezy.domain.quiz.LackOfConcentration;

import java.util.List;
import java.util.Optional;

public class LackOfConcentrationRepositoryImpl implements LackOfConcentrationRepository {

    private static final String SELECT_ALL = "SELECT lack_of_concentrations.* FROM lack_of_concentrations ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO lack_of_concentrations (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public LackOfConcentrationRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<LackOfConcentration>> find(Long id) {
        final HandleCallback<Optional<LackOfConcentration>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(LackOfConcentration.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<LackOfConcentration>> findAll() {
        final HandleCallback<List<LackOfConcentration>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(LackOfConcentration.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(LackOfConcentration lackOfConcentration) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(lackOfConcentration)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}