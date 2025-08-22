package viteezy.domain.klaviyo.profile;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileProperties {
    private final Integer age;
    private final String primaryGoal;
    private final String secondaryGoal;
    private final String dietType;
    private final String allergyType;
    private final String blendExternalReference;
    private final String ingredients;
    private final String paymentPlanStatus;
    private final String stopReason;
    private final Integer recurringMonths;
    private final String paymentDate;
    private final String referralCode;

    public ProfileProperties(@JsonProperty("age") Integer age,
                             @JsonProperty("primary_goal") String primaryGoal,
                             @JsonProperty("secondary_goal") String secondaryGoal,
                             @JsonProperty("diet_type") String dietType,
                             @JsonProperty("allergy_type") String allergyType,
                             @JsonProperty("blend_external_reference") String blendExternalReference,
                             @JsonProperty("ingredients") String ingredients,
                             @JsonProperty("status") String paymentPlanStatus,
                             @JsonProperty("stop_reason") String stopReason,
                             @JsonProperty("recurring_months") Integer recurringMonths,
                             @JsonProperty("payment_date") String paymentDate,
                             @JsonProperty("referral_code") String referralCode) {
        this.age = age;
        this.primaryGoal = primaryGoal;
        this.secondaryGoal = secondaryGoal;
        this.dietType = dietType;
        this.allergyType = allergyType;
        this.blendExternalReference = blendExternalReference;
        this.ingredients = ingredients;
        this.paymentPlanStatus = paymentPlanStatus;
        this.stopReason = stopReason;
        this.recurringMonths = recurringMonths;
        this.paymentDate = paymentDate;
        this.referralCode = referralCode;
    }

    public Integer getAge() {
        return age;
    }

    @JsonGetter(value = "primary_goal")
    public String getPrimaryGoal() {
        return primaryGoal;
    }

    @JsonGetter(value = "secondary_goal")
    public String getSecondaryGoal() {
        return secondaryGoal;
    }

    @JsonGetter(value = "diet_type")
    public String getDietType() {
        return dietType;
    }

    @JsonGetter(value = "allergy_type")
    public String getAllergyType() {
        return allergyType;
    }

    @JsonGetter(value = "blend_external_reference")
    public String getBlendExternalReference() {
        return blendExternalReference;
    }

    @JsonGetter(value = "ingredients")
    public String getIngredients() {
        return ingredients;
    }

    @JsonGetter(value = "status")
    public String getPaymentPlanStatus() {
        return paymentPlanStatus;
    }

    @JsonGetter(value = "stop_reason")
    public String getStopReason() {
        return stopReason;
    }

    @JsonGetter(value = "recurring_months")
    public Integer getRecurringMonths() {
        return recurringMonths;
    }

    @JsonGetter(value = "payment_date")
    public String getPaymentDate() {
        return paymentDate;
    }

    @JsonGetter(value = "referral_code")
    public String getReferralCode() {
        return referralCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileProperties that = (ProfileProperties) o;
        return Objects.equals(age, that.age) && Objects.equals(primaryGoal, that.primaryGoal) && Objects.equals(secondaryGoal, that.secondaryGoal) && Objects.equals(dietType, that.dietType) && Objects.equals(allergyType, that.allergyType) && Objects.equals(blendExternalReference, that.blendExternalReference) && Objects.equals(ingredients, that.ingredients) && Objects.equals(paymentPlanStatus, that.paymentPlanStatus) && Objects.equals(stopReason, that.stopReason) && Objects.equals(recurringMonths, that.recurringMonths) && Objects.equals(paymentDate, that.paymentDate) && Objects.equals(referralCode, that.referralCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, primaryGoal, secondaryGoal, dietType, allergyType, blendExternalReference, ingredients, paymentPlanStatus, stopReason, recurringMonths, paymentDate, referralCode);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProfileProperties.class.getSimpleName() + "[", "]")
                .add("age=" + age)
                .add("primaryGoal='" + primaryGoal + "'")
                .add("secondaryGoal='" + secondaryGoal + "'")
                .add("dietType='" + dietType + "'")
                .add("allergyType='" + allergyType + "'")
                .add("blendExternalReference='" + blendExternalReference + "'")
                .add("ingredients='" + ingredients + "'")
                .add("paymentPlanStatus='" + paymentPlanStatus + "'")
                .add("stopReason='" + stopReason + "'")
                .add("recurringMonths=" + recurringMonths)
                .add("paymentDate='" + paymentDate + "'")
                .add("referralCode='" + referralCode + "'")
                .toString();
    }
}
