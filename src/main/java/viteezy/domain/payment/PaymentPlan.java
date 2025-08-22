package viteezy.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

public record PaymentPlan(Long id, BigDecimal firstAmount, BigDecimal recurringAmount, Integer recurringMonths,
                          Long customerId, Long blendId, UUID externalReference, LocalDateTime creationDate,
                          LocalDateTime lastModified, PaymentPlanStatus status, LocalDateTime paymentDate,
                          String stopReason, LocalDateTime deliveryDate, Optional<LocalDateTime> nextPaymentDate,
                          Optional<LocalDateTime> nextDeliveryDate, String paymentMethod) {
}
