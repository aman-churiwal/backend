package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.EmailAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class EmailAnswerGetResponse {

    private final Long id;
    private final String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;
    private final Boolean optIn;

    @JsonCreator
    public EmailAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp,
            @JsonProperty(value = "optIn", required = true) Boolean optIn
    ) {
        this.id = id;
        this.email = email;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
        this.optIn = optIn;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    public Boolean getOptIn() {
        return optIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAnswerGetResponse that = (EmailAnswerGetResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                Objects.equals(creationTimestamp, that.creationTimestamp) &&
                Objects.equals(modificationTimestamp, that.modificationTimestamp) &&
                Objects.equals(optIn, that.optIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, creationTimestamp, modificationTimestamp, optIn);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EmailAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("email='" + email + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .add("optIn=" + optIn)
                .toString();
    }

    public static EmailAnswerGetResponse from(EmailAnswer that) {
        return new EmailAnswerGetResponse(that.getId(), that.getEmail(), that.getCreationTimestamp(), that.getModificationTimestamp(), that.getOptIn());
    }

}
