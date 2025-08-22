package viteezy.service.fulfilment;

import io.vavr.control.Try;

public interface XMLReaderService {

    Try<Void> updateDeliveryOrderMessages();
}
