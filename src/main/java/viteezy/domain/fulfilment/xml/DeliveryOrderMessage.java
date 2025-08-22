package viteezy.domain.fulfilment.xml;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(factoryMethod="createInstanceJAXB")
@XmlRootElement(name = "message")
public class DeliveryOrderMessage {
    @XmlElement(name = "type")
    private final String type = "deliveryOrder";

    @XmlElement(name = "messageNo")
    private final Long messageNo;

    @XmlElement(name = "date")
    private final String date;

    @XmlElement(name = "time")
    private final String time;

    @XmlElementWrapper(name = "deliveryOrders")
    @XmlElement(name = "deliveryOrder")
    private final List<DeliveryOrder> deliveryOrders;

    public DeliveryOrderMessage(Long messageNo, String date, String time, List<DeliveryOrder> deliveryOrders) {
        this.messageNo = messageNo;
        this.date = date;
        this.time = time;
        this.deliveryOrders = deliveryOrders;
    }

    private static DeliveryOrderMessage createInstanceJAXB() {
        return null;
    }

    public String getType() {
        return type;
    }

    public Long getMessageNo() {
        return messageNo;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public List<DeliveryOrder> getDeliveryOrders() {
        return deliveryOrders;
    }
}
