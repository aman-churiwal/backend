package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentsGetResponse {
    private final BigDecimal amount;
    private final LocalDateTime creationDate;
    private final PaymentStatus status;

    @JsonCreator
    public PaymentsGetResponse(
            @JsonProperty(value = "amount", required = true) BigDecimal amount,
            @JsonProperty(value = "creationDate", required = true) LocalDateTime creationDate,
            @JsonProperty(value = "status", required = true) PaymentStatus status
    ) {
        this.amount = amount;
        this.creationDate = creationDate;
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public static PaymentsGetResponse from(Payment payment) {
        return new PaymentsGetResponse(payment.getAmount(), payment.getCreationDate(), payment.getStatus());
    }
}
