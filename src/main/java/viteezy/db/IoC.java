package viteezy.db;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import viteezy.db.jdbi.*;
import viteezy.db.jdbi.klaviyo.KlaviyoRepositoryImpl;
import viteezy.db.klaviyo.KlaviyoRepository;


@Configuration("dbConfig")
@Import({viteezy.db.jdbi.IoC.class})
public class IoC {

    @Bean("blendRepository")
    public BlendRepository blendRepository(Jdbi jdbi) {
        return new BlendRepositoryImpl(jdbi);
    }

    @Bean("blendIngredientRepository")
    public BlendIngredientRepository blenIngredientRepository(Jdbi jdbi) {
        return new BlendIngredientRepositoryImpl(jdbi);
    }

    @Bean("blendIngredientReasonRepository")
    public BlendIngredientReasonRepository blendIngredientReasonRepository(Jdbi jdbi) {
        return new BlendIngredientReasonRepositoryImpl(jdbi);
    }

    @Bean("bundleRepository")
    public BundleRepository bundleRepository(Jdbi jdbi) {
        return new BundleRepositoryImpl(jdbi);
    }

    @Bean("contentRepository")
    public ContentRepository contentRepository(Jdbi jdbi) {
        return new ContentRepositoryImpl(jdbi);
    }

    @Bean("couponRepository")
    public CouponRepository couponRepository(Jdbi jdbi) {
        return new CouponRepositoryImpl(jdbi);
    }

    @Bean("customerRepository")
    public CustomerRepository customerRepository(Jdbi jdbi) {
        return new CustomerRepositoryImpl(jdbi);
    }

    @Bean("incentiveRepository")
    public IncentiveRepository incentiveRepository(Jdbi jdbi) {
        return new IncentiveRepositoryImpl(jdbi);
    }

    @Bean("ingredientPriceRepository")
    public IngredientPriceRepository ingredientPriceRepository(Jdbi jdbi) {
        return new IngredientPriceRepositoryImpl(jdbi);
    }

    @Bean("ingredientRepository")
    public IngredientRepository ingredientRepository(Jdbi jdbi) {
        return new IngredientRepositoryImpl(jdbi);
    }

    @Bean("ingredientUnitRepository")
    public IngredientUnitRepository ingredientUnitRepository(Jdbi jdbi) {
        return new IngredientUnitRepositoryImpl(jdbi);
    }

    @Bean("klaviyoRepository")
    public KlaviyoRepository klaviyoRepository(Jdbi jdbi) {
        return new KlaviyoRepositoryImpl(jdbi);
    }

    @Bean("loggingRepository")
    public LoggingRepository loggingRepository(Jdbi jdbi) {
        return new LoggingRepositoryImpl(jdbi);
    }

    @Bean("loginRepository")
    public LoginRepository loginRepository(Jdbi jdbi) {
        return new LoginRepositoryImpl(jdbi);
    }

    @Bean("paymentPlanRepository")
    public PaymentPlanRepository paymentPlanRepository(Jdbi jdbi) {
        return new PaymentPlanRepositoryImpl(jdbi);
    }

    @Bean("paymentRepository")
    public PaymentRepository paymentRepository(Jdbi jdbi) {
        return new PaymentRepositoryImpl(jdbi);
    }

    @Bean("pharmacistOrderRepository")
    public PharmacistOrderRepository pharmacistOrderRepository(Jdbi jdbi) {
        return new PharmacistOrderRepositoryImpl(jdbi);
    }

    @Bean("referralRepository")
    public ReferralRepository referralRepository(Jdbi jdbi) {
        return new ReferralRepositoryImpl(jdbi);
    }

    @Bean("reviewRepository")
    public ReviewRepository reviewRepository(Jdbi jdbi) {
        return new ReviewRepositoryImpl(jdbi);
    }

    @Bean
    public OrderRepository orderRepository(Jdbi jdbi) {
        return new OrderRepositoryImpl(jdbi);
    }
}