package viteezy.domain.pricing;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class ReferralDiscount {

    private final Integer discountAmount;
    private final BigDecimal minimumPrice;

    public ReferralDiscount(Integer discountAmount, BigDecimal minimumPrice) {
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
        ReferralDiscount that = (ReferralDiscount) o;
        return Objects.equals(discountAmount, that.discountAmount) && Objects.equals(minimumPrice, that.minimumPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountAmount, minimumPrice);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ReferralDiscount.class.getSimpleName() + "[", "]")
                .add("discountAmount=" + discountAmount)
                .add("minimumPrice=" + minimumPrice)
                .toString();
    }
}
