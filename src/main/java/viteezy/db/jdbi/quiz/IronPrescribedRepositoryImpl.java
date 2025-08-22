package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.IronPrescribedRepository;
import viteezy.domain.quiz.IronPrescribed;

import java.util.List;
import java.util.Optional;

public class IronPrescribedRepositoryImpl implements IronPrescribedRepository {

    private static final String SELECT_ALL = "SELECT iron_prescribeds.* FROM iron_prescribeds ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO iron_prescribeds (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public IronPrescribedRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<IronPrescribed>> find(Long id) {
        final HandleCallback<Optional<IronPrescribed>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(IronPrescribed.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<IronPrescribed>> findAll() {
        final HandleCallback<List<IronPrescribed>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(IronPrescribed.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(IronPrescribed ironPrescribed) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(ironPrescribed)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}