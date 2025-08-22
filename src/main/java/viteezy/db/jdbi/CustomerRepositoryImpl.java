package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.CustomerRepository;
import viteezy.domain.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CustomerRepositoryImpl implements CustomerRepository {

    private static final String SELECT_ALL = "SELECT * FROM customers ";
    private static final String SELECT_ALL_JOIN_ON_BLENDS = SELECT_ALL +
            "JOIN blends ON blends.customer_id=customers.id " +
            "WHERE blends.external_reference = :blendExternalReference";
    private static final String SELECT_ALL_JOIN_ON_QUIZ = SELECT_ALL +
            "JOIN quiz ON quiz.customer_id=customers.id " +
            "WHERE quiz.external_reference = :quizExternalReference";
    protected static final String JOIN_PAYMENT_PLANS_ON_CUSTOMERS_ID = "JOIN payment_plans on customers.id = payment_plans.customer_id ";
    private static final String SELECT_ALL_JOIN_ON_PAYMENT_PLAN = SELECT_ALL + JOIN_PAYMENT_PLANS_ON_CUSTOMERS_ID +
            "WHERE payment_plans.external_reference = :paymentPlanExternalReference";
    private static final String INSERT_QUERY = "INSERT INTO " +
            "customers (email,opt_in,external_reference,mollie_customer_id,active_campaign_contact_id," +
            "active_campaign_ecom_customer_id,klaviyo_profile_id,ga_id,facebook_pixel,user_agent,address,first_name,last_name,phone_number,street,house_number," +
            "house_number_addition,postcode,city,country) " +
            "VALUES (:email,:optIn,:externalReference,:mollieCustomerId,:activeCampaignContactId," +
            ":activeCampaignEcomCustomerId,:klaviyoProfileId,:gaId,:facebookPixel,:userAgent,:userIpAddress,:firstName,:lastName,:phoneNumber,:street,:houseNumber," +
            ":houseNumberAddition,:postcode,:city,:country) ";
    private static final String INSERT_OR_UPDATE_QUERY = INSERT_QUERY +
            "ON DUPLICATE KEY UPDATE " +
            "first_name = :firstName, email = :email, opt_in = :optIn, mollie_customer_id = :mollieCustomerId, " +
            "ga_id = :gaId, facebook_pixel = :facebookPixel, user_agent = :userAgent, address = :userIpAddress, " +
            "last_modified = NOW(), id = LAST_INSERT_ID(id)";
    private static final String UPDATE_QUERY = "UPDATE customers SET " +
            "first_name = :firstName, last_name = :lastName, phone_number = :phoneNumber, email = :email, " +
            "opt_in = :optIn ,street = :street, house_number = :houseNumber, house_number_addition = :houseNumberAddition, " +
            "postcode = :postcode, city = :city, country = :country, mollie_customer_id = :mollieCustomerId, " +
            "active_campaign_contact_id = :activeCampaignContactId, " +
            "active_campaign_ecom_customer_id = :activeCampaignEcomCustomerId, klaviyo_profile_id = :klaviyoProfileId, ga_id = :gaId, " +
            "facebook_pixel = :facebookPixel, user_agent = :userAgent, address = :userIpAddress, last_modified = NOW() ";

    private static final String UPDATE_BY_ID = UPDATE_QUERY + "WHERE id = :id";
    private static final String UPDATE_REFERRAL_BY_ID = "" +
            "UPDATE customers " +
            "SET referral_code = :referralCode " +
            "WHERE id = :id";

    private static final String UPDATE_ACTIVE_CAMPAIGN_CONTACT_BY_ID = "" +
            "UPDATE customers " +
            "SET active_campaign_contact_id = :activeCampaignContactId " +
            "WHERE id = :id";
    private static final String UPDATE_ACTIVE_CAMPAIGN_ECOM_CUSTOMER_BY_ID = "" +
            "UPDATE customers " +
            "SET active_campaign_ecom_customer_id = :activeCampaignEcomCustomerId " +
            "WHERE id = :id";

    private final Jdbi jdbi;

    public CustomerRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<Customer> find(Long id) {
        final HandleCallback<Customer, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Customer.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Customer> find(UUID externalReference) {
        final HandleCallback<Customer, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE external_reference = :externalReference")
                .bind("externalReference", externalReference)
                .mapTo(Customer.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Customer> find(String email) {
        final HandleCallback<Customer, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE email = :email")
                .bind("email", email)
                .mapTo(Customer.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Customer>> findByEmailWithNeedle(String email) {
        final String needle = "%" + email + "%";
        final HandleCallback<List<Customer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE email LIKE :needle")
                .bind("needle", needle)
                .mapTo(Customer.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Customer>> findByPhoneNumberWithNeedle(String phoneNumber) {
        final String needle = "%" + phoneNumber + "%";
        final HandleCallback<List<Customer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE phone_number LIKE :needle")
                .bind("needle", needle)
                .mapTo(Customer.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Customer>> findByPostcodeWithNeedle(String postcode) {
        final String needle = "%" + postcode + "%";
        final HandleCallback<List<Customer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE postcode LIKE :needle")
                .bind("needle", needle)
                .mapTo(Customer.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Customer>> findByNameWithNeedle(String name) {
        final String needle = "%" + name + "%";
        final HandleCallback<List<Customer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE CONCAT(first_name,' ',last_name) LIKE :needle")
                .bind("needle", needle)
                .mapTo(Customer.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Customer> findByEmailWithActivePaymentPlan(String email) {
        final HandleCallback<Customer, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + JOIN_PAYMENT_PLANS_ON_CUSTOMERS_ID + "WHERE customers.email = :email AND payment_plans.status = 'ACTIVE' LIMIT 1")
                .bind("email", email)
                .mapTo(Customer.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Customer> findByBlend(UUID blendExternalReference) {
        final HandleCallback<Customer, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_JOIN_ON_BLENDS)
                .bind("blendExternalReference", blendExternalReference)
                .mapTo(Customer.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Customer> findByQuiz(UUID quizExternalReference) {
        final HandleCallback<Customer, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_JOIN_ON_QUIZ)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(Customer.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<Customer>> findByReferral(String referralCode) {
        final HandleCallback<Optional<Customer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE referral_code = :referralCode")
                .bind("referralCode", referralCode)
                .mapTo(Customer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Customer> findByPaymentPlanExternalReference(UUID paymentPlanExternalReference) {
        final HandleCallback<Customer, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_JOIN_ON_PAYMENT_PLAN)
                .bind("paymentPlanExternalReference", paymentPlanExternalReference)
                .mapTo(Customer.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Customer>> findKlaviyoToBeImported() {
        final HandleCallback<List<Customer>, RuntimeException> queryCallback = handle -> handle
                .createQuery("select customers.* " +
                        "from customers, payment_plans, payments " +
                        "where payment_plans.customer_id = customers.id and payment_plans.id = payments.payment_plan_id " +
                        "and payments.status = 'paid' " +
                        "and payment_plans.status in ('STOPPED') " +
                        "and klaviyo_profile_id is null " +
                        "group by customers.id")
                .mapTo(Customer.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Customer> updateReferralCode(Long customerId, String referralCode) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_REFERRAL_BY_ID)
                .bind("id", customerId)
                .bind("referralCode", referralCode)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(__ -> find(customerId));
    }

    @Override
    public Try<Customer> update(Customer customer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_ID)
                .bindBean(customer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(__ -> customer);
    }

    @Override
    public Try<Customer> updateWithActiveContactId(Long customerId, Long activeContactId) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_ACTIVE_CAMPAIGN_CONTACT_BY_ID)
                .bind("id", customerId)
                .bind("activeCampaignContactId", activeContactId)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(__ -> find(customerId));
    }

    @Override
    public Try<Customer> updateWithActiveEcomCustomerId(Long customerId, Long activeEcomCustomerId) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_ACTIVE_CAMPAIGN_ECOM_CUSTOMER_BY_ID)
                .bind("id", customerId)
                .bind("activeCampaignEcomCustomerId", activeEcomCustomerId)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(__ -> find(customerId));
    }

    @Override
    public Try<Customer> save(Customer customer) {
        return saveCustomer(customer, INSERT_QUERY);
    }

    @Override
    public Try<Customer> saveOrUpdate(Customer customer) {
        return saveCustomer(customer, INSERT_OR_UPDATE_QUERY);
    }

    private Try<Customer> saveCustomer(Customer customer, String insertQuery) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(insertQuery)
                .bindBean(customer)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }
}
