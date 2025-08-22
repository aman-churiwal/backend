package viteezy.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Product {

    private final Long id;
    private final String name;
    private final String category;
    private final String description;
    private final String code;
    private final String url;
    private final boolean isVegan;
    private final boolean isActive;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public Product(Long id, String name, String category, String description, String code, String url,
                   boolean isVegan, boolean isActive, LocalDateTime creationTimestamp,
                   LocalDateTime modificationTimestamp) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.code = code;
        this.url = url;
        this.isVegan = isVegan;
        this.isActive = isActive;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
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
        Product product = (Product) o;
        return isVegan == product.isVegan && isActive == product.isActive && Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(category, product.category) && Objects.equals(description, product.description) && Objects.equals(code, product.code) && Objects.equals(url, product.url) && Objects.equals(creationTimestamp, product.creationTimestamp) && Objects.equals(modificationTimestamp, product.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, description, code, url, isVegan, isActive, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Product.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("category='" + category + "'")
                .add("description='" + description + "'")
                .add("code='" + code + "'")
                .add("url='" + url + "'")
                .add("isVegan=" + isVegan)
                .add("isActive=" + isActive)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
