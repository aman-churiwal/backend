package viteezy.controller.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.payment.PaymentStatus;

import java.util.Objects;
import java.util.StringJoiner;

public class PaymentPatchRequest {
    private final Long id;
    private final PaymentStatus status;

    @JsonCreator
    public PaymentPatchRequest(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "status", required = true) PaymentStatus status
    ) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentPatchRequest that = (PaymentPatchRequest) o;
        return Objects.equals(id, that.id) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PaymentPatchRequest.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("status=" + status)
                .toString();
    }
}
