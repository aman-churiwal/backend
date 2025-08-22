package viteezy.service.fulfilment;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.fulfilment.PharmacistOrderLine;

import java.util.List;

public interface CSVWriterService {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<List<PharmacistOrderLine>> createPharmacistRequest();
}
