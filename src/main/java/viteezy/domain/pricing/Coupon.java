package viteezy.domain.pricing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class Coupon {
    private final Long id;
    private final String couponCode;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final BigDecimal amount;
    private final BigDecimal minimumAmount;
    private final BigDecimal maximumAmount;
    private final Boolean percentage;
    private final Integer maxUses;
    private final Integer used;
    private final Optional<Integer> recurringMonths;
    private final Optional<String> recurringTerms;
    private final Boolean isRecurring;
    private final Optional<Long> ingredientId;
    private final LocalDateTime creationDate;
    private final Boolean isActive;

    public Coupon(Long id, String couponCode, LocalDateTime startDate, LocalDateTime endDate, BigDecimal amount,
                  BigDecimal minimumAmount, BigDecimal maximumAmount, Boolean percentage, Integer maxUses,
                  Integer used, Optional<Integer> recurringMonths, Optional<String> recurringTerms, Boolean isRecurring,
                  Optional<Long> ingredientId, LocalDateTime creationDate, Boolean isActive) {
        this.id = id;
        this.couponCode = couponCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.percentage = percentage;
        this.maxUses = maxUses;
        this.used = used;
        this.recurringMonths = recurringMonths;
        this.recurringTerms = recurringTerms;
        this.isRecurring = isRecurring;
        this.ingredientId = ingredientId;
        this.creationDate = creationDate;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
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

    public Integer getMaxUses() {
        return maxUses;
    }

    public Integer getUsed() {
        return used;
    }

    public Optional<Integer> getRecurringMonths() {
        return recurringMonths;
    }

    public Optional<String> getRecurringTerms() {
        return recurringTerms;
    }

    public Boolean getRecurring() {
        return isRecurring;
    }

    public Optional<Long> getIngredientId() {
        return ingredientId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public  Boolean getIsActive() { return isActive; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon that = (Coupon) o;
        return Objects.equals(id, that.id) && Objects.equals(couponCode, that.couponCode) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(amount, that.amount) && Objects.equals(minimumAmount, that.minimumAmount) && Objects.equals(maximumAmount, that.maximumAmount) && Objects.equals(percentage, that.percentage) && Objects.equals(maxUses, that.maxUses) && Objects.equals(used, that.used) && Objects.equals(recurringMonths, that.recurringMonths) && Objects.equals(isRecurring, that.isRecurring) && Objects.equals(ingredientId, that.ingredientId) && Objects.equals(creationDate, that.creationDate) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, couponCode, startDate, endDate, amount, minimumAmount, maximumAmount, percentage, maxUses, used, recurringMonths, isRecurring, ingredientId, creationDate, isActive);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Coupon.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("couponCode='" + couponCode + "'")
                .add("startDate=" + startDate)
                .add("endDate=" + endDate)
                .add("amount=" + amount)
                .add("minimumAmount=" + minimumAmount)
                .add("maximumAmount=" + maximumAmount)
                .add("percentage=" + percentage)
                .add("maxUses=" + maxUses)
                .add("used=" + used)
                .add("recurringMonths=" + recurringMonths)
                .add("isRecurring=" + isRecurring)
                .add("ingredientId=" + ingredientId)
                .add("creationDate=" + creationDate)
                .add("isActive=" + isActive)
                .toString();
    }
}
