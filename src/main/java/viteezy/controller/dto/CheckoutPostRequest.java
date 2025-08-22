package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class CheckoutPostRequest {

    private final String couponCode;
    private final Integer monthsSubscribed;
    private final Boolean isSubscription;
    private final String method;
    private final String fbclid;

    @JsonCreator
    public CheckoutPostRequest(
            @JsonProperty(value = "couponCode", required = false) String couponCode,
            @JsonProperty(value = "monthsSubscribed", defaultValue = "1") Integer monthsSubscribed,
            @JsonProperty(value = "isSubscription", defaultValue = "true") Boolean isSubscription,
            @JsonProperty(value = "method", required = false) String method,
            @JsonProperty(value = "fbclid", required = false) String fbclid
    ) {
        this.couponCode = couponCode;
        this.monthsSubscribed = monthsSubscribed;
        this.isSubscription = isSubscription;
        this.method = method;
        this.fbclid = fbclid;
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

    public String getMethod() {
        return method;
    }

    public String getFbclid() {
        return fbclid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckoutPostRequest that)) return false;
        return Objects.equals(couponCode, that.couponCode) && Objects.equals(monthsSubscribed, that.monthsSubscribed) && Objects.equals(isSubscription, that.isSubscription) && Objects.equals(method, that.method) && Objects.equals(fbclid, that.fbclid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponCode, monthsSubscribed, isSubscription, method, fbclid);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CheckoutPostRequest.class.getSimpleName() + "[", "]")
                .add("couponCode='" + couponCode + "'")
                .add("monthsSubscribed=" + monthsSubscribed)
                .add("isSubscription=" + isSubscription)
                .add("method='" + method + "'")
                .add("fbclid='" + fbclid + "'")
                .toString();
    }
}