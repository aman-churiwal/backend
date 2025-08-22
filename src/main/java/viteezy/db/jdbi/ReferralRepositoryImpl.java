package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.ReferralRepository;
import viteezy.domain.pricing.Referral;
import viteezy.domain.pricing.ReferralStatus;

import java.util.Optional;

public class ReferralRepositoryImpl implements ReferralRepository {
    private static final String SELECT_ALL_BASE_QUERY = "SELECT * FROM referrals ";
    private static final String INSERT_QUERY = "" +
            "INSERT INTO referrals (from_id, to_id, amount, status, creation_date, last_modified) " +
            "VALUES (:from, :to, :amount, :status, :creationDate, :lastModified)";

    private static final String UPDATE_STATUS_QUERY = "" +
            "UPDATE referrals SET status = :status WHERE id = :id";

    private final Jdbi jdbi;

    public ReferralRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<Optional<Referral>> findFromCustomer(Long customerId, ReferralStatus referralStatus) {
        final HandleCallback<Optional<Referral>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE from_id = :customerId AND status = :status ORDER BY last_modified LIMIT 1")
                .bind("customerId", customerId)
                .bind("status", referralStatus)
                .mapTo(Referral.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Referral> save(Referral referral) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(referral)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }

    @Override
    public Try<Referral> updateStatus(Long id, ReferralStatus referralStatus) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_STATUS_QUERY)
                .bind("id", id)
                .bind("status", referralStatus)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(__ -> find(id));
    }

    private Try<Referral> find(Long id) {
        final HandleCallback<Referral, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Referral.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }
}
