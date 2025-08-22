package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.Customer;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class CustomerGetResponse {

    private final UUID externalReference;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String email;
    private final String street;
    private final Integer houseNumber;
    private final String houseNumberAddition;
    private final String postcode;
    private final String city;
    private final String country;
    private final String referralCode;

    @JsonCreator
    public CustomerGetResponse(
            @JsonProperty(value = "externalReference", required = true) UUID externalReference,
            @JsonProperty(value = "firstName", required = true) String firstName,
            @JsonProperty(value = "lastName", required = true) String lastName,
            @JsonProperty(value = "phoneNumber", required = true) String phoneNumber,
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "street", required = true) String street,
            @JsonProperty(value = "houseNumber", required = true) Integer houseNumber,
            @JsonProperty(value = "houseNumberAddition", required = false) String houseNumberAddition,
            @JsonProperty(value = "postcode", required = true) String postcode,
            @JsonProperty(value = "city", required = true) String city,
            @JsonProperty(value = "country", required = true) String country,
            @JsonProperty(value = "referralCode", required = true) String referralCode) {
        this.externalReference = externalReference;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.street = street;
        this.houseNumber = houseNumber;
        this.houseNumberAddition = houseNumberAddition;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.referralCode = referralCode;
    }

    public UUID getExternalReference() {
        return externalReference;
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

    public String getEmail() {
        return email;
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

    public static CustomerGetResponse from(Customer that) {
        return new CustomerGetResponse(that.getExternalReference(), that.getFirstName(), that.getLastName(),
                that.getPhoneNumber(), that.getEmail(), that.getStreet(), that.getHouseNumber(),
                that.getHouseNumberAddition(), that.getPostcode(), that.getCity(), that.getCountry(),
                that.getReferralCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerGetResponse that = (CustomerGetResponse) o;
        return Objects.equals(externalReference, that.externalReference) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(email, that.email) &&
                Objects.equals(street, that.street) &&
                Objects.equals(houseNumber, that.houseNumber) &&
                Objects.equals(houseNumberAddition, that.houseNumberAddition) &&
                Objects.equals(postcode, that.postcode) &&
                Objects.equals(city, that.city) &&
                Objects.equals(country, that.country) &&
                Objects.equals(referralCode, that.referralCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                externalReference, firstName, lastName, phoneNumber, email, street, houseNumber, houseNumberAddition,
                postcode, city, country, referralCode);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CustomerGetResponse.class.getSimpleName() + "[", "]")
                .add("externalReference=" + externalReference)
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("phoneNumber='" + phoneNumber + "'")
                .add("email='" + email + "'")
                .add("street='" + street + "'")
                .add("houseNumber=" + houseNumber)
                .add("houseNumberAddition='" + houseNumberAddition + "'")
                .add("postcode='" + postcode + "'")
                .add("city='" + city + "'")
                .add("country='" + country + "'")
                .add("referralCode='" + referralCode + "'")
                .toString();
    }
}