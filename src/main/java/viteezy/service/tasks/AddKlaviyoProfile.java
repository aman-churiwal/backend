package viteezy.service.tasks;

import com.google.common.base.CaseFormat;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import viteezy.db.CustomerRepository;
import viteezy.db.PaymentPlanRepository;
import viteezy.db.PaymentRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.payment.PaymentStatus;
import viteezy.gateways.klaviyo.KlaviyoService;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Task
@Component
public class AddKlaviyoProfile extends io.dropwizard.servlets.tasks.Task {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddKlaviyoProfile.class);

    private final CustomerRepository customerRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final PaymentRepository paymentRepository;
    private final QuizRepository quizRepository;
    private final KlaviyoService klaviyoService;

    protected AddKlaviyoProfile(CustomerRepository customerRepository, PaymentPlanRepository paymentPlanRepository,
                                PaymentRepository paymentRepository, QuizRepository quizRepository,
                                KlaviyoService klaviyoService) {
        super(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, AddKlaviyoProfile.class.getSimpleName()));
        this.customerRepository = customerRepository;
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentRepository = paymentRepository;
        this.quizRepository = quizRepository;
        this.klaviyoService = klaviyoService;
    }

    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) {
        customerRepository.findKlaviyoToBeImported()
                .peek(customers -> customers
                        .forEach(customer -> paymentPlanRepository.findAllByCustomerId(customer.getId())
                                .map(paymentPlans -> paymentPlans.stream().max(Comparator.comparing(PaymentPlan::lastModified)).get())
                                .peek(paymentPlan -> klaviyoService.upsertExistingCustomer(customer, paymentPlan)
                                        .peek(__ -> customerRepository.find(paymentPlan.customerId())
                                                .peek(updatedCustomer -> paymentRepository.findByCustomerId(updatedCustomer.getId())
                                                        .map(payments -> payments.stream().filter(payment -> PaymentStatus.paid.equals(payment.getStatus())))
                                                        .peek(payments -> payments.forEach(payment -> klaviyoService.createPurchasePaidEvent(updatedCustomer, paymentPlan, payment)))))
                                        .onFailure(peekException()))
                                .onFailure(__ -> quizRepository.findByCustomerId(customer.getId())
                                        .peek(quiz -> klaviyoService.upsertExistingInitialProfile(customer, quiz)))
                        )
                ).onFailure(peekException());
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
