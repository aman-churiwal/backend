package viteezy.service.fulfilment;

import io.vavr.control.Try;
import viteezy.domain.fulfilment.PharmacistOrder;
import viteezy.domain.fulfilment.PharmacistOrderStatus;

import java.util.List;

public interface PharmacistOrderService {
    Try<PharmacistOrder> find(Long id);

    Try<PharmacistOrder> find(String fileName);

    Try<PharmacistOrder> findByLastInsertedId();

    Try<List<PharmacistOrder>> findByStatus(PharmacistOrderStatus pharmacistOrderStatus);

    Try<PharmacistOrder> save(PharmacistOrder pharmacistOrder);

}
