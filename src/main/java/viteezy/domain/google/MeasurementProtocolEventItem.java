package viteezy.domain.google;

import java.math.BigDecimal;

public class MeasurementProtocolEventItem {
    private final String itemName;
    private final Long itemId;
    private final String itemVariant;
    private final BigDecimal price;
    private final int quantity;

    public MeasurementProtocolEventItem(String itemName, Long itemId, String itemVariant, BigDecimal price, int quantity) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.itemVariant = itemVariant;
        this.price = price;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public Long getItemId() {
        return itemId;
    }

    public String getItemVariant() {
        return itemVariant;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
