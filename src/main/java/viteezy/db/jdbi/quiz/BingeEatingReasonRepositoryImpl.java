package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.BingeEatingReasonRepository;
import viteezy.domain.quiz.BingeEatingReason;

import java.util.List;
import java.util.Optional;

public class BingeEatingReasonRepositoryImpl implements BingeEatingReasonRepository {

    private static final String SELECT_ALL = "SELECT binge_eating_reasons.* FROM binge_eating_reasons ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO binge_eating_reasons (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public BingeEatingReasonRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<BingeEatingReason>> find(Long id) {
        final HandleCallback<Optional<BingeEatingReason>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(BingeEatingReason.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<BingeEatingReason>> findAll() {
        final HandleCallback<List<BingeEatingReason>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(BingeEatingReason.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(BingeEatingReason bingeEatingReason) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(bingeEatingReason)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}