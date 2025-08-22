package viteezy.service.pricing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.db.*;
import viteezy.service.IngredientService;
import viteezy.service.blend.BlendIngredientPriceService;
import viteezy.service.payment.PaymentPlanService;

import java.math.BigDecimal;

@Configuration("servicesDiscountConfig")
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

    @Bean("couponService")
    public CouponService couponService(CouponRepository couponRepository, ReferralService referralService) {
        return new CouponServiceImpl(couponRepository, referralService);
    }

    @Bean("incentiveService")
    public IncentiveService incentiveService(IncentiveRepository incentiveRepository, PaymentPlanService paymentPlanService) {
        return new IncentiveServiceImpl(incentiveRepository, paymentPlanService);
    }

    @Bean("ingredientPriceService")
    public IngredientPriceService ingredientPriceService(
            IngredientPriceRepository ingredientPriceRepository
    ) {
        return new IngredientPriceServiceImpl(ingredientPriceRepository);
    }

    @Bean("pricingService")
    public PricingService pricingService(IngredientService ingredientService,
                                         BlendIngredientPriceService blendIngredientPriceService,
                                         BlendIngredientRepository blendIngredientRepository,
                                         CouponService couponService, ReferralService referralService,
                                         ShippingService shippingService) {
        return new PricingServiceImpl(ingredientService, blendIngredientPriceService, blendIngredientRepository,
                couponService, referralService, shippingService);
    }

    @Bean("referralService")
    public ReferralService referralService(ReferralRepository referralRepository, CustomerRepository customerRepository) {
        return new ReferralServiceImpl(referralRepository, viteezyConfiguration.getReferralConfiguration(), customerRepository);
    }

    @Bean("shippingService")
    public ShippingService shippingService() {
        final BigDecimal shippingThreshold = viteezyConfiguration.getPaymentConfiguration().getShippingCostsThreshold();
        final BigDecimal shippingCost = viteezyConfiguration.getPaymentConfiguration().getShippingCosts();
        return new ShippingServiceImpl(shippingThreshold, shippingCost);
    }
}
