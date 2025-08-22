package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.ingredient.Ingredient;
import viteezy.domain.ingredient.IngredientComponent;
import viteezy.domain.ingredient.IngredientContent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class IngredientGetResponse {

    private final Long id;
    private final String name;
    private final String type;
    private final String description;
    private final IngredientContent ingredientContent;
    private final List<IngredientComponent> ingredientComponents;
    private final String code;
    private final String url;
    private final Integer strapiContentId;
    private final Boolean isAFlavour;
    private final Boolean isVegan;
    private final Integer priority;
    private final Boolean isActive;
    private final String sku;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public IngredientGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "type", required = true) String type,
            @JsonProperty(value = "description", required = true) String description,
            @JsonProperty(value = "content", required = true) IngredientContent ingredientContent,
            @JsonProperty(value = "components", required = true) List<IngredientComponent> ingredientComponents,
            @JsonProperty(value = "code", required = true) String code,
            @JsonProperty(value = "url", required = true) String url,
            @JsonProperty(value = "strapiContentId", required = true) Integer strapiContentId,
            @JsonProperty(value = "isAFlavour", required = true) Boolean isAFlavour,
            @JsonProperty(value = "isVegan", required = true) Boolean isVegan,
            @JsonProperty(value = "priority", required = true) Integer priority,
            @JsonProperty(value = "isActive", required = true) Boolean isActive,
            @JsonProperty(value = "sku", required = true) String sku,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.ingredientContent = ingredientContent;
        this.ingredientComponents = ingredientComponents;
        this.code = code;
        this.url = url;
        this.strapiContentId = strapiContentId;
        this.isAFlavour = isAFlavour;
        this.isVegan = isVegan;
        this.isActive = isActive;
        this.priority = priority;
        this.sku = sku;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public static IngredientGetResponse from(Ingredient that) {
        return new IngredientGetResponse(
                that.getId(), that.getName(), that.getType(), that.getDescription(), null,
                null, that.getCode(), that.getUrl(), that.getStrapiContentId(),
                that.getIsAFlavour(), that.getIsVegan(), that.getPriority(), that.getIsActive(), that.getSku(),
                that.getCreationTimestamp(), that.getModificationTimestamp()
        );
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

    public String getDescription() {
        return description;
    }

    @JsonGetter(value = "content")
    public IngredientContent getIngredientContent() {
        return ingredientContent;
    }

    @JsonGetter(value = "components")
    public List<IngredientComponent> getIngredientComponents() {
        return ingredientComponents;
    }

    public String getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public Integer getStrapiContentId() {
        return strapiContentId;
    }

    @JsonGetter(value = "isAFlavour")
    public Boolean isAFlavour() {
        return isAFlavour;
    }

    public Boolean getVegan() {
        return isVegan;
    }

    public Integer getPriority() {
        return priority;
    }

    @JsonGetter(value = "isActive")
    public Boolean isActive() {
        return isActive;
    }

    public String getSku() {
        return sku;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientGetResponse that = (IngredientGetResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(description, that.description) && Objects.equals(ingredientContent, that.ingredientContent) && Objects.equals(ingredientComponents, that.ingredientComponents) && Objects.equals(code, that.code) && Objects.equals(url, that.url) && Objects.equals(isAFlavour, that.isAFlavour) && Objects.equals(isVegan, that.isVegan) && Objects.equals(priority, that.priority) && Objects.equals(isActive, that.isActive) && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, description, ingredientContent, ingredientComponents, code, url, isAFlavour, isVegan, priority, isActive, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IngredientGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("type='" + type + "'")
                .add("description='" + description + "'")
                .add("ingredientContent=" + ingredientContent)
                .add("ingredientComponents=" + ingredientComponents)
                .add("code='" + code + "'")
                .add("url='" + url + "'")
                .add("isAFlavour=" + isAFlavour)
                .add("isVegan=" + isVegan)
                .add("priority=" + priority)
                .add("isActive=" + isActive)
                .add("sku='" + sku + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
