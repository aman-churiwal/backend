package viteezy.domain.ingredient;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class IngredientComponent {

    private final Long id;
    private final Long ingredientId;
    private final String name;
    private final String amount;
    private final String percentage;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public IngredientComponent(Long id, Long ingredientId, String name, String amount, String percentage,
                               LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.name = name;
        this.amount = amount;
        this.percentage = percentage;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getPercentage() {
        return percentage;
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
        IngredientComponent that = (IngredientComponent) o;
        return Objects.equals(id, that.id) && Objects.equals(ingredientId, that.ingredientId) && Objects.equals(name, that.name) && Objects.equals(amount, that.amount) && Objects.equals(percentage, that.percentage) && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredientId, name, amount, percentage, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IngredientComponent.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("ingredientId=" + ingredientId)
                .add("name='" + name + "'")
                .add("amount='" + amount + "'")
                .add("percentage='" + percentage + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
