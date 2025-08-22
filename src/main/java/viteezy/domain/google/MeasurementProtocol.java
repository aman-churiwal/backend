package viteezy.domain.google;

import java.util.List;

public class MeasurementProtocol {
    private final String clientId;
    private final long timestampMicros;
    private final List<MeasurementProtocolEvent> events;

    public MeasurementProtocol(String clientId, long timestampMicros, List<MeasurementProtocolEvent> events) {
        this.clientId = clientId;
        this.timestampMicros = timestampMicros;
        this.events = events;
    }

    public String getClientId() {
        return clientId;
    }

    public long getTimestampMicros() {
        return timestampMicros;
    }

    public List<MeasurementProtocolEvent> getEvents() {
        return events;
    }
}
