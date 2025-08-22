package viteezy.domain.fulfilment;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class PharmacistOrder {
    private final Long id;
    private final String batchName;
    private final Integer batchNumber;
    private final String orderNumber;
    private final String fileName;
    private final PharmacistOrderStatus status;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public PharmacistOrder(Long id, String batchName, Integer batchNumber, String orderNumber, String fileName,
                           PharmacistOrderStatus status, LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp) {
        this.id = id;
        this.batchName = batchName;
        this.batchNumber = batchNumber;
        this.orderNumber = orderNumber;
        this.fileName = fileName;
        this.status = status;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public String getBatchName() {
        return batchName;
    }

    public Integer getBatchNumber() {
        return batchNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public PharmacistOrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PharmacistOrder that = (PharmacistOrder) o;
        return Objects.equals(id, that.id) && Objects.equals(batchName, that.batchName) && Objects.equals(batchNumber, that.batchNumber) && Objects.equals(orderNumber, that.orderNumber) && Objects.equals(fileName, that.fileName) && status == that.status && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, batchName, batchNumber, orderNumber, fileName, status, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PharmacistOrder.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("batchName='" + batchName + "'")
                .add("batchNumber=" + batchNumber)
                .add("orderNumber='" + orderNumber + "'")
                .add("fileName='" + fileName + "'")
                .add("status=" + status)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
