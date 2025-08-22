package viteezy.domain.fulfilment.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(factoryMethod="createInstanceJAXB")
public class DeliveryOrderLine {
    @XmlElement(name = "itemNo")
    private final String itemNo;

    @XmlElement(name = "itemDescription")
    private final String itemDescription;

    @XmlElement(name = "quantity")
    private final Integer quantity;

    public DeliveryOrderLine(String itemNo, String itemDescription, Integer quantity) {
        this.itemNo = itemNo;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
    }

    private static DeliveryOrderLine createInstanceJAXB() {
        return null;
    }

    public String getItemNo() {
        return itemNo;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
