package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Quiz {

    private final Long id;
    private final UUID externalReference;
    private final LocalDateTime creationDate;
    private final LocalDateTime lastModified;
    private final Long customerId;
    private final String utmContent;

    public Quiz(Long id, UUID externalReference, LocalDateTime creationDate, LocalDateTime lastModified, Long customerId, String utmContent) {
        this.id = id;
        this.externalReference = externalReference;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
        this.customerId = customerId;
        this.utmContent = utmContent;
    }

    public Long getId() {
        return id;
    }

    public UUID getExternalReference() {
        return externalReference;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getUtmContent() {
        return utmContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quiz quiz)) return false;
        return Objects.equals(id, quiz.id) && Objects.equals(externalReference, quiz.externalReference) && Objects.equals(creationDate, quiz.creationDate) && Objects.equals(lastModified, quiz.lastModified) && Objects.equals(customerId, quiz.customerId) && Objects.equals(utmContent, quiz.utmContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalReference, creationDate, lastModified, customerId, utmContent);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Quiz.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("externalReference=" + externalReference)
                .add("creationDate=" + creationDate)
                .add("lastModified=" + lastModified)
                .add("customerId=" + customerId)
                .add("utmContent='" + utmContent + "'")
                .toString();
    }
}
