package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.VitaminOpinionRepository;
import viteezy.domain.quiz.VitaminOpinion;

import java.util.List;
import java.util.Optional;

public class VitaminOpinionRepositoryImpl implements VitaminOpinionRepository {

    private static final String SELECT_ALL = "SELECT vitamin_opinions.* FROM vitamin_opinions ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO vitamin_opinions (name, code, subtitle) " +
            "VALUES (:name, :code, :subtitle)";

    private final Jdbi jdbi;

    public VitaminOpinionRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<VitaminOpinion>> find(Long id) {
        final HandleCallback<Optional<VitaminOpinion>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(VitaminOpinion.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<VitaminOpinion>> findAll() {
        final HandleCallback<List<VitaminOpinion>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(VitaminOpinion.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(VitaminOpinion vitaminOpinion) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(vitaminOpinion)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}