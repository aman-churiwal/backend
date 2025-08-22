package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class BundleGetResponse {

    private final String name;
    private final String category;
    private final String description;
    private final BigDecimal price;
    private final String code;
    private final String url;
    private final boolean isVegan;
    private final boolean isActive;
    private final List<IngredientGetResponse> ingredients;

    @JsonCreator
    public BundleGetResponse(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "category", required = true) String category,
            @JsonProperty(value = "description", required = true) String description,
            @JsonProperty(value = "price", required = true) BigDecimal price,
            @JsonProperty(value = "code", required = true) String code,
            @JsonProperty(value = "url", required = true) String url,
            @JsonProperty(value = "isVegan", required = true) boolean isVegan,
            @JsonProperty(value = "isActive", required = true) boolean isActive,
            @JsonProperty(value = "ingredients", required = true) List<IngredientGetResponse> ingredients
    ) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.code = code;
        this.url = url;
        this.isVegan = isVegan;
        this.isActive = isActive;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<IngredientGetResponse> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BundleGetResponse that = (BundleGetResponse) o;
        return isVegan == that.isVegan && isActive == that.isActive && Objects.equals(name, that.name) && Objects.equals(category, that.category) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(code, that.code) && Objects.equals(url, that.url) && Objects.equals(ingredients, that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, description, price, code, url, isVegan, isActive, ingredients);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BundleGetResponse.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("category='" + category + "'")
                .add("description='" + description + "'")
                .add("price=" + price)
                .add("code='" + code + "'")
                .add("url='" + url + "'")
                .add("isVegan=" + isVegan)
                .add("isActive=" + isActive)
                .add("ingredients=" + ingredients)
                .toString();
    }
}
