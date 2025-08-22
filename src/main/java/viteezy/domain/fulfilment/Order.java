package viteezy.domain.fulfilment;

import be.woutschoovaerts.mollie.data.payment.SequenceType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Order {
    private final Long id;
    private final UUID externalReference;
    private final String orderNumber;
    private final Long paymentId;
    private final SequenceType sequenceType;
    private final Long paymentPlanId;
    private final Long blendId;
    private final Long customerId;
    private final Integer recurringMonths;
    private final String shipToFirstName;
    private final String shipToLastName;
    private final String shipToStreet;
    private final String shipToHouseNo;
    private final String shipToAnnex;
    private final String shipToPostalCode;
    private final String shipToCity;
    private final String shipToCountryCode;
    private final String shipToPhone;
    private final String shipToEmail;
    private final String referralCode;
    private final String trackTraceCode;
    private final String pharmacistOrderNumber;
    private final OrderStatus status;
    private final LocalDateTime created;
    private final LocalDateTime shipped;
    private final LocalDateTime lastModified;

    public Order(Long id, UUID externalReference, String orderNumber, Long paymentId, SequenceType sequenceType, Long paymentPlanId, Long blendId, Long customerId, Integer recurringMonths, String shipToFirstName, String shipToLastName, String shipToStreet, String shipToHouseNo, String shipToAnnex, String shipToPostalCode, String shipToCity, String shipToCountryCode, String shipToPhone, String shipToEmail, String referralCode, String trackTraceCode, String pharmacistOrderNumber, OrderStatus status, LocalDateTime created, LocalDateTime shipped, LocalDateTime lastModified) {
        this.id = id;
        this.externalReference = externalReference;
        this.orderNumber = orderNumber;
        this.paymentId = paymentId;
        this.sequenceType = sequenceType;
        this.paymentPlanId = paymentPlanId;
        this.blendId = blendId;
        this.customerId = customerId;
        this.recurringMonths = recurringMonths;
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
        this.referralCode = referralCode;
        this.trackTraceCode = trackTraceCode;
        this.pharmacistOrderNumber = pharmacistOrderNumber;
        this.status = status;
        this.created = created;
        this.shipped = shipped;
        this.lastModified = lastModified;
    }

    public Long getId() {
        return id;
    }

    public UUID getExternalReference() {
        return externalReference;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public SequenceType getSequenceType() {
        return sequenceType;
    }

    public Long getPaymentPlanId() {
        return paymentPlanId;
    }

    public Long getBlendId() {
        return blendId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Integer getRecurringMonths() {
        return recurringMonths;
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

    public String getReferralCode() {
        return referralCode;
    }

    public String getTrackTraceCode() {
        return trackTraceCode;
    }

    public String getPharmacistOrderNumber() {
        return pharmacistOrderNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getShipped() {
        return shipped;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(externalReference, order.externalReference) && Objects.equals(orderNumber, order.orderNumber) && Objects.equals(paymentId, order.paymentId) && sequenceType == order.sequenceType && Objects.equals(paymentPlanId, order.paymentPlanId) && Objects.equals(blendId, order.blendId) && Objects.equals(customerId, order.customerId) && Objects.equals(recurringMonths, order.recurringMonths) && Objects.equals(shipToFirstName, order.shipToFirstName) && Objects.equals(shipToLastName, order.shipToLastName) && Objects.equals(shipToStreet, order.shipToStreet) && Objects.equals(shipToHouseNo, order.shipToHouseNo) && Objects.equals(shipToAnnex, order.shipToAnnex) && Objects.equals(shipToPostalCode, order.shipToPostalCode) && Objects.equals(shipToCity, order.shipToCity) && Objects.equals(shipToCountryCode, order.shipToCountryCode) && Objects.equals(shipToPhone, order.shipToPhone) && Objects.equals(shipToEmail, order.shipToEmail) && Objects.equals(referralCode, order.referralCode) && Objects.equals(trackTraceCode, order.trackTraceCode) && Objects.equals(pharmacistOrderNumber, order.pharmacistOrderNumber) && status == order.status && Objects.equals(created, order.created) && Objects.equals(shipped, order.shipped) && Objects.equals(lastModified, order.lastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalReference, orderNumber, paymentId, sequenceType, paymentPlanId, blendId, customerId, recurringMonths, shipToFirstName, shipToLastName, shipToStreet, shipToHouseNo, shipToAnnex, shipToPostalCode, shipToCity, shipToCountryCode, shipToPhone, shipToEmail, referralCode, trackTraceCode, pharmacistOrderNumber, status, created, shipped, lastModified);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("externalReference=" + externalReference)
                .add("orderNumber='" + orderNumber + "'")
                .add("paymentId=" + paymentId)
                .add("sequenceType=" + sequenceType)
                .add("paymentPlanId=" + paymentPlanId)
                .add("blendId=" + blendId)
                .add("customerId=" + customerId)
                .add("recurringMonths=" + recurringMonths)
                .add("shipToFirstName='" + shipToFirstName + "'")
                .add("shipToLastName='" + shipToLastName + "'")
                .add("shipToStreet='" + shipToStreet + "'")
                .add("shipToHouseNo='" + shipToHouseNo + "'")
                .add("shipToAnnex='" + shipToAnnex + "'")
                .add("shipToPostalCode='" + shipToPostalCode + "'")
                .add("shipToCity='" + shipToCity + "'")
                .add("shipToCountryCode='" + shipToCountryCode + "'")
                .add("shipToPhone='" + shipToPhone + "'")
                .add("shipToEmail='" + shipToEmail + "'")
                .add("referralCode='" + referralCode + "'")
                .add("trackTraceCode='" + trackTraceCode + "'")
                .add("pharmacistOrderNumber='" + pharmacistOrderNumber + "'")
                .add("status=" + status)
                .add("created=" + created)
                .add("shipped=" + shipped)
                .add("lastModified=" + lastModified)
                .toString();
    }
}
