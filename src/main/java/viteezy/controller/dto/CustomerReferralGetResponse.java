package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.Customer;
import viteezy.domain.pricing.ReferralDiscount;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class CustomerReferralGetResponse {

    private final String referralCode;
    private final String firstName;
    private final Integer discountAmount;
    private final BigDecimal minimumPrice;

    @JsonCreator
    public CustomerReferralGetResponse(
            @JsonProperty(value = "referralCode", required = true) String referralCode,
            @JsonProperty(value = "firstName", required = true) String firstName,
            @JsonProperty(value = "discountAmount", required = true) Integer discountAmount,
            @JsonProperty(value = "minimumPrice", required = true) BigDecimal minimumPrice) {
        this.referralCode = referralCode;
        this.firstName = firstName;
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public static CustomerReferralGetResponse from(Customer that, ReferralDiscount referralDiscount) {
        return new CustomerReferralGetResponse(that.getReferralCode(), that.getFirstName(),
                referralDiscount.getDiscountAmount(), referralDiscount.getMinimumPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerReferralGetResponse that = (CustomerReferralGetResponse) o;
        return Objects.equals(referralCode, that.referralCode) &&
                Objects.equals(discountAmount, that.discountAmount) &&
                Objects.equals(minimumPrice, that.minimumPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(referralCode, discountAmount, minimumPrice);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CustomerReferralGetResponse.class.getSimpleName() + "[", "]")
                .add("referralCode='" + referralCode + "'")
                .add("discountAmount=" + discountAmount)
                .add("minimumPrice=" + minimumPrice)
                .toString();
    }
}