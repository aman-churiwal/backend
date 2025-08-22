package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.BlendRepository;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BlendRepositoryImpl implements BlendRepository {

    private static final String SELECT_ALL = "SELECT blends.* FROM blends ";

    private static final String ORDER_BY_MODIFICATION_TIMESTAMP_DESC_LIMIT_1 = "ORDER BY modification_timestamp DESC LIMIT 1";
    private static final String SELECT_ALL_JOIN_ON_CUSTOMER = SELECT_ALL +
            "JOIN customers on blends.customer_id = customers.id " +
            "WHERE customers.external_reference = :customerExternalReference ";
    private static final String INSERT_QUERY = "" +
            "INSERT INTO blends (status, external_reference, customer_id, quiz_id) " +
            "VALUES (:status, :externalReference, :customerId, :quizId)";

    private static final String UPDATE_QUERY = "" +
            "UPDATE blends " +
            "SET customer_id = :customerId, modification_timestamp = NOW() " +
            "WHERE id = :id";

    private static final String UPDATE_STATUS = "" +
            "UPDATE blends " +
            "SET status = :blendStatus, modification_timestamp = NOW() " +
            "WHERE id = :id";

    private final Jdbi jdbi;

    public BlendRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Blend> find(Long id) {
        final HandleCallback<Blend, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Blend.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Blend> find(UUID externalReference) {
        final HandleCallback<Blend, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE external_reference = :externalReference")
                .bind("externalReference", externalReference)
                .mapTo(Blend.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Blend> findByCustomerExternalReference(UUID customerExternalReference) {
        final HandleCallback<Blend, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_JOIN_ON_CUSTOMER + ORDER_BY_MODIFICATION_TIMESTAMP_DESC_LIMIT_1)
                .bind("customerExternalReference", customerExternalReference)
                .mapTo(Blend.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Blend>> findAllByCustomerExternalReference(UUID customerExternalReference) {
        final HandleCallback<List<Blend>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_JOIN_ON_CUSTOMER)
                .bind("customerExternalReference", customerExternalReference)
                .mapTo(Blend.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<Blend>> findByQuizId(Long quizId) {
        final HandleCallback<Optional<Blend>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE quiz_id = :quizId " + ORDER_BY_MODIFICATION_TIMESTAMP_DESC_LIMIT_1)
                .bind("quizId", quizId)
                .mapTo(Blend.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Blend> save(Blend blend) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(blend)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }

    @Override
    public Try<Blend> update(Blend blend) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_QUERY)
                .bindBean(blend)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(__ -> find(blend.getId()));
    }

    @Override
    public Try<Blend> updateStatus(Long id, BlendStatus blendStatus) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_STATUS)
                .bind("id", id)
                .bind("blendStatus", blendStatus)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(__ -> find(id));
    }
}