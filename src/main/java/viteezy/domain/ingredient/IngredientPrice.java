package viteezy.domain.ingredient;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class IngredientPrice {

    private final Long id;
    private final Long ingredientId;
    private final BigDecimal amount;
    private final String internationalSystemUnit;
    private final BigDecimal price;
    private final String currency;

    public IngredientPrice(Long id, Long ingredientId, BigDecimal amount, String internationalSystemUnit, BigDecimal price, String currency) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.amount = amount;
        this.internationalSystemUnit = internationalSystemUnit;
        this.price = price;
        this.currency = currency;
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
        IngredientPrice that = (IngredientPrice) o;
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
        return new StringJoiner(", ", IngredientPrice.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("ingredientId=" + ingredientId)
                .add("amount=" + amount)
                .add("internationalSystemUnit='" + internationalSystemUnit + "'")
                .add("price=" + price)
                .add("currency='" + currency + "'")
                .toString();
    }
}
