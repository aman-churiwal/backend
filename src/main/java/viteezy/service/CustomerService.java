package viteezy.service;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.controller.dto.CustomerAddressPostRequest;
import viteezy.controller.dto.CustomerPatchRequest;
import viteezy.controller.dto.quiz.EmailPostRequest;
import viteezy.domain.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Try<Customer> find(Long id);

    Try<Customer> find(UUID externalReference);

    Try<Customer> find(String email);

    Try<List<Customer>> findByEmailWithNeedle(String email);

    Try<List<Customer>> findByPhoneNumberWithNeedle(String phoneNumber);

    Try<List<Customer>> findByPostcodeWithNeedle(String phoneNumber);

    Try<List<Customer>> findByNameWithNeedle(String name);

    Try<Customer> findByEmailWithActivePaymentPlan(String email);

    Try<Customer> findByBlend(UUID blendExternalReference);

    Try<Customer> findByQuiz(UUID quizExternalReference);

    Try<Customer> findByPaymentPlanExternalReference(UUID paymentPlanExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Customer> save(Customer customer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Customer> update(UUID customerExternalReference, CustomerPatchRequest customerPatchRequest);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Customer> saveEmailIfNotActive(UUID quizExternalReference, EmailPostRequest emailPostRequest, String gaId, String facebookPixel, String facebookClick, String userAgent, String userIpAddress);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Customer> updateEmail(UUID quizExternalReference, EmailPostRequest emailPostRequest, String facebookClick);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Customer> createCustomer(UUID blendExternalReference, CustomerAddressPostRequest customer, String gaId, String facebookPixel, String userAgent, String userIpAddress);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Customer> updateReferralCode(Customer customer);
}
