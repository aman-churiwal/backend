package viteezy.service.pricing;

import java.math.BigDecimal;

public class ShippingServiceImpl implements ShippingService {

    private final BigDecimal shippingThreshold;
    private final BigDecimal shippingCost;
    
    public ShippingServiceImpl(BigDecimal shippingThreshold, BigDecimal shippingCost) {
        this.shippingThreshold = shippingThreshold;
        this.shippingCost = shippingCost;
    }

    @Override
    public BigDecimal getShippingCostForAmount(BigDecimal amount) {
        return amount.compareTo(shippingThreshold) < 0 ? shippingCost : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getShippingCostFromFirstAmount(BigDecimal firstAmount) {
        return firstAmount.compareTo(shippingThreshold.add(shippingCost)) < 0 ? shippingCost : BigDecimal.ZERO;
    }
}
