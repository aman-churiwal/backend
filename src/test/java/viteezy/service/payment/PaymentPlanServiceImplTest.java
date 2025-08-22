package viteezy.service.payment;

import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.configuration.PaymentConfiguration;
import viteezy.db.PaymentPlanRepository;
import viteezy.db.PaymentRepository;
import viteezy.domain.blend.Blend;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.blend.BlendStatus;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.CustomerService;
import viteezy.service.LoggingService;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.blend.BlendService;
import viteezy.service.mail.EmailService;
import viteezy.service.pricing.CouponService;
import viteezy.service.pricing.PricingService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentPlanServiceImplTest {

    private static final Long QUIZ_ID = 2L;
    private static final Long BLEND_ID = 3L;
    private static final Long CUSTOMER_ID = 4L;
    private static final Long PAYMENT_PLAN_ID = 5L;
    private static final UUID BLEND_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final Integer MONTHS_SUBSCRIBED = 1;
    private static final BigDecimal AMOUNT_VALUE = BigDecimal.valueOf(20L);
    private static final Blend BLEND = new Blend(BLEND_ID, BlendStatus.CREATED, BLEND_EXTERNAL_REFERENCE, CUSTOMER_ID, QUIZ_ID, LocalDateTime.now(), LocalDateTime.now());
    private static final OffsetDateTime NOW = OffsetDateTime.now();
    private static final LocalDateTime NOW_LD = NOW.toLocalDateTime();
    private final BlendService blendService = mock(BlendService.class);
    private final BlendIngredientService blendIngredientService = mock(BlendIngredientService.class);
    private final CustomerService customerService = mock(CustomerService.class);
    private final PaymentConfiguration paymentConfiguration = mock(PaymentConfiguration.class);
    private final PaymentPlanRepository paymentPlanRepository = mock(PaymentPlanRepository.class);
    private final PaymentRepository paymentRepository = mock(PaymentRepository.class);
    private final CouponService couponService = mock(CouponService.class);
    private final EmailService emailService = Mockito.mock(EmailService.class);
    private final KlaviyoService klaviyoService = Mockito.mock(KlaviyoService.class);
    private final PricingService pricingService = Mockito.mock(PricingService.class);
    private final LoggingService loggingService = Mockito.mock(LoggingService.class);
    private PaymentPlanService paymentPlanService;

    @BeforeEach
    void setUp() {
        paymentPlanService = new PaymentPlanServiceImpl(
                blendService,
                blendIngredientService,
                customerService,
                paymentConfiguration,
                paymentPlanRepository,
                paymentRepository,
                couponService,
                emailService,
                klaviyoService,
                pricingService,
                loggingService);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
                blendService,
                customerService,
                paymentConfiguration,
                paymentPlanRepository,
                paymentRepository,
                couponService,
                emailService,
                klaviyoService,
                pricingService,
                loggingService
        );

        reset(
                blendService,
                customerService,
                paymentConfiguration,
                paymentPlanRepository,
                paymentRepository,
                couponService,
                emailService,
                klaviyoService,
                pricingService,
                loggingService
        );
    }

    @Test
    void findActivePaymentPlanByBlendExternalReferenceOk() {
        PaymentPlan paymentPlan = buildPaymentPlan(PAYMENT_PLAN_ID, AMOUNT_VALUE, MONTHS_SUBSCRIBED);

        when(blendService.find(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND));
        when(paymentPlanRepository.findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(paymentPlan));

        Try<PaymentPlan> paymentPlanTry = paymentPlanService.findActivePaymentPlanByBlendExternalReference(BLEND_EXTERNAL_REFERENCE);
        assertEquals(paymentPlanTry, Try.success(paymentPlan));

        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);
        verify(paymentPlanRepository).findByBlendId(BLEND.getId(), PaymentPlanStatus.ACTIVE);
    }
    private PaymentPlan buildPaymentPlan(Long paymentPlanId, BigDecimal amountValue, int recurringMonths) {
        return new PaymentPlan(paymentPlanId, amountValue, amountValue, recurringMonths, CUSTOMER_ID, BLEND_ID, UUID.randomUUID(),
                NOW_LD, NOW_LD, PaymentPlanStatus.PENDING, NOW_LD, null, NOW_LD, Optional.empty(), Optional.empty(), null);
    }
}