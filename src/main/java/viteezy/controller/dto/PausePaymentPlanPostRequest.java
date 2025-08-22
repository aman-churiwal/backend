package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

public class PausePaymentPlanPostRequest {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private final LocalDate deliveryDate;

    @JsonCreator
    public PausePaymentPlanPostRequest(
            @JsonProperty(value = "deliveryDate", required = true) LocalDate deliveryDate
    ) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PausePaymentPlanPostRequest that = (PausePaymentPlanPostRequest) o;
        return Objects.equals(deliveryDate, that.deliveryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryDate);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PausePaymentPlanPostRequest.class.getSimpleName() + "[", "]")
                .add("deliveryDate=" + deliveryDate)
                .toString();
    }
}
