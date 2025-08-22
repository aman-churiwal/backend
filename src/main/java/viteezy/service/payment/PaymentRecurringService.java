package viteezy.service.payment;

import io.vavr.collection.Seq;
import io.vavr.control.Try;


public interface PaymentRecurringService {

    Try<Seq<Long>> subscriptionPayments(Integer daysAgo);
}
