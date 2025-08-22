package viteezy.service.tasks;

import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Component;
import viteezy.service.payment.PaymentPlanService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Task
@Component
public class UpdatePaymentDeliveryDate extends io.dropwizard.servlets.tasks.Task {

    private final PaymentPlanService paymentPlanService;

    protected UpdatePaymentDeliveryDate(PaymentPlanService paymentPlanService) {
        super(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, UpdatePaymentDeliveryDate.class.getSimpleName()));
        this.paymentPlanService = paymentPlanService;
    }

    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) throws Exception {
        paymentPlanService.updatePaymentDateWithNextPaymentDate()
                .flatMap(__ -> paymentPlanService.updateDeliveryDateWithNextDeliveryDate());

        paymentPlanService.updatePaymentDate()
                .flatMap(__ -> paymentPlanService.updateDeliveryDate());
    }
}
