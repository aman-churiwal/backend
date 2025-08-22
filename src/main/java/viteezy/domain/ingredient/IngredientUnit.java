package viteezy.domain.ingredient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class IngredientUnit {

    private final Long id;
    private final Long ingredientId;
    private final Long pharmacistCode;
    private final String pharmacistSize;
    private final BigDecimal pharmacistUnit;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public IngredientUnit(Long id, Long ingredientId, Long pharmacistCode, String pharmacistSize,
                          BigDecimal pharmacistUnit, LocalDateTime creationTimestamp,
                          LocalDateTime modificationTimestamp) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.pharmacistCode = pharmacistCode;
        this.pharmacistSize = pharmacistSize;
        this.pharmacistUnit = pharmacistUnit;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public Long getPharmacistCode() {
        return pharmacistCode;
    }

    public String getPharmacistSize() {
        return pharmacistSize;
    }

    public BigDecimal getPharmacistUnit() {
        return pharmacistUnit;
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
        IngredientUnit that = (IngredientUnit) o;
        return Objects.equals(id, that.id) && Objects.equals(ingredientId, that.ingredientId) && Objects.equals(pharmacistCode, that.pharmacistCode) && Objects.equals(pharmacistSize, that.pharmacistSize) && Objects.equals(pharmacistUnit, that.pharmacistUnit) && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredientId, pharmacistCode, pharmacistSize, pharmacistUnit, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IngredientUnit.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("ingredientId=" + ingredientId)
                .add("pharmacistCode=" + pharmacistCode)
                .add("pharmacistSize='" + pharmacistSize + "'")
                .add("pharmacistUnit=" + pharmacistUnit)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
