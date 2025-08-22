package viteezy.domain.blend;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class BlendIngredient {

    private final Long id;
    private final Long ingredientId;
    private final Long blendId;
    private final BigDecimal amount;
    private final String isUnit;
    private final BigDecimal price;
    private final String currency;
    private final String explanation;
    private Boolean isGoal;
    private Integer priorityScore;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public BlendIngredient(
            Long id, Long ingredientId, Long blendId, BigDecimal amount, String isUnit,
            BigDecimal price, String currency, String explanation, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.blendId = blendId;
        this.amount = amount;
        this.isUnit = isUnit;
        this.price = price;
        this.currency = currency;
        this.explanation = explanation;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public BlendIngredient(
            Long id, Long ingredientId, Long blendId, BigDecimal amount, String isUnit, BigDecimal price, String currency,
            String explanation, Boolean isGoal, Integer priorityScore, LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.blendId = blendId;
        this.amount = amount;
        this.isUnit = isUnit;
        this.price = price;
        this.currency = currency;
        this.explanation = explanation;
        this.isGoal = isGoal;
        this.priorityScore = priorityScore;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    @Deprecated
    public static BlendIngredient build(Long ingredientId, Long blendId, BigDecimal amount, String isUnit, String explanation) {
        final LocalDateTime now = LocalDateTime.now();
        return new BlendIngredient(null, ingredientId, blendId, amount, isUnit, null, null, explanation, now, now);
    }

    public static BlendIngredient buildV2(Long ingredientId, Long blendId, BigDecimal amount, String isUnit, String explanation, Boolean isGoal, Integer priorityScore) {
        final LocalDateTime now = LocalDateTime.now();
        return new BlendIngredient(null, ingredientId, blendId, amount, isUnit, null, null, explanation, isGoal, priorityScore, now, now);
    }

    public static BlendIngredient addPrice(BlendIngredient that, BigDecimal price, String currency) {
        return new BlendIngredient(
                that.getId(), that.getIngredientId(), that.getBlendId(), that.getAmount(), that.getIsUnit(),
                price, currency, that.getExplanation(), that.getCreationTimestamp(), LocalDateTime.now()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public Long getBlendId() {
        return blendId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getIsUnit() {
        return isUnit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getExplanation() {
        return explanation;
    }

    public Boolean getGoal() {
        return isGoal;
    }

    public Integer getPriorityScore() {
        return priorityScore;
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
        BlendIngredient that = (BlendIngredient) o;
        return Objects.equals(id, that.id) && Objects.equals(ingredientId, that.ingredientId) && Objects.equals(blendId, that.blendId) && Objects.equals(amount, that.amount) && Objects.equals(isUnit, that.isUnit) && Objects.equals(price, that.price) && Objects.equals(currency, that.currency) && Objects.equals(explanation, that.explanation) && Objects.equals(isGoal, that.isGoal) && Objects.equals(priorityScore, that.priorityScore) && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredientId, blendId, amount, isUnit, price, currency, explanation, isGoal, priorityScore, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BlendIngredient.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("ingredientId=" + ingredientId)
                .add("blendId=" + blendId)
                .add("amount=" + amount)
                .add("isUnit='" + isUnit + "'")
                .add("price=" + price)
                .add("currency='" + currency + "'")
                .add("explanation='" + explanation + "'")
                .add("isGoal=" + isGoal)
                .add("priorityScore=" + priorityScore)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
