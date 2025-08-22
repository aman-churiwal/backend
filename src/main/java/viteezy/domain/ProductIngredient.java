package viteezy.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class ProductIngredient {

    private final Long id;
    private final Long productId;
    private final Long ingredientId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public ProductIngredient(Long id, Long productId, Long ingredientId, LocalDateTime creationTimestamp,
                             LocalDateTime modificationTimestamp) {
        this.id = id;
        this.productId = productId;
        this.ingredientId = ingredientId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getIngredientId() {
        return ingredientId;
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
        ProductIngredient that = (ProductIngredient) o;
        return Objects.equals(id, that.id) && Objects.equals(productId, that.productId) && Objects.equals(ingredientId, that.ingredientId) && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, ingredientId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProductIngredient.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("productId=" + productId)
                .add("ingredientId=" + ingredientId)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
