package viteezy.service.pricing;

import java.math.BigDecimal;

public interface ShippingService {
    BigDecimal getShippingCostForAmount(BigDecimal amount);

    BigDecimal getShippingCostFromFirstAmount(BigDecimal firstAmount);
}
