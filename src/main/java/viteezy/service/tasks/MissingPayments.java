package viteezy.service.tasks;

import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Component;
import viteezy.service.payment.PaymentMissingService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Task
@Component
public class MissingPayments extends io.dropwizard.servlets.tasks.Task {

    private final PaymentMissingService paymentMissingService;

    protected MissingPayments(PaymentMissingService paymentMissingService) {
        super(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, MissingPayments.class.getSimpleName()));
        this.paymentMissingService = paymentMissingService;
    }

    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) throws Exception {
        if (map.containsKey("attempts")) {
            List<String> days = map.get("attempts");
            Integer attempts = Integer.valueOf(days.iterator().next());
            paymentMissingService.missingPaymentsByChargebackDate(attempts);
        } else {
            throw new NoSuchMethodException("attempts is missing");
        }
    }
}
