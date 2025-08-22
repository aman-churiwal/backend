package viteezy.controller.dto.ingredient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.ingredient.IngredientPrice;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class IngredientPriceGetResponse {

    private final Long id;
    private final Long ingredientId;
    private final BigDecimal amount;
    private final String internationalSystemUnit;
    private final BigDecimal price;
    private final String currency;

    @JsonCreator
    public IngredientPriceGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "ingredientId", required = true) Long ingredientId,
            @JsonProperty(value = "amount", required = true) BigDecimal amount,
            @JsonProperty(value = "internationalSystemUnit", required = true) String internationalSystemUnit,
            @JsonProperty(value = "price", required = true) BigDecimal price,
            @JsonProperty(value = "currency", required = true)String currency) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.amount = amount;
        this.internationalSystemUnit = internationalSystemUnit;
        this.price = price;
        this.currency = currency;
    }

    public static IngredientPriceGetResponse from(IngredientPrice that) {
        return new IngredientPriceGetResponse(
                that.getId(), that.getIngredientId(), that.getAmount(), that.getInternationalSystemUnit(), that.getPrice(),
                that.getCurrency()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getInternationalSystemUnit() {
        return internationalSystemUnit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientPriceGetResponse that = (IngredientPriceGetResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ingredientId, that.ingredientId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(internationalSystemUnit, that.internationalSystemUnit) &&
                Objects.equals(price, that.price) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredientId, amount, internationalSystemUnit, price, currency);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IngredientPriceGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("ingredientId=" + ingredientId)
                .add("amount=" + amount)
                .add("internationalSystemUnit='" + internationalSystemUnit + "'")
                .add("price=" + price)
                .add("currency='" + currency + "'")
                .toString();
    }
}
