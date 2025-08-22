package viteezy.domain.postnl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Shipment {

    private final String barcode;
    private final LocalDateTime creationDate;
    private final Long customerNumber;
    private final String customerCode;
    private final ShipmentStatus status;

    @JsonCreator
    public Shipment(@JsonProperty("Barcode") String barcode,
                    @JsonProperty("CreationDate") LocalDateTime creationDate,
                    @JsonProperty("CustomerNumber") Long customerNumber,
                    @JsonProperty("CustomerCode") String customerCode,
                    @JsonProperty("Status") ShipmentStatus status) {
        this.barcode = barcode;
        this.creationDate = creationDate;
        this.customerNumber = customerNumber;
        this.customerCode = customerCode;
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipment shipment = (Shipment) o;
        return Objects.equals(barcode, shipment.barcode) && Objects.equals(creationDate, shipment.creationDate) && Objects.equals(customerNumber, shipment.customerNumber) && Objects.equals(customerCode, shipment.customerCode) && Objects.equals(status, shipment.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barcode, creationDate, customerNumber, customerCode, status);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Shipment.class.getSimpleName() + "[", "]")
                .add("barcode='" + barcode + "'")
                .add("creationDate=" + creationDate)
                .add("customerNumber=" + customerNumber)
                .add("customerCode='" + customerCode + "'")
                .add("status=" + status)
                .toString();
    }
}
