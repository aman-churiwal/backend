package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.GenderAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class GenderAnswerGetResponse {

    private final Long id;
    private final Long genderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public GenderAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "genderId", required = true) Long genderId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.genderId = genderId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getGenderId() {
        return genderId;
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
        GenderAnswerGetResponse genderAnswerGetResponse = (GenderAnswerGetResponse) o;
        return Objects.equals(id, genderAnswerGetResponse.id) &&
                Objects.equals(genderId, genderAnswerGetResponse.genderId) &&
                Objects.equals(creationTimestamp, genderAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, genderAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, genderId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenderAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("genderId='" + genderId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static GenderAnswerGetResponse from(GenderAnswer that) {
        return new GenderAnswerGetResponse(that.getId(), that.getGenderId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
