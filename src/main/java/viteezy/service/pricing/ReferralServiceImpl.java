package viteezy.service.pricing;

import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.configuration.referral.ReferralConfiguration;
import viteezy.db.CustomerRepository;
import viteezy.db.ReferralRepository;
import viteezy.domain.Customer;
import viteezy.domain.pricing.Referral;
import viteezy.domain.pricing.ReferralDiscount;
import viteezy.domain.pricing.ReferralStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ReferralServiceImpl implements ReferralService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferralService.class);

    private final ReferralRepository referralRepository;
    private final CustomerRepository customerRepository;
    private final ReferralDiscount referralDiscount;

    protected ReferralServiceImpl(ReferralRepository referralRepository,
                               ReferralConfiguration referralConfiguration,
                               CustomerRepository customerRepository) {
        this.referralRepository = referralRepository;
        this.customerRepository = customerRepository;
        this.referralDiscount = new ReferralDiscount(referralConfiguration.getDiscountAmount(), referralConfiguration.getMinimumPrice());
    }

    @Override
    public Try<Optional<Referral>> findFromCustomer(Long customerId, ReferralStatus referralStatus) {
        return referralRepository.findFromCustomer(customerId, referralStatus);
    }

    @Override
    public ReferralDiscount getReferralDiscount() {
        return referralDiscount;
    }

    @Override
    public Try<Referral> updateStatus(Long id, ReferralStatus referralStatus) {
        return referralRepository.updateStatus(id, referralStatus)
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Optional<Customer>> findCustomerByReferralCode(String referralCode) {
        return customerRepository.findByReferral(referralCode);
    }

    @Override
    public Try<Optional<Long>> createPendingReferral(String couponCode, BigDecimal referralDiscount, Long customerIdTo) {
        if (StringUtils.isNotBlank(couponCode) && referralDiscount.compareTo(BigDecimal.ZERO) > 0) {
            return findCustomerByReferralCode(couponCode)
                    .map(optional -> optional.map(customer -> referralRepository.save(buildReferral(customer.getId(), customerIdTo))))
                    .flatMap(optionalTryReferralToNarrowedTry());
        } else {
            return Try.of(Optional::empty);
        }
    }

    Function<Optional<Try<Referral>>, Try<Optional<Long>>> optionalTryReferralToNarrowedTry() {
        return optional -> Try.of(() -> optional.map(Try::get).map(Referral::getId));
    }

    private Referral buildReferral(Long customerIdFrom, Long customerIdTo) {
        return new Referral(null, customerIdFrom, customerIdTo, new BigDecimal(referralDiscount.getDiscountAmount()), ReferralStatus.PENDING, LocalDateTime.now(), LocalDateTime.now());
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
