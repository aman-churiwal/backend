package viteezy.domain.pricing;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class Pricing {

    private final BigDecimal basePrice;
    private final BigDecimal noSubscriptionFee;
    private final BigDecimal threeMonthPlanDiscount;
    private final BigDecimal couponDiscount;
    private final BigDecimal referralDiscount;
    private final BigDecimal shippingCost;
    private final BigDecimal firstAmount;
    private final BigDecimal recurringAmount;


    public Pricing(BigDecimal basePrice, BigDecimal noSubscriptionFee, BigDecimal threeMonthPlanDiscount,
                   BigDecimal couponDiscount, BigDecimal referralDiscount, BigDecimal shippingCost,
                   BigDecimal firstAmount, BigDecimal recurringAmount) {
        this.basePrice = basePrice;
        this.noSubscriptionFee = noSubscriptionFee;
        this.threeMonthPlanDiscount = threeMonthPlanDiscount;
        this.couponDiscount = couponDiscount;
        this.referralDiscount = referralDiscount;
        this.shippingCost = shippingCost;
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

    public BigDecimal getFirstAmount() {
        return firstAmount;
    }

    public BigDecimal getRecurringAmount() {
        return recurringAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pricing pricing = (Pricing) o;
        return Objects.equals(basePrice, pricing.basePrice) && Objects.equals(noSubscriptionFee, pricing.noSubscriptionFee) && Objects.equals(threeMonthPlanDiscount, pricing.threeMonthPlanDiscount) && Objects.equals(couponDiscount, pricing.couponDiscount) && Objects.equals(referralDiscount, pricing.referralDiscount) && Objects.equals(shippingCost, pricing.shippingCost) && Objects.equals(firstAmount, pricing.firstAmount) && Objects.equals(recurringAmount, pricing.recurringAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(basePrice, noSubscriptionFee, threeMonthPlanDiscount, couponDiscount, referralDiscount, shippingCost, firstAmount, recurringAmount);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Pricing.class.getSimpleName() + "[", "]")
                .add("basePrice=" + basePrice)
                .add("noSubscriptionFee=" + noSubscriptionFee)
                .add("threeMonthPlanDiscount=" + threeMonthPlanDiscount)
                .add("couponDiscount=" + couponDiscount)
                .add("referralDiscount=" + referralDiscount)
                .add("shippingCost=" + shippingCost)
                .add("firstAmount=" + firstAmount)
                .add("recurringAmount=" + recurringAmount)
                .toString();
    }
}
