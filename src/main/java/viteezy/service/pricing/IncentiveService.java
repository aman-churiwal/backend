package viteezy.service.pricing;

import io.vavr.control.Try;
import viteezy.domain.pricing.Incentive;
import viteezy.domain.pricing.IncentiveType;
import viteezy.domain.pricing.IncentiveStatus;

import java.util.Optional;
import java.util.UUID;

public interface IncentiveService {

    Try<Optional<Incentive>> findFromCustomer(Long customerId, IncentiveStatus status, IncentiveType incentiveType);

    Try<Incentive> save(UUID customerExternalReference, IncentiveType incentiveType);

    Try<Incentive> updateStatus(Long id, IncentiveStatus status);
}
