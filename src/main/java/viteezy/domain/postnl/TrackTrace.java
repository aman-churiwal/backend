package viteezy.domain.postnl;

import java.net.URI;

public class TrackTrace {

    private final URI trackTraceLink;

    public TrackTrace(URI trackTraceLink) {
        this.trackTraceLink = trackTraceLink;
    }

    public URI getTrackTraceLink() {
        return trackTraceLink;
    }
}
