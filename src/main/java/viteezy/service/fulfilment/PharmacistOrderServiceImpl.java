package viteezy.service.fulfilment;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.PharmacistOrderRepository;
import viteezy.domain.fulfilment.PharmacistOrder;
import viteezy.domain.fulfilment.PharmacistOrderStatus;

import java.util.List;

public class PharmacistOrderServiceImpl implements PharmacistOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PharmacistOrderService.class);

    private final PharmacistOrderRepository pharmacistOrderRepository;

    protected PharmacistOrderServiceImpl(PharmacistOrderRepository pharmacistOrderRepository) {
        this.pharmacistOrderRepository = pharmacistOrderRepository;
    }

    @Override
    public Try<PharmacistOrder> find(Long id) {
        return pharmacistOrderRepository.find(id);
    }

    @Override
    public Try<PharmacistOrder> find(String fileName) {
        return pharmacistOrderRepository.find(fileName);
    }

    @Override
    public Try<PharmacistOrder> findByLastInsertedId() {
        return pharmacistOrderRepository.findByLastInsertedId();
    }

    @Override
    public Try<List<PharmacistOrder>> findByStatus(PharmacistOrderStatus pharmacistOrderStatus) {
        return pharmacistOrderRepository.findByStatus(pharmacistOrderStatus);
    }

    @Override
    public Try<PharmacistOrder> save(PharmacistOrder pharmacistOrder) {
        return pharmacistOrderRepository.save(pharmacistOrder);
    }
}
