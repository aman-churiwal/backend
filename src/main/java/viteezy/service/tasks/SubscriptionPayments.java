package viteezy.service.tasks;

import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Component;
import viteezy.service.payment.PaymentRecurringService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Task
@Component
public class SubscriptionPayments extends io.dropwizard.servlets.tasks.Task {

    private final PaymentRecurringService paymentRecurringService;

    protected SubscriptionPayments(PaymentRecurringService paymentRecurringService) {
        super(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, SubscriptionPayments.class.getSimpleName()));
        this.paymentRecurringService = paymentRecurringService;
    }

    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) throws Exception {
        if (map.containsKey("daysAgo")) {
            List<String> days = map.get("daysAgo");
            Integer daysAgo = Integer.valueOf(days.iterator().next());
            paymentRecurringService.subscriptionPayments(daysAgo);
        } else {
            throw new NoSuchMethodException("daysAgo is missing");
        }
    }
}
