package viteezy.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Logging {

    private final Long id;
    private final Long customerId;
    private final LoggingEvent event;
    private final String info;
    private final LocalDateTime creationTimestamp;

    public Logging(Long id, Long customerId, LoggingEvent event, String info, LocalDateTime creationTimestamp) {
        this.id = id;
        this.customerId = customerId;
        this.event = event;
        this.info = info;
        this.creationTimestamp = creationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public LoggingEvent getEvent() {
        return event;
    }

    public String getInfo() {
        return info;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Logging logging = (Logging) o;
        return Objects.equals(id, logging.id) && Objects.equals(customerId, logging.customerId) && event == logging.event && Objects.equals(info, logging.info) && Objects.equals(creationTimestamp, logging.creationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, event, info, creationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Logging.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("customerId=" + customerId)
                .add("event=" + event)
                .add("info='" + info + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .toString();
    }
}
