package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class CheckoutPatchRequest {

    private final String couponCode;
    private final Integer monthsSubscribed;

    @JsonCreator
    public CheckoutPatchRequest(
            @JsonProperty(value = "couponCode", required = false) String couponCode,
            @JsonProperty(value = "monthsSubscribed", defaultValue = "1") Integer monthsSubscribed) {
        this.couponCode = couponCode;
        this.monthsSubscribed = monthsSubscribed;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public Integer getMonthsSubscribed() {
        return monthsSubscribed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckoutPatchRequest that = (CheckoutPatchRequest) o;
        return Objects.equals(couponCode, that.couponCode) &&
                Objects.equals(monthsSubscribed, that.monthsSubscribed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponCode, monthsSubscribed);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CheckoutPatchRequest.class.getSimpleName() + "[", "]")
                .add("couponCode='" + couponCode + "'")
                .add("monthsSubscribed=" + monthsSubscribed)
                .toString();
    }
}