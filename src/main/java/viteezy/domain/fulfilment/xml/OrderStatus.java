package viteezy.domain.fulfilment.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrderStatus {
    @XmlElement(name = "orderNo")
    private String orderNo;

    @XmlElement(name = "reference")
    private String reference;

    @XmlElement(name = "trackAndTraceCode")
    private String trackAndTraceCode;

    @XmlElement(name = "deliveryMode")
    private String deliveryMode;

    @XmlElement(name = "orderDate")
    private String orderDate;

    @XmlElement(name = "orderTime")
    private String orderTime;

    @XmlElement(name = "postcode")
    private String postcode;

    @XmlElement(name = "huisnummer")
    private String huisnummer;

    @XmlElement(name = "toevoeging")
    private String toevoeging;

    @XmlElement(name = "shipDate")
    private String shipDate;

    @XmlElement(name = "shipTime")
    private String shipTime;

    @XmlElementWrapper(name = "orderStatusLines")
    @XmlElement(name = "orderStatusLine")
    private List<OrderStatusLine> orderStatusLines;

    public OrderStatus(String orderNo, String reference, String trackAndTraceCode, String deliveryMode, String orderDate, String orderTime, String postcode, String huisnummer, String toevoeging, String shipDate, String shipTime, List<OrderStatusLine> orderStatusLines) {
        this.orderNo = orderNo;
        this.reference = reference;
        this.trackAndTraceCode = trackAndTraceCode;
        this.deliveryMode = deliveryMode;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.toevoeging = toevoeging;
        this.shipDate = shipDate;
        this.shipTime = shipTime;
        this.orderStatusLines = orderStatusLines;
    }

    public OrderStatus() {
        super();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getReference() {
        return reference;
    }

    public String getTrackAndTraceCode() {
        return trackAndTraceCode;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public String getToevoeging() {
        return toevoeging;
    }

    public String getShipDate() {
        return shipDate;
    }

    public String getShipTime() {
        return shipTime;
    }

    public List<OrderStatusLine> getOrderStatusLines() {
        return orderStatusLines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatus that = (OrderStatus) o;
        return Objects.equals(orderNo, that.orderNo) && Objects.equals(reference, that.reference) && Objects.equals(trackAndTraceCode, that.trackAndTraceCode) && Objects.equals(deliveryMode, that.deliveryMode) && Objects.equals(orderDate, that.orderDate) && Objects.equals(orderTime, that.orderTime) && Objects.equals(postcode, that.postcode) && Objects.equals(huisnummer, that.huisnummer) && Objects.equals(toevoeging, that.toevoeging) && Objects.equals(shipDate, that.shipDate) && Objects.equals(shipTime, that.shipTime) && Objects.equals(orderStatusLines, that.orderStatusLines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNo, reference, trackAndTraceCode, deliveryMode, orderDate, orderTime, postcode, huisnummer, toevoeging, shipDate, shipTime, orderStatusLines);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderStatus.class.getSimpleName() + "[", "]")
                .add("orderNo='" + orderNo + "'")
                .add("reference='" + reference + "'")
                .add("trackAndTraceCode='" + trackAndTraceCode + "'")
                .add("deliveryMode='" + deliveryMode + "'")
                .add("orderDate='" + orderDate + "'")
                .add("orderTime='" + orderTime + "'")
                .add("postcode='" + postcode + "'")
                .add("huisnummer='" + huisnummer + "'")
                .add("toevoeging='" + toevoeging + "'")
                .add("shipDate='" + shipDate + "'")
                .add("shipTime='" + shipTime + "'")
                .add("orderStatusLines=" + orderStatusLines)
                .toString();
    }
}
