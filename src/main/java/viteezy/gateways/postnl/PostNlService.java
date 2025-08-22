package viteezy.gateways.postnl;

import io.vavr.control.Try;
import viteezy.controller.dto.AddressCheckPostRequest;
import viteezy.domain.postnl.Address;
import viteezy.domain.postnl.Shipment;

import java.util.List;

public interface PostNlService {

    Try<List<Address>> checkAddress(AddressCheckPostRequest addressCheckPostRequest);

    Try<List<Shipment>> getShippingStatuses();
}
