package viteezy.service.tasks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import viteezy.db.CustomerRepository;
import viteezy.db.PaymentPlanRepository;
import viteezy.db.PaymentRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.fulfilment.CSVWriterService;
import viteezy.service.fulfilment.XMLWriterService;
import viteezy.service.payment.PaymentMissingService;
import viteezy.service.payment.PaymentRecurringService;

@Configuration("tasksIoC")
public class IoC {

    @Bean
    public MissingPayments retryPayments(PaymentMissingService paymentMissingService) {
        return new MissingPayments(paymentMissingService);
    }

    @Bean
    public SubscriptionPayments subscriptionPayments(PaymentRecurringService paymentRecurringService) {
        return new SubscriptionPayments(paymentRecurringService);
    }

    @Bean
    public PostNlFulfilmentRequest postNLFulfilmentRequest(XMLWriterService xmlWriterService) {
        return new PostNlFulfilmentRequest(xmlWriterService);
    }

    @Bean
    public PharmacistRequest pharmacistRequest(CSVWriterService csvWriterService) {
        return new PharmacistRequest(csvWriterService);
    }

    @Bean
    public AddKlaviyoProfile addKlaviyoProfile(CustomerRepository customerRepository,
                                               PaymentPlanRepository paymentPlanRepository,
                                               PaymentRepository paymentRepository, QuizRepository quizRepository,
                                               KlaviyoService klaviyoService) {
        return new AddKlaviyoProfile(customerRepository, paymentPlanRepository, paymentRepository, quizRepository, klaviyoService);
    }
}
