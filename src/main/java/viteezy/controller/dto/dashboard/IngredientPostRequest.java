package viteezy.controller.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class IngredientPostRequest {

    private final Long id;
    private final String name;
    private final String type;
    private final String url;
    private final Optional<Integer> strapiContentId;
    private final Boolean isActive;
    private final Optional<String> sku;
    private final Boolean isVegan;
    private final Optional<Integer> priority;
    private final BigDecimal price;
    private final Optional<Long> pharmacistCode;
    private final Optional<String> pharmacistSize;
    private final Optional<BigDecimal> pharmacistUnit;

    @JsonCreator
    public IngredientPostRequest(
            @JsonProperty(value = "id", required = false) Long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "type", required = true) String type,
            @JsonProperty(value = "url", required = true) String url,
            @JsonProperty(value = "strapiContentId", required = false) Optional<Integer> strapiContentId,
            @JsonProperty(value = "isActive", required = true) Boolean isActive,
            @JsonProperty(value = "sku", required = false) Optional<String> sku,
            @JsonProperty(value = "isVegan", required = true) Boolean isVegan,
            @JsonProperty(value = "priority", required = true) Optional<Integer> priority,
            @JsonProperty(value = "price", required = true) BigDecimal price,
            @JsonProperty(value = "pharmacistCode", required = false) Optional<Long> pharmacistCode,
            @JsonProperty(value = "pharmacistSize", required = false) Optional<String> pharmacistSize,
            @JsonProperty(value = "pharmacistUnit", required = false) Optional<BigDecimal> pharmacistUnit) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
        this.strapiContentId = strapiContentId;
        this.isActive = isActive;
        this.sku = sku;
        this.isVegan = isVegan;
        this.priority = priority;
        this.price = price;
        this.pharmacistCode = pharmacistCode;
        this.pharmacistSize = pharmacistSize;
        this.pharmacistUnit = pharmacistUnit;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public Optional<Integer> getStrapiContentId() {
        return strapiContentId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Optional<String> getSku() {
        return sku;
    }

    public Boolean getVegan() {
        return isVegan;
    }

    public Optional<Integer> getPriority() {
        return priority;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Optional<Long> getPharmacistCode() {
        return pharmacistCode;
    }

    public Optional<String> getPharmacistSize() {
        return pharmacistSize;
    }

    public Optional<BigDecimal> getPharmacistUnit() {
        return pharmacistUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientPostRequest that = (IngredientPostRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(url, that.url) && Objects.equals(strapiContentId, that.strapiContentId) && Objects.equals(isActive, that.isActive) && Objects.equals(sku, that.sku) && Objects.equals(isVegan, that.isVegan) && Objects.equals(priority, that.priority) && Objects.equals(price, that.price) && Objects.equals(pharmacistCode, that.pharmacistCode) && Objects.equals(pharmacistSize, that.pharmacistSize) && Objects.equals(pharmacistUnit, that.pharmacistUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, url, strapiContentId, isActive, sku, isVegan, priority, price, pharmacistCode, pharmacistSize, pharmacistUnit);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IngredientPostRequest.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("type='" + type + "'")
                .add("url='" + url + "'")
                .add("strapiContentId=" + strapiContentId)
                .add("isActive=" + isActive)
                .add("sku=" + sku)
                .add("isVegan=" + isVegan)
                .add("priority=" + priority)
                .add("price=" + price)
                .add("pharmacistCode=" + pharmacistCode)
                .add("pharmacistSize=" + pharmacistSize)
                .add("pharmacistUnit=" + pharmacistUnit)
                .toString();
    }
}