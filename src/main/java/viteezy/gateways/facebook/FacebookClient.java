package viteezy.gateways.facebook;

import com.facebook.ads.sdk.serverside.Event;
import io.vavr.control.Try;

public interface FacebookClient {
    Try<Void> sendEventRequest(Event event);
}
