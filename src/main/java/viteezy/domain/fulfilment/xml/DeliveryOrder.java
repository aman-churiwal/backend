package viteezy.domain.fulfilment.xml;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(factoryMethod="createInstanceJAXB")
public class DeliveryOrder {
    @XmlElement(name = "orderNo")
    private final String orderNo;

    @XmlElement(name = "webOrderNo")
    private final String webOrderNo;

    @XmlElement(name = "orderDate")
    private final String orderDate;

    @XmlElement(name = "orderTime")
    private final String orderTime;

    @XmlElement(name = "customerNo")
    private final String customerNo;

    @XmlElement(name = "onlyHomeAddress")
    private final Boolean onlyHomeAddress;

    @XmlElement(name = "shipToFirstName")
    private final String shipToFirstName;

    @XmlElement(name = "shipToLastName")
    private final String shipToLastName;

    @XmlElement(name = "shipToStreet")
    private final String shipToStreet;

    @XmlElement(name = "shipToHouseNo")
    private final String shipToHouseNo;

    @XmlElement(name = "shipToAnnex")
    private final String shipToAnnex;

    @XmlElement(name = "shipToPostalCode")
    private final String shipToPostalCode;

    @XmlElement(name = "shipToCity")
    private final String shipToCity;

    @XmlElement(name = "shipToCountryCode")
    private final String shipToCountryCode;

    @XmlElement(name = "shipToPhone")
    private final String shipToPhone;

    @XmlElement(name = "shipToEmail")
    private final String shipToEmail;

    @XmlElement(name = "language")
    private final String language;

    @XmlElement(name = "shippingAgentCode")
    private final String shippingAgentCode;

    @XmlElement(name = "shipmentType")
    private final String shipmentType;

    @XmlElementWrapper(name = "deliveryOrderLines")
    @XmlElement(name = "deliveryOrderLine")
    private final List<DeliveryOrderLine> deliveryOrderLines;

    public DeliveryOrder(String orderNo, String webOrderNo, String orderDate, String orderTime, String customerNo, Boolean onlyHomeAddress, String shipToFirstName, String shipToLastName, String shipToStreet, String shipToHouseNo, String shipToAnnex, String shipToPostalCode, String shipToCity, String shipToCountryCode, String shipToPhone, String shipToEmail, String language, String shippingAgentCode, String shipmentType, List<DeliveryOrderLine> deliveryOrderLines) {
        this.orderNo = orderNo;
        this.webOrderNo = webOrderNo;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.customerNo = customerNo;
        this.onlyHomeAddress = onlyHomeAddress;
        this.shipToFirstName = shipToFirstName;
        this.shipToLastName = shipToLastName;
        this.shipToStreet = shipToStreet;
        this.shipToHouseNo = shipToHouseNo;
        this.shipToAnnex = shipToAnnex;
        this.shipToPostalCode = shipToPostalCode;
        this.shipToCity = shipToCity;
        this.shipToCountryCode = shipToCountryCode;
        this.shipToPhone = shipToPhone;
        this.shipToEmail = shipToEmail;
        this.language = language;
        this.shippingAgentCode = shippingAgentCode;
        this.shipmentType = shipmentType;
        this.deliveryOrderLines = deliveryOrderLines;
    }

    private static DeliveryOrder createInstanceJAXB() {
        return null;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getWebOrderNo() {
        return webOrderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public Boolean getOnlyHomeAddress() {
        return onlyHomeAddress;
    }

    public String getShipToFirstName() {
        return shipToFirstName;
    }

    public String getShipToLastName() {
        return shipToLastName;
    }

    public String getShipToStreet() {
        return shipToStreet;
    }

    public String getShipToHouseNo() {
        return shipToHouseNo;
    }

    public String getShipToAnnex() {
        return shipToAnnex;
    }

    public String getShipToPostalCode() {
        return shipToPostalCode;
    }

    public String getShipToCity() {
        return shipToCity;
    }

    public String getShipToCountryCode() {
        return shipToCountryCode;
    }

    public String getShipToPhone() {
        return shipToPhone;
    }

    public String getShipToEmail() {
        return shipToEmail;
    }

    public String getLanguage() {
        return language;
    }

    public String getShippingAgentCode() {
        return shippingAgentCode;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public List<DeliveryOrderLine> getDeliveryOrderLines() {
        return deliveryOrderLines;
    }
}
