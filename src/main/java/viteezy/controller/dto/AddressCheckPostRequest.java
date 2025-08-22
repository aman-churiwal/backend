package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class AddressCheckPostRequest {

    private final String city;
    private final String countryIso;
    private final String postalCode;
    private final String street;
    private final String houseNumber;
    private final String houseNumberAddition;

    @JsonCreator
    public AddressCheckPostRequest(
            @JsonProperty(value = "city", required = false) String city,
            @JsonProperty(value = "countryIso", required = true) String countryIso,
            @JsonProperty(value = "postalCode", required = true) String postalCode,
            @JsonProperty(value = "street", required = false) String street,
            @JsonProperty(value = "houseNumber", required = true) String houseNumber,
            @JsonProperty(value = "houseNumberAddition", required = false) String houseNumberAddition) {
        this.city = city;
        this.countryIso = countryIso;
        this.postalCode = postalCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.houseNumberAddition = houseNumberAddition;
    }

    public String getCity() {
        return city;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getHouseNumberAddition() {
        return houseNumberAddition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressCheckPostRequest that = (AddressCheckPostRequest) o;
        return Objects.equals(city, that.city) && Objects.equals(countryIso, that.countryIso) && Objects.equals(postalCode, that.postalCode) && Objects.equals(street, that.street) && Objects.equals(houseNumber, that.houseNumber) && Objects.equals(houseNumberAddition, that.houseNumberAddition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, countryIso, postalCode, street, houseNumber, houseNumberAddition);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AddressCheckPostRequest.class.getSimpleName() + "[", "]")
                .add("city='" + city + "'")
                .add("countryIso='" + countryIso + "'")
                .add("postalCode='" + postalCode + "'")
                .add("street='" + street + "'")
                .add("houseNumber='" + houseNumber + "'")
                .add("houseNumberAddition='" + houseNumberAddition + "'")
                .toString();
    }
}
