package viteezy.service.tasks;

import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Component;
import viteezy.service.payment.PaymentMissingService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Task
@Component
public class UpcomingPayments extends io.dropwizard.servlets.tasks.Task {

    private final PaymentMissingService paymentMissingService;

    protected UpcomingPayments(PaymentMissingService paymentMissingService) {
        super(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, UpcomingPayments.class.getSimpleName()));
        this.paymentMissingService = paymentMissingService;
    }

    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) throws Exception {
        if (map.containsKey("daysUntil")) {
            List<String> days = map.get("daysUntil");
            Integer daysUntil = Integer.valueOf(days.iterator().next());
            paymentMissingService.upcomingPaymentsByDate(daysUntil);
        } else {
            throw new NoSuchMethodException("daysUntil is missing");
        }
    }
}
