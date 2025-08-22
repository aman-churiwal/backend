package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerMapper implements RowMapper<Customer> {

    @Override
    public Customer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String email = rs.getString("email");
        final boolean optIn = rs.getBoolean("opt_in");
        final UUID externalReference = getExternalReference(rs);
        final String mollieCustomerId = rs.getString("mollie_customer_id");
        final Long activeCampaignContactId = (Long) rs.getObject("active_campaign_contact_id");
        final Long activeCampaignEcomCustomerId = (Long) rs.getObject("active_campaign_ecom_customer_id");
        final String klaviyoProfileId = rs.getString("klaviyo_profile_id");
        final String gaId = rs.getString("ga_id");
        final String facebookPixel = rs.getString("facebook_pixel");
        final String userAgent = rs.getString("user_agent");
        final String userIpAddress = rs.getString("address");
        final String firstName = rs.getString("first_name");
        final String lastName = rs.getString("last_name");
        final String phoneNumber = rs.getString("phone_number");
        final String street = rs.getString("street");
        final Integer houseNumber = (Integer) rs.getObject("house_number");
        final String houseNumberAddition = rs.getString("house_number_addition");
        final String postcode = rs.getString("postcode");
        final String city = rs.getString("city");
        final String country = rs.getString("country");
        final String referralCode = rs.getString("referral_code");
        final LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
        final LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
        return new Customer(id, email, optIn, externalReference, mollieCustomerId, activeCampaignContactId,
                activeCampaignEcomCustomerId, klaviyoProfileId, gaId, facebookPixel, userAgent, userIpAddress, firstName, lastName, phoneNumber, street, houseNumber,
                houseNumberAddition, postcode, city, country, referralCode, creationDate, lastModified);
    }

    private UUID getExternalReference(ResultSet rs) throws SQLException {
        String rawExternalReference = rs.getString("external_reference");
        return UUID.fromString(rawExternalReference);
    }
}
