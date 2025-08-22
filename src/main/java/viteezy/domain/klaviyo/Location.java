package viteezy.domain.klaviyo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {

    private final String address1;
    private final String city;
    private final String country;
    private final String zip;

    @JsonCreator
    public Location(@JsonProperty("address1") String address1,
                    @JsonProperty("city") String city,
                    @JsonProperty("country") String country,
                    @JsonProperty("zip") String zip) {
        this.address1 = address1;
        this.city = city;
        this.country = country;
        this.zip = zip;
    }

    public String getAddress1() {
        return address1;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getZip() {
        return zip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(address1, location.address1) && Objects.equals(city, location.city) && Objects.equals(country, location.country) && Objects.equals(zip, location.zip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address1, city, country, zip);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Location.class.getSimpleName() + "[", "]")
                .add("address1='" + address1 + "'")
                .add("city='" + city + "'")
                .add("country='" + country + "'")
                .add("zip='" + zip + "'")
                .toString();
    }
}
