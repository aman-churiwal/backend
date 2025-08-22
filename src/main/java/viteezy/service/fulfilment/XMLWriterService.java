package viteezy.service.fulfilment;

import io.vavr.control.Try;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface XMLWriterService {

    Try<List<File>> createDeliveryOrderMessage(UUID blendExternalReference);

    Try<List<File>> createDeliveryOrderMessages();
}
