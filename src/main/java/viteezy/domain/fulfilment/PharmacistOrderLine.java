package viteezy.domain.fulfilment;

import be.woutschoovaerts.mollie.data.payment.SequenceType;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class PharmacistOrderLine {

    private final Long orderId;
    public final String orderNumber;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String postcode;
    private final String city;
    private final Integer recurringMonths;
    private final SequenceType sequenceType;
    private final String pharmacistOrderNumber;
    private final Long pharmacistIngredientCode;
    private final BigDecimal pharmacistIngredientUnit;
    private final Integer pieces;

    public PharmacistOrderLine(Long orderId, String orderNumber, String email, String firstName, String lastName,
                               String postcode, String city, Integer recurringMonths, SequenceType sequenceType,
                               String pharmacistOrderNumber, Long pharmacistIngredientCode,
                               BigDecimal pharmacistIngredientUnit, Integer pieces) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postcode = postcode;
        this.city = city;
        this.recurringMonths = recurringMonths;
        this.sequenceType = sequenceType;
        this.pharmacistOrderNumber = pharmacistOrderNumber;
        this.pharmacistIngredientCode = pharmacistIngredientCode;
        this.pharmacistIngredientUnit = pharmacistIngredientUnit;
        this.pieces = pieces;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    public Integer getRecurringMonths() {
        return recurringMonths;
    }

    public SequenceType getSequenceType() {
        return sequenceType;
    }

    public String getPharmacistOrderNumber() {
        return pharmacistOrderNumber;
    }

    public Long getPharmacistIngredientCode() {
        return pharmacistIngredientCode;
    }

    public BigDecimal getPharmacistIngredientUnit() {
        return pharmacistIngredientUnit;
    }

    public Integer getPieces() {
        return pieces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PharmacistOrderLine that = (PharmacistOrderLine) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(orderNumber, that.orderNumber) && Objects.equals(email, that.email) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(postcode, that.postcode) && Objects.equals(city, that.city) && Objects.equals(recurringMonths, that.recurringMonths) && sequenceType == that.sequenceType && Objects.equals(pharmacistOrderNumber, that.pharmacistOrderNumber) && Objects.equals(pharmacistIngredientCode, that.pharmacistIngredientCode) && Objects.equals(pharmacistIngredientUnit, that.pharmacistIngredientUnit) && Objects.equals(pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderNumber, email, firstName, lastName, postcode, city, recurringMonths, sequenceType, pharmacistOrderNumber, pharmacistIngredientCode, pharmacistIngredientUnit, pieces);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PharmacistOrderLine.class.getSimpleName() + "[", "]")
                .add("orderId=" + orderId)
                .add("orderNumber='" + orderNumber + "'")
                .add("email='" + email + "'")
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("postcode='" + postcode + "'")
                .add("city='" + city + "'")
                .add("recurringMonths=" + recurringMonths)
                .add("sequenceType=" + sequenceType)
                .add("pharmacistOrderNumber='" + pharmacistOrderNumber + "'")
                .add("pharmacistIngredientCode=" + pharmacistIngredientCode)
                .add("pharmacistIngredientUnit=" + pharmacistIngredientUnit)
                .add("pieces=" + pieces)
                .toString();
    }
}
