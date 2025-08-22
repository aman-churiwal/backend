package viteezy.domain.postnl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class Address {

    private final Integer resultNumber;
    private final Integer mailabilityScore;
    private final Integer resultPercentage;
    private final List<String> formattedAddress;
    private final String street;
    private final Integer houseNumber;
    private final String houseNumberAddition;
    private final String postalCode;
    private final String city;
    private final String country;
    private final String countryIso2;
    private final String countryIso3;
    private final String locality;
    private final String state;
    private final BigDecimal latitude;
    private final BigDecimal longitude;

    @JsonCreator
    public Address(@JsonProperty("ResultNumber") Integer resultNumber,
                   @JsonProperty("MailabilityScore") Integer mailabilityScore,
                   @JsonProperty("ResultPercentage") Integer resultPercentage,
                   @JsonProperty("FormattedAddress") List<String> formattedAddress,
                   @JsonProperty("Street") String street,
                   @JsonProperty("HouseNumber") Integer houseNumber,
                   @JsonProperty("HouseNumberAddition") String houseNumberAddition,
                   @JsonProperty("PostalCode") String postalCode,
                   @JsonProperty("City") String city,
                   @JsonProperty("Country") String country,
                   @JsonProperty("CountryIso2") String countryIso2,
                   @JsonProperty("CountryIso3") String countryIso3,
                   @JsonProperty("Locality") String locality,
                   @JsonProperty("State") String state,
                   @JsonProperty("Latitude") BigDecimal latitude,
                   @JsonProperty("Longitude") BigDecimal longitude) {
        this.resultNumber = resultNumber;
        this.mailabilityScore = mailabilityScore;
        this.resultPercentage = resultPercentage;
        this.formattedAddress = formattedAddress;
        this.street = street;
        this.houseNumber = houseNumber;
        this.houseNumberAddition = houseNumberAddition;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.countryIso2 = countryIso2;
        this.countryIso3 = countryIso3;
        this.locality = locality;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getResultNumber() {
        return resultNumber;
    }

    public Integer getMailabilityScore() {
        return mailabilityScore;
    }

    public Integer getResultPercentage() {
        return resultPercentage;
    }

    public List<String> getFormattedAddress() {
        return formattedAddress;
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

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryIso2() {
        return countryIso2;
    }

    public String getCountryIso3() {
        return countryIso3;
    }

    public String getLocality() {
        return locality;
    }

    public String getState() {
        return state;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }
}
