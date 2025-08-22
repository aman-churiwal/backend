package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.blend.BlendIngredient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class BlendIngredientGetResponse {

    private final Long id;
    private final Long ingredientId;
    private final Long blendId;
    private final BigDecimal amount;
    private final String isUnit;
    private final BigDecimal price;
    private final String currency;
    private final String explanation;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public BlendIngredientGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "ingredientId", required = true) Long ingredientId,
            @JsonProperty(value = "blendId", required = true) Long blendId,
            @JsonProperty(value = "amount", required = true) BigDecimal amount,
            @JsonProperty(value = "isUnit", required = true) String isUnit,
            @JsonProperty(value = "price", required = true) BigDecimal price,
            @JsonProperty(value = "currency", required = true) String currency,
            @JsonProperty(value = "explanation", required = true) String explanation,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
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

    public static BlendIngredientGetResponse from(BlendIngredient that) {
        return new BlendIngredientGetResponse(
                that.getId(), that.getIngredientId(), that.getBlendId(), that.getAmount(), that.getIsUnit(),
                that.getPrice(), that.getCurrency(), that.getExplanation(), that.getCreationTimestamp(),
                that.getModificationTimestamp()
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
        BlendIngredientGetResponse that = (BlendIngredientGetResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ingredientId, that.ingredientId) &&
                Objects.equals(blendId, that.blendId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(isUnit, that.isUnit) &&
                Objects.equals(price, that.price) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(explanation, that.explanation) &&
                Objects.equals(creationTimestamp, that.creationTimestamp) &&
                Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, ingredientId, blendId, amount, isUnit, price, currency, explanation, creationTimestamp,
                modificationTimestamp
        );
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BlendIngredientGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("ingredientId=" + ingredientId)
                .add("blendId=" + blendId)
                .add("amount=" + amount)
                .add("isUnit='" + isUnit + "'")
                .add("price=" + price)
                .add("currency='" + currency + "'")
                .add("explanation='" + explanation + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
