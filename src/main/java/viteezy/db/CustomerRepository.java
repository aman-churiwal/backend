package viteezy.db;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Try<Customer> find(Long id);

    Try<Customer> find(UUID externalReference);

    Try<Customer> find(String email);

    Try<List<Customer>> findByEmailWithNeedle(String email);

    Try<List<Customer>> findByPhoneNumberWithNeedle(String phoneNumber);

    Try<List<Customer>> findByPostcodeWithNeedle(String postcode);

    Try<List<Customer>> findByNameWithNeedle(String lastName);

    Try<Customer> findByEmailWithActivePaymentPlan(String email);

    Try<Customer> findByBlend(UUID blendExternalReference);

    Try<Customer> findByQuiz(UUID quizExternalReference);

    Try<Optional<Customer>> findByReferral(String referralCode);

    Try<Customer> findByPaymentPlanExternalReference(UUID paymentPlanExternalReference);

    Try<List<Customer>> findKlaviyoToBeImported();

    @Transactional(transactionManager = "transactionManager")
    Try<Customer> save(Customer customer);

    @Transactional(transactionManager = "transactionManager")
    Try<Customer> saveOrUpdate(Customer customer);

    @Transactional(transactionManager = "transactionManager")
    Try<Customer> updateReferralCode(Long customerId, String referralCode);

    @Transactional(transactionManager = "transactionManager")
    Try<Customer> updateWithActiveContactId(Long customerId, Long activeContactId);

    @Transactional(transactionManager = "transactionManager")
    Try<Customer> updateWithActiveEcomCustomerId(Long customerId, Long activeEcomCustomerId);

    @Transactional(transactionManager = "transactionManager")
    Try<Customer> update(Customer customer);
}
