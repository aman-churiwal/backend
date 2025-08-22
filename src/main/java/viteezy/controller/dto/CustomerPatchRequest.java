package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class CustomerPatchRequest {

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

    @JsonCreator
    public CustomerPatchRequest(
            @JsonProperty(value = "firstName", required = true) String firstName,
            @JsonProperty(value = "lastName", required = true) String lastName,
            @JsonProperty(value = "phoneNumber", required = true) String phoneNumber,
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "street", required = true) String street,
            @JsonProperty(value = "houseNumber", required = true) Integer houseNumber,
            @JsonProperty(value = "houseNumberAddition", required = false) String houseNumberAddition,
            @JsonProperty(value = "postcode", required = true) String postcode,
            @JsonProperty(value = "city", required = true) String city,
            @JsonProperty(value = "country", required = true) String country) {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerPatchRequest that = (CustomerPatchRequest) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(email, that.email) &&
                Objects.equals(street, that.street) &&
                Objects.equals(houseNumber, that.houseNumber) &&
                Objects.equals(houseNumberAddition, that.houseNumberAddition) &&
                Objects.equals(postcode, that.postcode) &&
                Objects.equals(city, that.city) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phoneNumber, email, street, houseNumber, houseNumberAddition, postcode, city, country);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CustomerPatchRequest.class.getSimpleName() + "[", "]")
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
                .toString();
    }
}