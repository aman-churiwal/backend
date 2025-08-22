package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.pricing.Pricing;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class PricingGetResponse {

    private final BigDecimal basePrice;
    private final BigDecimal noSubscriptionFee;
    private final BigDecimal threeMonthPlanDiscount;
    private final BigDecimal couponDiscount;
    private final BigDecimal referralDiscount;
    private final BigDecimal shippingCost;
    private final BigDecimal totalPrice;
    private final BigDecimal firstAmount;
    private final BigDecimal recurringAmount;

    @JsonCreator
    public PricingGetResponse(
            @JsonProperty(value = "basePrice", required = true) BigDecimal basePrice,
            @JsonProperty(value = "noSubscriptionFee", required = true) BigDecimal noSubscriptionFee,
            @JsonProperty(value = "threeMonthPlanDiscount", required = true) BigDecimal threeMonthPlanDiscount,
            @JsonProperty(value = "couponDiscount", required = true) BigDecimal couponDiscount,
            @JsonProperty(value = "referralDiscount", required = true) BigDecimal referralDiscount,
            @JsonProperty(value = "shippingCost", required = true) BigDecimal shippingCost,
            @JsonProperty(value = "totalPrice", required = true) BigDecimal totalPrice,
            @JsonProperty(value = "firstAmount", required = true) BigDecimal firstAmount,
            @JsonProperty(value = "recurringAmount", required = true) BigDecimal recurringAmount
    ) {
        this.basePrice = basePrice;
        this.noSubscriptionFee = noSubscriptionFee;
        this.threeMonthPlanDiscount = threeMonthPlanDiscount;
        this.couponDiscount = couponDiscount;
        this.referralDiscount = referralDiscount;
        this.shippingCost = shippingCost;
        this.totalPrice = totalPrice;
        this.firstAmount = firstAmount;
        this.recurringAmount = recurringAmount;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public BigDecimal getNoSubscriptionFee() {
        return noSubscriptionFee;
    }

    public BigDecimal getThreeMonthPlanDiscount() {
        return threeMonthPlanDiscount;
    }

    public BigDecimal getCouponDiscount() {
        return couponDiscount;
    }

    public BigDecimal getReferralDiscount() {
        return referralDiscount;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getFirstAmount() {
        return firstAmount;
    }

    public BigDecimal getRecurringAmount() {
        return recurringAmount;
    }

    public static PricingGetResponse from(Pricing pricing) {
        return new PricingGetResponse(
                pricing.getBasePrice(), pricing.getNoSubscriptionFee(), pricing.getThreeMonthPlanDiscount(),
                pricing.getCouponDiscount(), pricing.getReferralDiscount(), pricing.getShippingCost(),
                pricing.getFirstAmount(), pricing.getFirstAmount(), pricing.getRecurringAmount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricingGetResponse that = (PricingGetResponse) o;
        return Objects.equals(basePrice, that.basePrice) && Objects.equals(noSubscriptionFee, that.noSubscriptionFee) && Objects.equals(threeMonthPlanDiscount, that.threeMonthPlanDiscount) && Objects.equals(couponDiscount, that.couponDiscount) && Objects.equals(referralDiscount, that.referralDiscount) && Objects.equals(shippingCost, that.shippingCost) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(firstAmount, that.firstAmount) && Objects.equals(recurringAmount, that.recurringAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(basePrice, noSubscriptionFee, threeMonthPlanDiscount, couponDiscount, referralDiscount, shippingCost, totalPrice, firstAmount, recurringAmount);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PricingGetResponse.class.getSimpleName() + "[", "]")
                .add("basePrice=" + basePrice)
                .add("noSubscriptionFee=" + noSubscriptionFee)
                .add("threeMonthPlanDiscount=" + threeMonthPlanDiscount)
                .add("couponDiscount=" + couponDiscount)
                .add("referralDiscount=" + referralDiscount)
                .add("shippingCost=" + shippingCost)
                .add("totalPrice=" + totalPrice)
                .add("firstAmount=" + firstAmount)
                .add("recurringAmount=" + recurringAmount)
                .toString();
    }
}
