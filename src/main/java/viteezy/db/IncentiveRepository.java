package viteezy.db;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.pricing.Incentive;
import viteezy.domain.pricing.IncentiveType;
import viteezy.domain.pricing.IncentiveStatus;

import java.util.Optional;

public interface IncentiveRepository {
    Try<Optional<Incentive>> findFromCustomer(Long customerId, IncentiveStatus status, IncentiveType incentiveType);

    @Transactional(transactionManager = "transactionManager")
    Try<Incentive> save(Incentive incentive);

    @Transactional(transactionManager = "transactionManager")
    Try<Incentive> updateStatus(Long id, IncentiveStatus status);
}
