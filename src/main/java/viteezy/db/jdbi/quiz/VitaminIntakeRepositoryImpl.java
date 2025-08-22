package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.VitaminIntakeRepository;
import viteezy.domain.quiz.VitaminIntake;

import java.util.List;
import java.util.Optional;

public class VitaminIntakeRepositoryImpl implements VitaminIntakeRepository {

    private static final String SELECT_ALL = "SELECT vitamin_intakes.* FROM vitamin_intakes ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO vitamin_intakes (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public VitaminIntakeRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<VitaminIntake>> find(Long id) {
        final HandleCallback<Optional<VitaminIntake>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(VitaminIntake.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<VitaminIntake>> findAll() {
        final HandleCallback<List<VitaminIntake>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(VitaminIntake.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(VitaminIntake vitaminIntake) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(vitaminIntake)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}