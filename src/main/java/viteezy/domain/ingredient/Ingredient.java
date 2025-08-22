package viteezy.domain.ingredient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Ingredient {

    private final Long id;
    private final String name;
    private final String type;
    private final String description;
    private final String code;
    private final String url;
    private final Integer strapiContentId;
    private final Boolean isAFlavour;
    private final Boolean isVegan;
    private final Integer priority;
    private final boolean isActive;
    private final String sku;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public Ingredient(Long id, String name, String type, String description, String code, String url,
                      Integer strapiContentId, Boolean isAFlavour, Boolean isVegan, Integer priority, boolean isActive,
                      String sku, LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.code = code;
        this.url = url;
        this.strapiContentId = strapiContentId;
        this.isAFlavour = isAFlavour;
        this.isVegan = isVegan;
        this.priority = priority;
        this.isActive = isActive;
        this.sku = sku;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
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

    public String getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public Integer getStrapiContentId() {
        return strapiContentId;
    }

    public Boolean getIsAFlavour() {
        return isAFlavour;
    }

    public Boolean getIsVegan() {
        return isVegan;
    }

    public Integer getPriority() {
        return priority;
    }

    public boolean getIsActive() {
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
        Ingredient that = (Ingredient) o;
        return isActive == that.isActive && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(description, that.description) && Objects.equals(code, that.code) && Objects.equals(url, that.url) && Objects.equals(strapiContentId, that.strapiContentId) && Objects.equals(isAFlavour, that.isAFlavour) && Objects.equals(isVegan, that.isVegan) && Objects.equals(priority, that.priority) && Objects.equals(sku, that.sku) && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, description, code, url, strapiContentId, isAFlavour, isVegan, priority, isActive, sku, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Ingredient.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("type='" + type + "'")
                .add("description='" + description + "'")
                .add("code='" + code + "'")
                .add("url='" + url + "'")
                .add("strapiContentId=" + strapiContentId)
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
