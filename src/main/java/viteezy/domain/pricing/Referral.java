package viteezy.domain.pricing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Referral {

    private final Long id;
    private final Long from;
    private final Long to;
    private final BigDecimal amount;
    private final ReferralStatus status;
    private final LocalDateTime creationDate;
    private final LocalDateTime lastModified;

    public Referral(Long id, Long from, Long to, BigDecimal amount, ReferralStatus status,
                    LocalDateTime creationDate, LocalDateTime lastModified) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.status = status;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
    }

    public Long getId() {
        return id;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ReferralStatus getStatus() {
        return status;
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
        if (o == null || getClass() != o.getClass()) return false;
        Referral referral = (Referral) o;
        return Objects.equals(id, referral.id) &&
                Objects.equals(from, referral.from) &&
                Objects.equals(to, referral.to) &&
                Objects.equals(amount, referral.amount) &&
                status == referral.status &&
                Objects.equals(creationDate, referral.creationDate) &&
                Objects.equals(lastModified, referral.lastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, from, to, amount, status, creationDate, lastModified);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Referral.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("from=" + from)
                .add("to=" + to)
                .add("amount=" + amount)
                .add("status=" + status)
                .add("creationDate=" + creationDate)
                .add("lastModified=" + lastModified)
                .toString();
    }
}
