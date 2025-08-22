package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class StopPaymentPlanPostRequest {

    private final String stopReason;

    @JsonCreator
    public StopPaymentPlanPostRequest(
            @JsonProperty(value = "stopReason", required = true) String stopReason
    ) {
        this.stopReason = stopReason;
    }

    public String getStopReason() {
        return stopReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StopPaymentPlanPostRequest that = (StopPaymentPlanPostRequest) o;
        return Objects.equals(stopReason, that.stopReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopReason);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StopPaymentPlanPostRequest.class.getSimpleName() + "[", "]")
                .add("stopReason='" + stopReason + "'")
                .toString();
    }
}
