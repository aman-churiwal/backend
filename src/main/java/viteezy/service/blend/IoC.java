package viteezy.service.blend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import viteezy.configuration.PaymentConfiguration;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.db.*;
import viteezy.domain.ingredient.IngredientCode;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.CustomerService;
import viteezy.service.IngredientService;
import viteezy.service.LoggingService;
import viteezy.service.blend.preview.QuizBlendPreviewServiceV2;
import viteezy.service.blend.v2.BlendIngredientProcessorV2;
import viteezy.service.payment.PaymentPlanService;
import viteezy.service.pricing.CouponService;
import viteezy.service.pricing.IngredientPriceService;
import viteezy.service.pricing.PricingService;
import viteezy.service.quiz.*;

import java.util.HashMap;
import java.util.Map;

@EnableAsync
@Configuration("blendConfig")
@Import({
        viteezy.db.IoC.class
})
public class IoC {

    private final PaymentConfiguration paymentConfiguration;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(
            ViteezyConfiguration viteezyConfiguration
    ) {
        this.paymentConfiguration = viteezyConfiguration.getPaymentConfiguration();
    }

    @Bean("blendService")
    public BlendService blendService(IngredientPriceService ingredientPriceService,
                                     BlendIngredientService blendIngredientService,
                                     BlendIngredientPriceService blendIngredientPriceService,
                                     BlendRepository blendRepository, BundleRepository bundleRepository) {
        return new BlendServiceImpl(ingredientPriceService, blendIngredientService, blendIngredientPriceService, blendRepository, bundleRepository);
    }

    @Bean("blendIngredientPriceService")
    public BlendIngredientPriceService blendIngredientPriceService(IngredientPriceService ingredientPriceService) {
        return new BlendIngredientPriceServiceImpl(ingredientPriceService);
    }

    @Bean("blendIngredientReasonService")
    public BlendIngredientReasonService blendIngredientReasonService(BlendIngredientReasonRepository blendIngredientReasonRepository) {
        return new BlendIngredientReasonServiceImpl(blendIngredientReasonRepository);
    }

    @Bean("blendProcessorV2")
    public BlendProcessorV2 blendProcessorV2(
            IngredientRepository ingredientRepository, BlendIngredientPriceService blendIngredientPriceService,
            BlendIngredientReasonService blendIngredientReasonService,
            Map<String, BlendIngredientProcessorV2> blendIngredientProcessorMap,
            VitaminIntakeAnswerService vitaminIntakeAnswerService,
            VitaminIntakeService vitaminIntakeService,
            AmountOfMeatConsumptionAnswerService amountOfMeatConsumptionAnswerService,
            AmountOfMeatConsumptionService amountOfMeatConsumptionService,
            AmountOfFishConsumptionAnswerService amountOfFishConsumptionAnswerService,
            AmountOfFishConsumptionService amountOfFishConsumptionService
    ) {
        return new BlendProcessorImplV2(
                ingredientRepository, blendIngredientPriceService, blendIngredientReasonService,
                blendIngredientProcessorMap, vitaminIntakeAnswerService, vitaminIntakeService,
                amountOfMeatConsumptionAnswerService, amountOfMeatConsumptionService, amountOfFishConsumptionAnswerService,
                amountOfFishConsumptionService);
    }

    @Bean("blendIngredientProcessorMapV2")
    public Map<String, BlendIngredientProcessorV2> blendIngredientProcessorMapV2() {
        Map<String, BlendIngredientProcessorV2> blendIngredientProcessorMap = new HashMap<>();
        blendIngredientProcessorMap.put(IngredientCode.IRON, new viteezy.service.blend.v2.IronBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.VITAMIN_B_12, new viteezy.service.blend.v2.VitaminB12BlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.VITAMIN_C, new viteezy.service.blend.v2.VitaminCBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.VITAMIN_D, new viteezy.service.blend.v2.VitaminDBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.MAGNESIUM, new viteezy.service.blend.v2.MagnesiumBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.ZINC, new viteezy.service.blend.v2.ZincBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.FISH_OIL, new viteezy.service.blend.v2.FishOilBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.OMEGA_THREE_VEGAN, new viteezy.service.blend.v2.OmegaThreeVeganBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.CRANBERRY, new viteezy.service.blend.v2.CranberryBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.GREEN_TEA_EXTRACT, new viteezy.service.blend.v2.GreenTeaExtractBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.SLEEP_FORMULA, new viteezy.service.blend.v2.SleepFormulaBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.ENERGY_FORMULA, new viteezy.service.blend.v2.EnergyFormulaBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.STRESS_FORMULA, new viteezy.service.blend.v2.StressFormulaBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.PRENATAL_MULTI_FORMULA, new viteezy.service.blend.v2.PrenatalMultiBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.DETOX_FORMULA, new viteezy.service.blend.v2.DetoxFormulaBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.HAIR_AND_NAILS_FORMULA, new viteezy.service.blend.v2.HairAndNailsFormulaBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.SKIN_FORMULA, new viteezy.service.blend.v2.SkinFormulaBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.LIBIDO_FORMULA, new viteezy.service.blend.v2.LibidoFormulaBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.GUT_SUPPORT, new viteezy.service.blend.v2.GutSupportBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.BRAIN_BOOST, new viteezy.service.blend.v2.BrainBoostBlendIngredientProcessor());
        blendIngredientProcessorMap.put(IngredientCode.HORMONE_CONTROL, new viteezy.service.blend.v2.HormoneControlBlendIngredientProcessor());
        return blendIngredientProcessorMap;
    }

    @Bean("blendIngredientService")
    public BlendIngredientService blendIngredientService(
            BlendIngredientRepository blendIngredientRepository, BlendRepository blendRepository,
            BlendIngredientPriceService blendIngredientPriceService, PricingService pricingService,
            CouponService couponService, PaymentPlanRepository paymentPlanRepository,
            IngredientService ingredientService, IngredientPriceService ingredientPriceService,
            LoggingService loggingService) {
        return new BlendIngredientServiceImpl(blendIngredientRepository, blendRepository, blendIngredientPriceService, pricingService,
                couponService, paymentPlanRepository, ingredientService, ingredientPriceService, loggingService,
                paymentConfiguration);
    }

    @Bean
    public QuizBlendComputeService quizBlendComputeService(BlendIngredientService blendIngredientService,
                                                           QuizService quizService, BlendService blendService,
                                                           BlendProcessorV2 blendProcessorV2,
                                                           QuizBlendPreviewServiceV2 quizBlendPreviewServiceV2,
                                                           PaymentPlanService paymentPlanService,
                                                           CustomerService customerService,
                                                           KlaviyoService klaviyoService) {
        return new QuizBlendComputeServiceImpl(blendIngredientService, quizService, blendService, blendProcessorV2,
                quizBlendPreviewServiceV2, paymentPlanService, customerService, klaviyoService);
    }
}
