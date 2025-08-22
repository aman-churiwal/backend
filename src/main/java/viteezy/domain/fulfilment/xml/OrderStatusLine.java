package viteezy.domain.fulfilment.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.Objects;
import java.util.StringJoiner;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrderStatusLine {
    @XmlElement(name = "itemNo")
    private String itemNo;

    @XmlElement(name = "quantity")
    private Integer quantity;

    @XmlElement(name = "expiryDate")
    private String expiryDate;

    public OrderStatusLine(String itemNo, Integer quantity, String expiryDate) {
        this.itemNo = itemNo;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    private OrderStatusLine() {
        super();
    }

    public String getItemNo() {
        return itemNo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatusLine that = (OrderStatusLine) o;
        return Objects.equals(itemNo, that.itemNo) && Objects.equals(quantity, that.quantity) && Objects.equals(expiryDate, that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemNo, quantity, expiryDate);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderStatusLine.class.getSimpleName() + "[", "]")
                .add("itemNo='" + itemNo + "'")
                .add("quantity=" + quantity)
                .add("expiryDate='" + expiryDate + "'")
                .toString();
    }
}
