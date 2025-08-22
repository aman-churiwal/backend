package viteezy.domain.pricing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Incentive {

    private final Long id;
    private final Long customerId;
    private final BigDecimal amount;
    private final IncentiveStatus status;
    private final IncentiveType type;
    private final LocalDateTime creationDate;
    private final LocalDateTime lastModified;

    public Incentive(Long id, Long customerId, BigDecimal amount, IncentiveStatus status,
                     IncentiveType type, LocalDateTime creationDate, LocalDateTime lastModified) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.status = status;
        this.type = type;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public IncentiveStatus getStatus() {
        return status;
    }

    public IncentiveType getType() {
        return type;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Incentive)) return false;
        Incentive incentive = (Incentive) o;
        return Objects.equals(id, incentive.id) && Objects.equals(customerId, incentive.customerId) && Objects.equals(amount, incentive.amount) && status == incentive.status && type == incentive.type && Objects.equals(creationDate, incentive.creationDate) && Objects.equals(lastModified, incentive.lastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, amount, status, type, creationDate, lastModified);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Incentive.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("customerId=" + customerId)
                .add("amount=" + amount)
                .add("status=" + status)
                .add("type=" + type)
                .add("creationDate=" + creationDate)
                .add("lastModified=" + lastModified)
                .toString();
    }
}
