package viteezy.gateways.infobip;

import viteezy.domain.fulfilment.Order;

public interface InfobipService {

    void sendOrderAtPickUpLocation(Order order);
}
