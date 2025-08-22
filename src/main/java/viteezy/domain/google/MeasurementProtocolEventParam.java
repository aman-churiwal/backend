package viteezy.domain.google;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class MeasurementProtocolEventParam {
    private final UUID transactionId;
    private final String currency;
    private final BigDecimal value;
    private final BigDecimal tax;
    private final List<MeasurementProtocolEventItem> items;

    private final int debugMode;
    private final String sessionId;

    public MeasurementProtocolEventParam(UUID transactionId, String currency, BigDecimal value, BigDecimal tax, List<MeasurementProtocolEventItem> items, int debugMode, String sessionId) {
        this.transactionId = transactionId;
        this.currency = currency;
        this.value = value;
        this.tax = tax;
        this.items = items;
        this.debugMode = debugMode;
        this.sessionId = sessionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public List<MeasurementProtocolEventItem> getItems() {
        return items;
    }

    public int getDebugMode() {
        return debugMode;
    }

    public String getSessionId() {
        return sessionId;
    }
}
