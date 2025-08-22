package viteezy.domain.google;

public class MeasurementProtocolEvent {
    private final String name;
    private final MeasurementProtocolEventParam params;

    public MeasurementProtocolEvent(String name, MeasurementProtocolEventParam params) {
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public MeasurementProtocolEventParam getParams() {
        return params;
    }
}
