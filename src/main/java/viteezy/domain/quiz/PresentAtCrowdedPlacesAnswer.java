package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class PresentAtCrowdedPlacesAnswer {

    private final Long id;
    private final Long quizId;
    private final Long presentAtCrowdedPlacesId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public PresentAtCrowdedPlacesAnswer(
            Long id, Long quizId, Long presentAtCrowdedPlacesId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.presentAtCrowdedPlacesId = presentAtCrowdedPlacesId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getPresentAtCrowdedPlacesId() {
        return presentAtCrowdedPlacesId;
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
        PresentAtCrowdedPlacesAnswer presentAtCrowdedPlaces = (PresentAtCrowdedPlacesAnswer) o;
        return Objects.equals(id, presentAtCrowdedPlaces.id) &&
                Objects.equals(quizId, presentAtCrowdedPlaces.quizId) &&
                Objects.equals(presentAtCrowdedPlacesId, presentAtCrowdedPlaces.presentAtCrowdedPlacesId) &&
                Objects.equals(creationTimestamp, presentAtCrowdedPlaces.creationTimestamp) &&
                Objects.equals(modificationTimestamp, presentAtCrowdedPlaces.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, presentAtCrowdedPlacesId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("presentAtCrowdedPlacesId='" + presentAtCrowdedPlacesId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
