package viteezy.domain.payment;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentPlanStatus {
    PENDING, PENDING_SINGLE_BUY, ACTIVE, PAID_SINGLE_BUY, STOPPED, CANCELED, SUSPENDED, COMPLETED;

    @JsonValue
    public String getJsonValue() {
        return name().toLowerCase().replace('_', '-');
    }
}