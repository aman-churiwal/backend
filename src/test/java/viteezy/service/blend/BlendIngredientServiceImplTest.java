package viteezy.service.blend;

import io.vavr.control.Try;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.transaction.NoTransactionException;
import viteezy.configuration.PaymentConfiguration;
import viteezy.db.BlendIngredientRepository;
import viteezy.db.BlendRepository;
import viteezy.db.PaymentPlanRepository;
import viteezy.domain.*;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.ingredient.Ingredient;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.pricing.Pricing;
import viteezy.domain.blend.BlendStatus;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.service.IngredientService;
import viteezy.service.LoggingService;
import viteezy.service.pricing.CouponService;
import viteezy.service.pricing.IngredientPriceService;
import viteezy.service.pricing.PricingService;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class BlendIngredientServiceImplTest {

    private static final Long ID = 1L;
    private static final Long BLEND_ID = 1L;
    private static final Long CUSTOMER_ID = 1L;
    private static final Long QUIZ_ID = 1L;
    private static final Long INGREDIENT_ID = 1L;
    private static final BigDecimal AMOUNT = new BigDecimal("10");
    private static final String IS_UNIT = "t";
    private static final UUID EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final BlendIngredient SAMPLE_ENTITY = new BlendIngredient(ID, INGREDIENT_ID, BLEND_ID, AMOUNT, IS_UNIT, null, null, null, LocalDateTime.now(), LocalDateTime.now());
    private static final String NAME = "NAME";
    private static final String CODE = "CODE";
    private static final String URL = "URL";
    private static final String TYPE = "TYPE";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final Boolean IS_A_FLAVOUR = false;
    private static final Boolean IS_ACTIVE = false;
    private static final String SKU = "SKU";
    private static final Integer PRIORITY = 1;
    private static final Ingredient INGREDIENT_ENTITY = new Ingredient(INGREDIENT_ID, NAME, TYPE, DESCRIPTION, CODE, URL, null, IS_A_FLAVOUR, true, PRIORITY, IS_ACTIVE, SKU, LocalDateTime.now(), LocalDateTime.now());
    private static final Blend BLEND = new Blend(BLEND_ID, BlendStatus.FINISHED, EXTERNAL_REFERENCE, CUSTOMER_ID, QUIZ_ID, LocalDateTime.now(), LocalDateTime.now());
    private static final BigDecimal FIRST_AMOUNT = new BigDecimal("10");
    private static final BigDecimal RECURRING_AMOUNT = new BigDecimal("10");
    private static final Pricing PRICING = new Pricing(FIRST_AMOUNT, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, FIRST_AMOUNT, RECURRING_AMOUNT);
    private static final Logging LOGGING = new Logging(ID, CUSTOMER_ID, LoggingEvent.INGREDIENT_REMOVED, INGREDIENT_ENTITY.getName() + " verwijderd", LocalDateTime.now());
    private static final Integer RECURRING_MONTHS = 1;
    private static final PaymentPlan PAYMENT_PLAN = new PaymentPlan(ID, FIRST_AMOUNT, RECURRING_AMOUNT, RECURRING_MONTHS,
            1L ,BLEND_ID, EXTERNAL_REFERENCE, LocalDateTime.now(), LocalDateTime.now(),
            PaymentPlanStatus.ACTIVE, LocalDateTime.now(), null, LocalDateTime.now(), Optional.empty(),
            Optional.empty(), null);

    private final BlendIngredientRepository repository = Mockito.mock(BlendIngredientRepository.class);
    private final BlendRepository blendRepository = Mockito.mock(BlendRepository.class);
    private final BlendIngredientPriceService blendIngredientPriceService = Mockito.mock(BlendIngredientPriceService.class);
    private final PricingService pricingService = Mockito.mock(PricingService.class);
    private final CouponService couponService = Mockito.mock(CouponService.class);
    private final PaymentPlanRepository paymentPlanRepository = Mockito.mock(PaymentPlanRepository.class);
    private final IngredientService ingredientService = Mockito.mock(IngredientService.class);
    private final IngredientPriceService ingredientPriceService = Mockito.mock(IngredientPriceService.class);
    private final PaymentConfiguration paymentConfiguration = Mockito.mock(PaymentConfiguration.class);
    private final LoggingService loggingService = Mockito.mock(LoggingService.class);
    private BlendIngredientService service;

    @BeforeEach
    void setUp() {
        this.service = new BlendIngredientServiceImpl(
                repository,
                blendRepository,
                blendIngredientPriceService,
                pricingService,
                couponService,
                paymentPlanRepository,
                ingredientService,
                ingredientPriceService,
                loggingService,
                paymentConfiguration);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
                repository,
                blendRepository,
                blendIngredientPriceService,
                pricingService,
                couponService,
                paymentPlanRepository,
                ingredientService,
                ingredientPriceService,
                loggingService);

        reset(
                repository,
                blendRepository,
                blendIngredientPriceService,
                pricingService,
                couponService,
                paymentPlanRepository,
                ingredientService,
                ingredientPriceService,
                loggingService);
    }

    @Test
    void find() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Try<BlendIngredient> either = service.find(ID);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findException() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        Try<BlendIngredient> either = service.find(ID);
        Assertions.assertTrue(either.isFailure());
        Assertions.assertEquals(EXCEPTION, either.getCause());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findByBlendId() {
        Mockito.when(repository.findByBlendId(BLEND_ID))
                .thenReturn(Try.success(Collections.singletonList(SAMPLE_ENTITY)));

        Try<List<BlendIngredient>> either = service.findByBlendId(BLEND_ID);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertFalse(either.get().isEmpty());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get().get(0));

        Mockito.verify(repository).findByBlendId(BLEND_ID);
    }

    @Test
    void findByBlendIdEmptyList() {
        Mockito.when(repository.findByBlendId(BLEND_ID))
                .thenReturn(Try.of(Collections::emptyList));

        Try<List<BlendIngredient>> either = service.findByBlendId(BLEND_ID);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertTrue(either.get().isEmpty());

        Mockito.verify(repository).findByBlendId(BLEND_ID);
    }

    @Test
    void findByBlendIdException() {
        Mockito.when(repository.findByBlendId(BLEND_ID))
                .thenReturn(Try.failure(EXCEPTION));

        Try<List<BlendIngredient>> either = service.findByBlendId(BLEND_ID);
        Assertions.assertTrue(either.isFailure());
        Assertions.assertEquals(EXCEPTION, either.getCause());

        Mockito.verify(repository).findByBlendId(BLEND_ID);
    }

    @Test
    void findByBlendExternalReference() {
        Mockito.when(repository.findByBlendExternalReference(EXTERNAL_REFERENCE))
                .thenReturn(Try.success(Collections.singletonList(SAMPLE_ENTITY)));

        Try<List<BlendIngredient>> either = service.findByBlendExternalReference(EXTERNAL_REFERENCE);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get().get(0));

        Mockito.verify(repository).findByBlendExternalReference(EXTERNAL_REFERENCE);
    }

    @Test
    void findByBlendExternalReferenceEmptyList() {
        Mockito.when(repository.findByBlendExternalReference(EXTERNAL_REFERENCE))
                .thenReturn(Try.of(Collections::emptyList));

        Try<List<BlendIngredient>> either = service.findByBlendExternalReference(EXTERNAL_REFERENCE);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertTrue(either.get().isEmpty());

        Mockito.verify(repository).findByBlendExternalReference(EXTERNAL_REFERENCE);
    }

    @Test
    void findByBlendExternalReferenceException() {
        Mockito.when(repository.findByBlendExternalReference(EXTERNAL_REFERENCE))
                .thenReturn(Try.failure(EXCEPTION));

        Try<List<BlendIngredient>> either = service.findByBlendExternalReference(EXTERNAL_REFERENCE);
        Assertions.assertTrue(either.isFailure());
        Assertions.assertEquals(EXCEPTION, either.getCause());

        Mockito.verify(repository).findByBlendExternalReference(EXTERNAL_REFERENCE);
    }

    @Test
    void save() {
        Mockito.when(paymentPlanRepository.findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(PAYMENT_PLAN));
        Mockito.when(repository.save(SAMPLE_ENTITY))
                .thenReturn(Try.success(ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Mockito.when(ingredientService.find(INGREDIENT_ID))
                .thenReturn(Try.of(() -> INGREDIENT_ENTITY));

        Mockito.when(couponService.findCouponUsedByPaymentPlan(PAYMENT_PLAN.id()))
                .thenReturn(Try.of(Optional::empty));
        Mockito.when(blendRepository.find(PAYMENT_PLAN.blendId()))
                .thenReturn(Try.of(() -> BLEND));
        Mockito.when(pricingService.getBlendPricing(BLEND.getExternalReference(), Optional.empty(), null, PAYMENT_PLAN.recurringMonths(), true))
                .thenReturn(Try.of(() -> PRICING));

        Mockito.when(paymentPlanRepository.update(Mockito.any(PaymentPlan.class)))
                .thenReturn(Try.of(() -> PAYMENT_PLAN));
        Mockito.when(loggingService.create(PAYMENT_PLAN.customerId(), LoggingEvent.INGREDIENT_ADDED, INGREDIENT_ENTITY.getName() + " toegevoegd"))
                .thenReturn(Try.of(() -> LOGGING));

        Try<BlendIngredient> either = service.save(SAMPLE_ENTITY);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get());

        Mockito.verify(paymentPlanRepository, Mockito.times(4)).findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE);
        Mockito.verify(repository).save(SAMPLE_ENTITY);
        Mockito.verify(repository).find(ID);
        Mockito.verify(ingredientService).find(INGREDIENT_ID);
        Mockito.verify(couponService).findCouponUsedByPaymentPlan(PAYMENT_PLAN.id());
        Mockito.verify(blendRepository).find(PAYMENT_PLAN.blendId());
        Mockito.verify(pricingService).getBlendPricing(BLEND.getExternalReference(), Optional.empty(), null, PAYMENT_PLAN.recurringMonths(), true);
        Mockito.verify(paymentPlanRepository).update(Mockito.any(PaymentPlan.class));
        Mockito.verify(loggingService).create(PAYMENT_PLAN.customerId(), LoggingEvent.INGREDIENT_ADDED, MessageFormat.format("Bundel of quiz aanpassing heeft {0} toegevoegd",INGREDIENT_ENTITY.getName()));
    }

    @Test
    void saveExceptionAtSave() {
        Mockito.when(paymentPlanRepository.findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(PAYMENT_PLAN));
        Mockito.when(repository.save(SAMPLE_ENTITY))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.save(SAMPLE_ENTITY));

        Mockito.verify(paymentPlanRepository, Mockito.times(2)).findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE);
        Mockito.verify(repository).save(SAMPLE_ENTITY);
        Mockito.verify(repository, Mockito.times(0)).find(ID);
    }

    @Test
    void saveExceptionAtAddingPrice() {
        Mockito.when(repository.save(SAMPLE_ENTITY))
                .thenReturn(Try.failure(EXCEPTION));

        Mockito.when(paymentPlanRepository.findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(PAYMENT_PLAN));

        Assertions.assertThrows(NoTransactionException.class, () -> service.save(SAMPLE_ENTITY));

        Mockito.verify(repository).save(SAMPLE_ENTITY);
        Mockito.verify(paymentPlanRepository, Mockito.times(2)).findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE);
    }

    @Test
    void saveExceptionAtRetrieve() {
        Mockito.when(paymentPlanRepository.findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(PAYMENT_PLAN));
        Mockito.when(repository.save(SAMPLE_ENTITY))
                .thenReturn(Try.of(() -> ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.save(SAMPLE_ENTITY));

        Mockito.verify(paymentPlanRepository, Mockito.times(2)).findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE);
        Mockito.verify(repository).save(SAMPLE_ENTITY);
        Mockito.verify(repository).find(ID);
    }

    @Test
    void update() {
        Mockito.when(repository.findByBlendIdAndIngredientId(BLEND_ID, INGREDIENT_ID)).thenReturn(Try.success(SAMPLE_ENTITY));
        Mockito.when(blendIngredientPriceService.addPrice(SAMPLE_ENTITY)).thenReturn(Try.success(SAMPLE_ENTITY));
        Mockito.when(repository.update(SAMPLE_ENTITY))
                .thenReturn(Try.success(ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Mockito.when(paymentPlanRepository.findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(PAYMENT_PLAN));
        Mockito.when(ingredientService.find(INGREDIENT_ID))
                .thenReturn(Try.of(() -> INGREDIENT_ENTITY));

        Mockito.when(couponService.findCouponUsedByPaymentPlan(PAYMENT_PLAN.id()))
                .thenReturn(Try.of(Optional::empty));
        Mockito.when(blendRepository.find(PAYMENT_PLAN.blendId()))
                .thenReturn(Try.of(() -> BLEND));
        Mockito.when(pricingService.getBlendPricing(BLEND.getExternalReference(), Optional.empty(), null, PAYMENT_PLAN.recurringMonths(), true))
                .thenReturn(Try.of(() -> PRICING));

        Mockito.when(paymentPlanRepository.update(Mockito.any(PaymentPlan.class)))
                .thenReturn(Try.of(() -> PAYMENT_PLAN));
        Mockito.when(loggingService.create(PAYMENT_PLAN.customerId(), LoggingEvent.INGREDIENT_UPDATED, INGREDIENT_ENTITY.getName() + " aangepast"))
                .thenReturn(Try.of(() -> LOGGING));

        Try<BlendIngredient> either = service.update(SAMPLE_ENTITY);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get());

        Mockito.verify(repository).findByBlendIdAndIngredientId(BLEND_ID, INGREDIENT_ID);
        Mockito.verify(blendIngredientPriceService).addPrice(SAMPLE_ENTITY);
        Mockito.verify(repository).update(SAMPLE_ENTITY);
        Mockito.verify(repository).find(ID);
        Mockito.verify(paymentPlanRepository, Mockito.times(2)).findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE);
        Mockito.verify(ingredientService).find(INGREDIENT_ID);
        Mockito.verify(couponService).findCouponUsedByPaymentPlan(PAYMENT_PLAN.id());
        Mockito.verify(blendRepository).find(PAYMENT_PLAN.blendId());
        Mockito.verify(pricingService).getBlendPricing(BLEND.getExternalReference(), Optional.empty(), null, PAYMENT_PLAN.recurringMonths(), true);
        Mockito.verify(paymentPlanRepository).update(Mockito.any(PaymentPlan.class));
        Mockito.verify(loggingService).create(PAYMENT_PLAN.customerId(), LoggingEvent.INGREDIENT_UPDATED, INGREDIENT_ENTITY.getName() + " aangepast");
    }

    @Test
    void updateExceptionAtSave() {
        Mockito.when(repository.findByBlendIdAndIngredientId(BLEND_ID, INGREDIENT_ID)).thenReturn(Try.success(SAMPLE_ENTITY));
        Mockito.when(blendIngredientPriceService.addPrice(SAMPLE_ENTITY)).thenReturn(Try.success(SAMPLE_ENTITY));
        Mockito.when(repository.update(SAMPLE_ENTITY))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.update(SAMPLE_ENTITY));

        Mockito.verify(repository).findByBlendIdAndIngredientId(BLEND_ID, INGREDIENT_ID);
        Mockito.verify(blendIngredientPriceService).addPrice(SAMPLE_ENTITY);
        Mockito.verify(repository).update(SAMPLE_ENTITY);
        Mockito.verify(repository, Mockito.times(0)).find(ID);
    }

    @Test
    void updateExceptionAtAddingPrice() {
        final IndexOutOfBoundsException indexOutOfBoundsException = new IndexOutOfBoundsException();
        Mockito.when(repository.findByBlendIdAndIngredientId(BLEND_ID, INGREDIENT_ID)).thenReturn(Try.success(SAMPLE_ENTITY));
        Mockito.when(blendIngredientPriceService.addPrice(SAMPLE_ENTITY)).thenReturn(Try.failure(indexOutOfBoundsException));
        Mockito.when(repository.update(SAMPLE_ENTITY))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.update(SAMPLE_ENTITY));

        Mockito.verify(repository).findByBlendIdAndIngredientId(BLEND_ID, INGREDIENT_ID);
        Mockito.verify(blendIngredientPriceService).addPrice(SAMPLE_ENTITY);
    }

    @Test
    void updateExceptionAtRetrieve() {
        Mockito.when(repository.findByBlendIdAndIngredientId(BLEND_ID, INGREDIENT_ID)).thenReturn(Try.success(SAMPLE_ENTITY));
        Mockito.when(blendIngredientPriceService.addPrice(SAMPLE_ENTITY)).thenReturn(Try.success(SAMPLE_ENTITY));
        Mockito.when(repository.update(SAMPLE_ENTITY))
                .thenReturn(Try.of(() -> ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.update(SAMPLE_ENTITY));

        Mockito.verify(repository).findByBlendIdAndIngredientId(BLEND_ID, INGREDIENT_ID);
        Mockito.verify(blendIngredientPriceService).addPrice(SAMPLE_ENTITY);
        Mockito.verify(repository).update(SAMPLE_ENTITY);
        Mockito.verify(repository).find(ID);
    }

    @Test
    void delete() {
        Mockito.when(paymentPlanRepository.findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(PAYMENT_PLAN));
        Mockito.when(repository.delete(BLEND_ID, INGREDIENT_ID))
                .thenReturn(Try.success(null));

        Mockito.when(repository.findByBlendId(BLEND_ID))
                .thenReturn(Try.of(() -> Collections.singletonList(SAMPLE_ENTITY)));
        Mockito.when(ingredientService.find(INGREDIENT_ID))
                .thenReturn(Try.of(() -> INGREDIENT_ENTITY));

        Mockito.when(couponService.findCouponUsedByPaymentPlan(PAYMENT_PLAN.id()))
                .thenReturn(Try.of(Optional::empty));
        Mockito.when(blendRepository.find(PAYMENT_PLAN.blendId()))
                .thenReturn(Try.of(() -> BLEND));
        Mockito.when(pricingService.getBlendPricing(BLEND.getExternalReference(), Optional.empty(), null, PAYMENT_PLAN.recurringMonths(), true))
                .thenReturn(Try.of(() -> PRICING));

        Mockito.when(paymentPlanRepository.update(Mockito.any(PaymentPlan.class)))
                .thenReturn(Try.of(() -> PAYMENT_PLAN));
        Mockito.when(loggingService.create(PAYMENT_PLAN.customerId(), LoggingEvent.INGREDIENT_REMOVED, INGREDIENT_ENTITY.getName() + " verwijderd"))
                .thenReturn(Try.of(() -> LOGGING));

        Try<Void> either = service.deleteIfNotInProcess(BLEND_ID, INGREDIENT_ID);
        Assertions.assertTrue(either.isSuccess());

        Mockito.verify(paymentPlanRepository, Mockito.times(4)).findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE);
        Mockito.verify(repository).delete(BLEND_ID, INGREDIENT_ID);
        Mockito.verify(ingredientService).find(INGREDIENT_ID);

        Mockito.verify(couponService).findCouponUsedByPaymentPlan(PAYMENT_PLAN.id());
        Mockito.verify(blendRepository).find(PAYMENT_PLAN.blendId());
        Mockito.verify(pricingService).getBlendPricing(BLEND.getExternalReference(), Optional.empty(), null, PAYMENT_PLAN.recurringMonths(), true);

        Mockito.verify(paymentPlanRepository).update(Mockito.any(PaymentPlan.class));
        Mockito.verify(loggingService).create(PAYMENT_PLAN.customerId(), LoggingEvent.INGREDIENT_REMOVED, INGREDIENT_ENTITY.getName() + " verwijderd");
    }

    @Test
    void deleteException() {
        Mockito.when(paymentPlanRepository.findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE))
                .thenReturn(Try.success(PAYMENT_PLAN));
        Mockito.when(repository.delete(BLEND_ID, INGREDIENT_ID))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.deleteIfNotInProcess(BLEND_ID, INGREDIENT_ID));

        Mockito.verify(paymentPlanRepository, Mockito.times(2)).findByBlendId(BLEND_ID, PaymentPlanStatus.ACTIVE);
        Mockito.verify(repository).delete(BLEND_ID, INGREDIENT_ID);
    }
}