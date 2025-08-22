package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.DateOfBirthAnswer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DateOfBirthAnswerGetResponse {

    private final Long id;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private final LocalDate date;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public DateOfBirthAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "date", required = true) LocalDate date,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.date = date;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateOfBirthAnswerGetResponse dateOfBirthAnswerGetResponse = (DateOfBirthAnswerGetResponse) o;
        return Objects.equals(id, dateOfBirthAnswerGetResponse.id) &&
                Objects.equals(date, dateOfBirthAnswerGetResponse.date) &&
                Objects.equals(creationTimestamp, dateOfBirthAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, dateOfBirthAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DateOfBirthAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("dateOfBirthId='" + date + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static DateOfBirthAnswerGetResponse from(DateOfBirthAnswer that) {
        return new DateOfBirthAnswerGetResponse(that.getId(), that.getDate(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
