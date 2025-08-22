package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class PricingPostRequest {

    private final Optional<List<Long>> ingredientIds;
    private final String couponCode;
    private final Integer monthsSubscribed;
    private final Boolean isSubscription;
    @JsonCreator
    public PricingPostRequest(
            @JsonProperty(value = "ingredientIds", required = false) Optional<List<Long>> ingredientIds,
            @JsonProperty(value = "couponCode", required = false) String couponCode,
            @JsonProperty(value = "monthsSubscribed", defaultValue = "1") Integer monthsSubscribed,
            @JsonProperty(value = "isSubscription", defaultValue = "true") Boolean isSubscription
    ) {
        this.ingredientIds = ingredientIds;
        this.couponCode = couponCode;
        this.monthsSubscribed = monthsSubscribed;
        this.isSubscription = isSubscription;
    }

    public Optional<List<Long>> getIngredientIds() {
        return ingredientIds;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public Integer getMonthsSubscribed() {
        return monthsSubscribed;
    }

    public Boolean getSubscription() {
        return isSubscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricingPostRequest that = (PricingPostRequest) o;
        return Objects.equals(ingredientIds, that.ingredientIds) && Objects.equals(couponCode, that.couponCode) && Objects.equals(monthsSubscribed, that.monthsSubscribed) && Objects.equals(isSubscription, that.isSubscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientIds, couponCode, monthsSubscribed, isSubscription);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PricingPostRequest.class.getSimpleName() + "[", "]")
                .add("ingredientIds=" + ingredientIds)
                .add("couponCode='" + couponCode + "'")
                .add("monthsSubscribed=" + monthsSubscribed)
                .add("isSubscription=" + isSubscription)
                .toString();
    }
}
