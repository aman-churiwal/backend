package viteezy.service.pricing;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.Customer;
import viteezy.domain.pricing.Referral;
import viteezy.domain.pricing.ReferralDiscount;
import viteezy.domain.pricing.ReferralStatus;

import java.math.BigDecimal;
import java.util.Optional;

public interface ReferralService {

    Try<Optional<Referral>> findFromCustomer(Long customerId, ReferralStatus referralStatus);

    ReferralDiscount getReferralDiscount();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Referral> updateStatus(Long id, ReferralStatus referralStatus);

    Try<Optional<Customer>> findCustomerByReferralCode(String referralCode);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Optional<Long>> createPendingReferral(String couponCode, BigDecimal referralDiscount, Long customerIdTo);
}
