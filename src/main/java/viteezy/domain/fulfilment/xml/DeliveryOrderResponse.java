package viteezy.domain.fulfilment.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;
import java.util.StringJoiner;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "message")
public class DeliveryOrderResponse {
    @XmlElement(name = "type")
    private String type;

    @XmlElement(name = "messageNo")
    private String messageNo;

    @XmlElement(name = "date")
    private String date;

    @XmlElement(name = "time")
    private String time;

    @XmlElement(name = "retailerName")
    private String retailerName;

    @XmlElement(name = "orderStatus")
    private OrderStatus orderStatus;

    public DeliveryOrderResponse(String type, String messageNo, String date, String time, String retailerName) {
        this.type = type;
        this.messageNo = messageNo;
        this.date = date;
        this.time = time;
        this.retailerName = retailerName;
    }

    public DeliveryOrderResponse() {
        super();
    }

    public String getType() {
        return type;
    }

    public String getMessageNo() {
        return messageNo;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryOrderResponse that = (DeliveryOrderResponse) o;
        return Objects.equals(type, that.type) && Objects.equals(messageNo, that.messageNo) && Objects.equals(date, that.date) && Objects.equals(time, that.time) && Objects.equals(retailerName, that.retailerName) && Objects.equals(orderStatus, that.orderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, messageNo, date, time, retailerName, orderStatus);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DeliveryOrderResponse.class.getSimpleName() + "[", "]")
                .add("type='" + type + "'")
                .add("messageNo='" + messageNo + "'")
                .add("date='" + date + "'")
                .add("time='" + time + "'")
                .add("retailerName='" + retailerName + "'")
                .add("orderStatus=" + orderStatus)
                .toString();
    }
}
