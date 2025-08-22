package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.pricing.Coupon;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class CouponGetResponse {

    private final String couponCode;
    private final BigDecimal amount;
    private final BigDecimal minimumAmount;
    private final BigDecimal maximumAmount;
    private final Boolean percentage;
    private final Integer recurringMonths;
    private final String recurringTerms;
    private final Boolean isRecurring;
    private final Long ingredientId;

    @JsonCreator
    public CouponGetResponse(
            @JsonProperty(value = "couponCode", required = true) String couponCode,
            @JsonProperty(value = "amount", required = true) BigDecimal amount,
            @JsonProperty(value = "minimumAmount", required = true) BigDecimal minimumAmount,
            @JsonProperty(value = "maximumAmount", required = true) BigDecimal maximumAmount,
            @JsonProperty(value = "percentage", required = true) Boolean percentage,
            @JsonProperty(value = "recurringMonths", required = true) Integer recurringMonths,
            @JsonProperty(value = "recurringTerms", required = true) String recurringTerms,
            @JsonProperty(value = "recurring", required = true) Boolean isRecurring,
            @JsonProperty(value = "ingredientId", required = true) Long ingredientId) {
        this.couponCode = couponCode;
        this.amount = amount;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.percentage = percentage;
        this.recurringMonths = recurringMonths;
        this.recurringTerms = recurringTerms;
        this.isRecurring = isRecurring;
        this.ingredientId = ingredientId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    public BigDecimal getMaximumAmount() {
        return maximumAmount;
    }

    public Boolean getPercentage() {
        return percentage;
    }

    public Integer getRecurringMonths() {
        return recurringMonths;
    }

    public String getRecurringTerms() {
        return recurringTerms;
    }

    public Boolean getRecurring() {
        return isRecurring;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public static CouponGetResponse from(Coupon that) {
        return new CouponGetResponse(that.getCouponCode(), that.getAmount(), that.getMinimumAmount(), that.getMaximumAmount(), that.getPercentage(), that.getRecurringMonths().orElse(null), that.getRecurringTerms().orElse(null), that.getRecurring(), that.getIngredientId().orElse(null));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouponGetResponse that = (CouponGetResponse) o;
        return Objects.equals(couponCode, that.couponCode) && Objects.equals(amount, that.amount) && Objects.equals(minimumAmount, that.minimumAmount) && Objects.equals(maximumAmount, that.maximumAmount) && Objects.equals(percentage, that.percentage) && Objects.equals(recurringMonths, that.recurringMonths) && Objects.equals(recurringTerms, that.recurringTerms) && Objects.equals(isRecurring, that.isRecurring) && Objects.equals(ingredientId, that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponCode, amount, minimumAmount, maximumAmount, percentage, recurringMonths, recurringTerms, isRecurring, ingredientId);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CouponGetResponse.class.getSimpleName() + "[", "]")
                .add("couponCode='" + couponCode + "'")
                .add("amount=" + amount)
                .add("minimumAmount=" + minimumAmount)
                .add("maximumAmount=" + maximumAmount)
                .add("percentage=" + percentage)
                .add("recurringMonths=" + recurringMonths)
                .add("recurringTerms='" + recurringTerms + "'")
                .add("isRecurring=" + isRecurring)
                .add("ingredientId=" + ingredientId)
                .toString();
    }
}
