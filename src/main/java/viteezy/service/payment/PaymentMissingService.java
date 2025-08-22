package viteezy.service.payment;

import io.vavr.collection.Seq;
import io.vavr.control.Try;

public interface PaymentMissingService {

    Try<Seq<Long>> missingPaymentsByChargebackDate(Integer attempts);

    Try<Seq<Long>> upcomingPaymentsByDate(Integer daysUntil);
}
