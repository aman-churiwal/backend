package viteezy.configuration.referral;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class ReferralConfiguration {
    private final Integer discountAmount;
    private final BigDecimal minimumPrice;

    @JsonCreator
    public ReferralConfiguration(
            @JsonProperty(value = "discount_amount", required = true) Integer discountAmount,
            @JsonProperty(value = "minimum_price", required = true) BigDecimal minimumPrice) {
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferralConfiguration that = (ReferralConfiguration) o;
        return Objects.equals(discountAmount, that.discountAmount) && Objects.equals(minimumPrice, that.minimumPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountAmount, minimumPrice);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ReferralConfiguration.class.getSimpleName() + "[", "]")
                .add("discountAmount=" + discountAmount)
                .add("minimumPrice=" + minimumPrice)
                .toString();
    }
}
