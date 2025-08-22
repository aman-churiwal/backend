package viteezy.domain.postnl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class ShipmentStatus {
    private final LocalDateTime timeStamp;
    private final Integer statusCode;
    private final String statusDescription;
    private final String phaseCode;
    private final String phaseDescription;

    @JsonCreator
    public ShipmentStatus(@JsonProperty("Timestamp") LocalDateTime timeStamp,
                          @JsonProperty("StatusCode") Integer statusCode,
                          @JsonProperty("StatusDescription") String statusDescription,
                          @JsonProperty("PhaseCode") String phaseCode,
                          @JsonProperty("PhaseDescription") String phaseDescription) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.phaseCode = phaseCode;
        this.phaseDescription = phaseDescription;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public String getPhaseCode() {
        return phaseCode;
    }

    public String getPhaseDescription() {
        return phaseDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipmentStatus shipment = (ShipmentStatus) o;
        return Objects.equals(timeStamp, shipment.timeStamp) && Objects.equals(statusCode, shipment.statusCode) && Objects.equals(statusDescription, shipment.statusDescription) && Objects.equals(phaseCode, shipment.phaseCode) && Objects.equals(phaseDescription, shipment.phaseDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStamp, statusCode, statusDescription, phaseCode, phaseDescription);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ShipmentStatus.class.getSimpleName() + "[", "]")
                .add("timeStamp=" + timeStamp)
                .add("statusCode=" + statusCode)
                .add("statusDescription='" + statusDescription + "'")
                .add("phaseCode='" + phaseCode + "'")
                .add("phaseDescription='" + phaseDescription + "'")
                .toString();
    }
}
