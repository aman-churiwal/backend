package viteezy.service.payment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.db.PaymentPlanRepository;
import viteezy.db.PaymentRepository;
import viteezy.gateways.facebook.FacebookService;
import viteezy.gateways.googleanalytics.GoogleAnalyticsService;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.gateways.slack.SlackService;
import viteezy.service.CustomerService;
import viteezy.service.LoggingService;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.blend.BlendService;
import viteezy.service.fulfilment.OrderService;
import viteezy.service.mail.EmailService;
import viteezy.service.pricing.CouponService;
import viteezy.service.pricing.IncentiveService;
import viteezy.service.pricing.PricingService;
import viteezy.service.pricing.ReferralService;

@Configuration("paymentServicesConfig")
@Import({
        viteezy.db.IoC.class
})
public class IoC {

    private final ViteezyConfiguration viteezyConfiguration;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(
            ViteezyConfiguration viteezyConfiguration
    ) {
        this.viteezyConfiguration = viteezyConfiguration;
    }

    @Bean("mollieService")
    public MollieService mollieService() {
        return new MollieServiceImpl(viteezyConfiguration.getPaymentConfiguration());
    }

    @Bean("paymentService")
    public PaymentService paymentService(
            MollieService mollieService, BlendService blendService, CustomerService customerService,
            PaymentPlanRepository paymentPlanRepository, PaymentRepository paymentRepository,
            CouponService couponService, KlaviyoService klaviyoService,
            PricingService pricingService, ReferralService referralService
    ) {
        return new PaymentServiceImpl(mollieService, blendService, customerService,
                viteezyConfiguration.getPaymentConfiguration(), paymentPlanRepository, paymentRepository,
                couponService, klaviyoService, pricingService, referralService);
    }

    @Bean("paymentPlanService")
    public PaymentPlanService paymentPlanService(
            BlendService blendService, BlendIngredientService blendIngredientService, CustomerService customerService,
            PaymentPlanRepository paymentPlanRepository, PaymentRepository paymentRepository,
            CouponService couponService, EmailService emailService, KlaviyoService klaviyoService,
            PricingService pricingService, LoggingService loggingService
    ) {
        return new PaymentPlanServiceImpl(blendService, blendIngredientService, customerService,
                viteezyConfiguration.getPaymentConfiguration(), paymentPlanRepository, paymentRepository,
                couponService, emailService, klaviyoService, pricingService, loggingService);
    }

    @Bean("paymentCallbackService")
    public PaymentCallbackService paymentCallbackService(
            MollieService mollieService, PaymentPlanRepository paymentPlanRepository, PaymentService paymentService,
            GoogleAnalyticsService googleAnalyticsService, FacebookService facebookService, EmailService emailService,
            CouponService couponService, ReferralService referralService,
            CustomerService customerService, SlackService slackService, OrderService orderService,
            LoggingService loggingService, KlaviyoService klaviyoService) {
        return new PaymentCallbackServiceImpl(mollieService, paymentPlanRepository, paymentService, googleAnalyticsService,
                facebookService, viteezyConfiguration.getPaymentConfiguration(), emailService, couponService,
                referralService, customerService, slackService, orderService, loggingService, klaviyoService);
    }

    @Bean
    public PaymentMissingServiceImpl paymentRetryService(PaymentRepository paymentRepository,
                                                         PaymentPlanRepository paymentPlanRepository,
                                                         CustomerService customerService,
                                                         EmailService emailService) {
        return new PaymentMissingServiceImpl(paymentRepository, paymentPlanRepository, customerService, emailService);
    }

    @Bean
    public PaymentRecurringService paymentRecurringService(PaymentService paymentService,
                                                           PaymentPlanService paymentPlanService,
                                                           CustomerService customerService, MollieService mollieService,
                                                           EmailService emailService, CouponService couponService,
                                                           ReferralService referralService,
                                                           IncentiveService incentiveService,
                                                           PaymentPlanRepository paymentPlanRepository,
                                                           LoggingService loggingService) {
        return new PaymentRecurringServiceImpl(paymentService, paymentPlanService, viteezyConfiguration.getPaymentConfiguration(), customerService, mollieService, emailService, couponService, referralService, incentiveService, paymentPlanRepository, loggingService);
    }
}
