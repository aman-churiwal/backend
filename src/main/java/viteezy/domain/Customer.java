package viteezy.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Customer {
    private final Long id;
    private final String email;
    private final Boolean optIn;
    private final UUID externalReference;
    private final String mollieCustomerId;
    private final Long activeCampaignContactId;
    private final Long activeCampaignEcomCustomerId;
    private final String klaviyoProfileId;
    private final String gaId;
    private final String facebookPixel;
    private final String userAgent;
    private final String userIpAddress;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String street;
    private final Integer houseNumber;
    private final String houseNumberAddition;
    private final String postcode;
    private final String city;
    private final String country;
    private final String referralCode;
    private final LocalDateTime creationDate;
    private final LocalDateTime lastModified;

    public Customer(
            Long id, String email, Boolean optIn, UUID externalReference, String mollieCustomerId,
            Long activeCampaignContactId, Long activeCampaignEcomCustomerId, String klaviyoProfileId, String gaId,
            String facebookPixel, String userAgent, String userIpAddress, String firstName, String lastName,
            String phoneNumber, String street, Integer houseNumber, String houseNumberAddition, String postcode,
            String city, String country, String referralCode, LocalDateTime creationDate, LocalDateTime lastModified) {
        this.id = id;
        this.email = email;
        this.optIn = optIn;
        this.externalReference = externalReference;
        this.mollieCustomerId = mollieCustomerId;
        this.activeCampaignContactId = activeCampaignContactId;
        this.activeCampaignEcomCustomerId = activeCampaignEcomCustomerId;
        this.klaviyoProfileId = klaviyoProfileId;
        this.gaId = gaId;
        this.facebookPixel = facebookPixel;
        this.userAgent = userAgent;
        this.userIpAddress = userIpAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.houseNumber = houseNumber;
        this.houseNumberAddition = houseNumberAddition;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.referralCode = referralCode;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
    }

    public static Customer buildWithPixels(
            Customer that, String gaId, String facebookPixel, String userAgent, String userIpAddress
    ) {
        return new Customer(
                that.id, that.email, that.optIn, that.externalReference, that.mollieCustomerId,
                that.activeCampaignContactId, that.activeCampaignEcomCustomerId, that.klaviyoProfileId, gaId, facebookPixel, userAgent, userIpAddress, that.firstName, that.lastName,
                that.phoneNumber, that.street, that.houseNumber, that.houseNumberAddition, that.postcode, that.city, that.country, that.referralCode,
                that.creationDate, that.lastModified
        );
    }

    public static Customer buildWithContactInformation(
            Customer that, String mollieCustomerId, String gaId, String facebookPixel, String userAgent, String userIpAddress, String firstName, String lastName, String phoneNumber,
            String email, String street, Integer houseNumber, String houseNumberAddition, String postcode, String city,
            String country
    ) {
        return new Customer(
                that.getId(), email, that.getOptIn(), that.getExternalReference(), mollieCustomerId,
                that.getActiveCampaignContactId(), that.getActiveCampaignEcomCustomerId(), that.getKlaviyoProfileId(), gaId, facebookPixel, userAgent, userIpAddress, firstName, lastName,
                phoneNumber, street, houseNumber, houseNumberAddition, postcode, city, country, that.getReferralCode(),
                that.getCreationDate(), LocalDateTime.now()
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getOptIn() {
        return optIn;
    }

    public UUID getExternalReference() {
        return externalReference;
    }

    public String getMollieCustomerId() {
        return mollieCustomerId;
    }

    public Long getActiveCampaignContactId() {
        return activeCampaignContactId;
    }

    public Long getActiveCampaignEcomCustomerId() {
        return activeCampaignEcomCustomerId;
    }

    public String getKlaviyoProfileId() {
        return klaviyoProfileId;
    }

    public String getGaId() {
        return gaId;
    }

    public String getFacebookPixel() {
        return facebookPixel;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public String getHouseNumberAddition() {
        return houseNumberAddition;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(email, customer.email) && Objects.equals(optIn, customer.optIn) && Objects.equals(externalReference, customer.externalReference) && Objects.equals(mollieCustomerId, customer.mollieCustomerId) && Objects.equals(activeCampaignContactId, customer.activeCampaignContactId) && Objects.equals(activeCampaignEcomCustomerId, customer.activeCampaignEcomCustomerId) && Objects.equals(klaviyoProfileId, customer.klaviyoProfileId) && Objects.equals(gaId, customer.gaId) && Objects.equals(facebookPixel, customer.facebookPixel) && Objects.equals(userAgent, customer.userAgent) && Objects.equals(userIpAddress, customer.userIpAddress) && Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(phoneNumber, customer.phoneNumber) && Objects.equals(street, customer.street) && Objects.equals(houseNumber, customer.houseNumber) && Objects.equals(houseNumberAddition, customer.houseNumberAddition) && Objects.equals(postcode, customer.postcode) && Objects.equals(city, customer.city) && Objects.equals(country, customer.country) && Objects.equals(referralCode, customer.referralCode) && Objects.equals(creationDate, customer.creationDate) && Objects.equals(lastModified, customer.lastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, optIn, externalReference, mollieCustomerId, activeCampaignContactId, activeCampaignEcomCustomerId, klaviyoProfileId, gaId, facebookPixel, userAgent, userIpAddress, firstName, lastName, phoneNumber, street, houseNumber, houseNumberAddition, postcode, city, country, referralCode, creationDate, lastModified);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Customer.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("email='" + email + "'")
                .add("optIn=" + optIn)
                .add("externalReference=" + externalReference)
                .add("mollieCustomerId='" + mollieCustomerId + "'")
                .add("activeCampaignContactId=" + activeCampaignContactId)
                .add("activeCampaignEcomCustomerId=" + activeCampaignEcomCustomerId)
                .add("klaviyoProfileId='" + klaviyoProfileId + "'")
                .add("gaId='" + gaId + "'")
                .add("facebookPixel='" + facebookPixel + "'")
                .add("userAgent='" + userAgent + "'")
                .add("userIpAddress='" + userIpAddress + "'")
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("phoneNumber='" + phoneNumber + "'")
                .add("street='" + street + "'")
                .add("houseNumber=" + houseNumber)
                .add("houseNumberAddition='" + houseNumberAddition + "'")
                .add("postcode='" + postcode + "'")
                .add("city='" + city + "'")
                .add("country='" + country + "'")
                .add("referralCode='" + referralCode + "'")
                .add("creationDate=" + creationDate)
                .add("lastModified=" + lastModified)
                .toString();
    }
}
