package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.ingredient.Ingredient;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class IngredientPostRequest {

    private final String name;
    private final String type;
    private final String description;
    private final String excipients;
    private final String claim;
    private final String code;
    private final String url;
    private final Boolean isAFlavour;
    private final Boolean isVegan;
    private final Integer priority;
    private final Boolean isActive;
    private final String sku;

    @JsonCreator
    public IngredientPostRequest(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "type", required = true) String type,
            @JsonProperty(value = "description", required = true) String description,
            @JsonProperty(value = "excipients", required = true) String excipients,
            @JsonProperty(value = "claim", required = true) String claim,
            @JsonProperty(value = "code", required = true) String code,
            @JsonProperty(value = "url", required = true) String url,
            @JsonProperty(value = "isAFlavour", required = true) Boolean isAFlavour,
            @JsonProperty(value = "isVegan", required = true) Boolean isVegan,
            @JsonProperty(value = "priority", required = true) Integer priority,
            @JsonProperty(value = "isActive", required = true) Boolean isActive,
            @JsonProperty(value = "sku", required = true) String sku) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.excipients = excipients;
        this.claim = claim;
        this.code = code;
        this.url = url;
        this.isAFlavour = isAFlavour;
        this.isVegan = isVegan;
        this.priority = priority;
        this.isActive = isActive;
        this.sku = sku;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientPostRequest that = (IngredientPostRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(description, that.description) && Objects.equals(excipients, that.excipients) && Objects.equals(claim, that.claim) && Objects.equals(code, that.code) && Objects.equals(url, that.url) && Objects.equals(isAFlavour, that.isAFlavour) && Objects.equals(isVegan, that.isVegan) && Objects.equals(priority, that.priority) && Objects.equals(isActive, that.isActive) && Objects.equals(sku, that.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, description, excipients, claim, code, url, isAFlavour, isVegan, priority, isActive, sku);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IngredientPostRequest.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("type='" + type + "'")
                .add("description='" + description + "'")
                .add("excipients='" + excipients + "'")
                .add("claim='" + claim + "'")
                .add("code='" + code + "'")
                .add("url='" + url + "'")
                .add("isAFlavour=" + isAFlavour)
                .add("isVegan=" + isVegan)
                .add("priority=" + priority)
                .add("isActive=" + isActive)
                .add("sku='" + sku + "'")
                .toString();
    }

    public Ingredient to() {
        final LocalDateTime now = LocalDateTime.now();
        return new Ingredient(null, this.name, this.type, this.description, this.code, this.url, null, this.isAFlavour, this.isVegan, this.priority, this.isActive, this.sku, now, now);
    }
}