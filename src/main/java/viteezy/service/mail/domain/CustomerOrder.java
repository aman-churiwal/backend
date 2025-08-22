package viteezy.service.mail.domain;

import io.vavr.collection.Seq;
import viteezy.domain.Customer;
import viteezy.domain.ingredient.Ingredient;
import viteezy.domain.payment.PaymentPlanStatus;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class CustomerOrder {

    private final Customer customer;
    private final PaymentPlanStatus paymentPlanStatus;
    private final Seq<Ingredient> ingredients;
    private final BigDecimal subTotal;
    private final BigDecimal discountTotal;
    private final BigDecimal shippingTotal;
    private final BigDecimal taxTotal;
    private final BigDecimal orderTotal;
    
    public CustomerOrder(Customer customer, PaymentPlanStatus paymentPlanStatus, Seq<Ingredient> ingredients,
                         BigDecimal subTotal, BigDecimal discountTotal, BigDecimal shippingTotal, BigDecimal taxTotal,
                         BigDecimal orderTotal) {
        this.customer = customer;
        this.paymentPlanStatus = paymentPlanStatus;
        this.ingredients = ingredients;
        this.subTotal = subTotal;
        this.discountTotal = discountTotal;
        this.shippingTotal = shippingTotal;
        this.taxTotal = taxTotal;
        this.orderTotal = orderTotal;
    }

    public Customer getCustomer() {
        return customer;
    }

    public PaymentPlanStatus getPaymentPlanStatus() {
        return paymentPlanStatus;
    }

    public Seq<Ingredient> getIngredients() {
        return ingredients;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public BigDecimal getShippingTotal() {
        return shippingTotal;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerOrder that = (CustomerOrder) o;
        return Objects.equals(customer, that.customer) && paymentPlanStatus == that.paymentPlanStatus && Objects.equals(ingredients, that.ingredients) && Objects.equals(subTotal, that.subTotal) && Objects.equals(discountTotal, that.discountTotal) && Objects.equals(shippingTotal, that.shippingTotal) && Objects.equals(taxTotal, that.taxTotal) && Objects.equals(orderTotal, that.orderTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, paymentPlanStatus, ingredients, subTotal, discountTotal, shippingTotal, taxTotal, orderTotal);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CustomerOrder.class.getSimpleName() + "[", "]")
                .add("customer=" + customer)
                .add("paymentPlanStatus=" + paymentPlanStatus)
                .add("ingredients=" + ingredients)
                .add("subTotal=" + subTotal)
                .add("discountTotal=" + discountTotal)
                .add("shippingTotal=" + shippingTotal)
                .add("taxTotal=" + taxTotal)
                .add("orderTotal=" + orderTotal)
                .toString();
    }
}