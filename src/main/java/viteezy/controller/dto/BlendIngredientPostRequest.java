package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class BlendIngredientPostRequest {

    private final BigDecimal amount;
    private final String isUnit;

    @JsonCreator
    public BlendIngredientPostRequest(
            @JsonProperty(value = "amount", required = true) BigDecimal amount,
            @JsonProperty(value = "isUnit", required = true) String isUnit
    ) {
        this.amount = amount;
        this.isUnit = isUnit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getIsUnit() {
        return isUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlendIngredientPostRequest that = (BlendIngredientPostRequest) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(isUnit, that.isUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, isUnit);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BlendIngredientPostRequest.class.getSimpleName() + "[", "]")
                .add("amount=" + amount)
                .add("isUnit='" + isUnit + "'")
                .toString();
    }
}
