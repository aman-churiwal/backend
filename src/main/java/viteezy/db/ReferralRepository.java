package viteezy.db;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.pricing.Referral;
import viteezy.domain.pricing.ReferralStatus;

import java.util.Optional;

public interface ReferralRepository {

    Try<Optional<Referral>> findFromCustomer(Long customerId, ReferralStatus referralStatus);

    @Transactional(transactionManager = "transactionManager")
    Try<Referral> save(Referral referral);

    @Transactional(transactionManager = "transactionManager")
    Try<Referral> updateStatus(Long id, ReferralStatus referralStatus);
}
