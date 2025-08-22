package viteezy.service.blend;

import com.google.common.base.Throwables;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.configuration.PaymentConfiguration;
import viteezy.db.BlendIngredientRepository;
import viteezy.db.BlendRepository;
import viteezy.db.PaymentPlanRepository;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.pricing.Coupon;
import viteezy.domain.LoggingEvent;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.ingredient.UnitCode;
import viteezy.domain.ingredient.Ingredient;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.service.IngredientService;
import viteezy.service.LoggingService;
import viteezy.service.pricing.CouponService;
import viteezy.service.pricing.IngredientPriceService;
import viteezy.service.pricing.PricingService;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlendIngredientServiceImpl implements BlendIngredientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlendIngredientService.class);
    private final BlendIngredientRepository blendIngredientRepository;
    private final BlendRepository blendRepository;
    private final BlendIngredientPriceService blendIngredientPriceService;
    private final PricingService pricingService;
    private final CouponService couponService;
    private final PaymentPlanRepository paymentPlanRepository;
    private final IngredientService ingredientService;
    private final IngredientPriceService ingredientPriceService;
    private final LoggingService loggingService;
    private final PaymentConfiguration paymentConfiguration;

    protected BlendIngredientServiceImpl(
            BlendIngredientRepository blendIngredientRepository, BlendRepository blendRepository,
            BlendIngredientPriceService blendIngredientPriceService, PricingService pricingService,
            CouponService couponService, PaymentPlanRepository paymentPlanRepository,
            IngredientService ingredientService, IngredientPriceService ingredientPriceService,
            LoggingService loggingService, PaymentConfiguration paymentConfiguration) {
        this.blendIngredientRepository = blendIngredientRepository;
        this.blendRepository = blendRepository;
        this.blendIngredientPriceService = blendIngredientPriceService;
        this.pricingService = pricingService;
        this.couponService = couponService;
        this.paymentPlanRepository = paymentPlanRepository;
        this.ingredientService = ingredientService;
        this.ingredientPriceService = ingredientPriceService;
        this.loggingService = loggingService;
        this.paymentConfiguration = paymentConfiguration;
    }

    @Override
    public Try<BlendIngredient> find(Long id) {
        return blendIngredientRepository.find(id);
    }

    @Override
    public Try<List<BlendIngredient>> findByBlendExternalReference(UUID externalReference) {
        return blendIngredientRepository.findByBlendExternalReference(externalReference)
                .map(blendIngredients -> blendIngredients.stream()
                        .filter(blendIngredient -> !UnitCode.UNITLESS.equals(blendIngredient.getIsUnit()))
                        .collect(Collectors.toList())
                );
    }

    @Override
    public Try<List<BlendIngredient>> findByBlendId(Long blendId) {
        return blendIngredientRepository.findByBlendId(blendId);
    }

    @Override
    public Try<BlendIngredient> save(Long blendId, Long ingredientId) {
        return ingredientPriceService.findActiveByIngredientId(ingredientId)
                .flatMap(ingredientPrice -> matchAmountAndUnitValuesWithPrice(buildBlendIngredient(blendId, ingredientId, ingredientPrice.getAmount(), ingredientPrice.getInternationalSystemUnit())))
                .filter(blendIngredient -> isNotActiveWithinPaymentDate(blendId), () -> new DuplicateKeyException("Payment plan is active and within payment date and delivery date"))
                .filter(blendIngredient -> isNotActiveWithinCreationDate(blendId), () -> new DuplicateKeyException("Payment plan is active and within first delivery"))
                .flatMap(blendIngredientRepository::save)
                .flatMap(retrieveById())
                .peek(__ -> updateBlendPayment(blendId))
                .peek(__ -> ingredientService.find(ingredientId)
                        .peek(ingredient -> logAction(blendId, LoggingEvent.INGREDIENT_ADDED, MessageFormat.format("Klant heeft {0} toegevoegd",ingredient.getName()))))
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<BlendIngredient> save(BlendIngredient blendIngredient) {
        return Try.success(blendIngredient)
                .filter(blendIngredient1 -> isNotActiveWithinPaymentDate(blendIngredient1.getBlendId()), () -> new DuplicateKeyException("Payment plan is active and within payment date and delivery date"))
                .filter(blendIngredient1 -> isNotActiveWithinCreationDate(blendIngredient1.getBlendId()), () -> new DuplicateKeyException("Payment plan is active and within first delivery"))
                .flatMap(blendIngredientRepository::save)
                .flatMap(retrieveById())
                .peek(__ -> updateBlendPayment(blendIngredient.getBlendId()))
                .peek(__ -> ingredientService.find(blendIngredient.getIngredientId())
                        .peek(ingredient -> logAction(blendIngredient.getBlendId(), LoggingEvent.INGREDIENT_ADDED, MessageFormat.format("Bundel of quiz aanpassing heeft {0} toegevoegd",ingredient.getName()))))
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Seq<Long>> saveHistory(List<BlendIngredient> blendIngredients) {
        final List<Try<Long>> eitherList = blendIngredients.stream()
                .map(blendIngredientRepository::saveHistoryBlendIngredient)
                .collect(Collectors.toList());
        return Try.sequence(eitherList);
    }

    @Override
    public Try<BlendIngredient> update(BlendIngredient blendIngredient) {
        return blendIngredientRepository.findByBlendIdAndIngredientId(blendIngredient.getBlendId(), blendIngredient.getIngredientId())
                .map(blendIngredientFromRepo -> mergeWithIncomingEntity(blendIngredientFromRepo, blendIngredient))
                .flatMap(this::matchAmountAndUnitValuesWithPrice)
                .flatMap(blendIngredientRepository::update)
                .flatMap(retrieveById())
                .peek(__ -> updateBlendPayment(blendIngredient.getBlendId()))
                .peek(__ -> ingredientService.find(blendIngredient.getIngredientId())
                        .peek(ingredient -> logAction(blendIngredient.getBlendId(), LoggingEvent.INGREDIENT_UPDATED, ingredient.getName() + " aangepast")))
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    private BlendIngredient mergeWithIncomingEntity(BlendIngredient blendIngredientFromRepo, BlendIngredient blendIngredient) {
        return new BlendIngredient(
                blendIngredientFromRepo.getId(), blendIngredientFromRepo.getIngredientId(),
                blendIngredientFromRepo.getBlendId(), blendIngredient.getAmount(), blendIngredient.getIsUnit(),
                blendIngredientFromRepo.getPrice(), blendIngredientFromRepo.getCurrency(), blendIngredientFromRepo.getExplanation(),
                blendIngredientFromRepo.getCreationTimestamp(), blendIngredientFromRepo.getModificationTimestamp()
        );
    }

    @Override
    public Try<Void> deleteBulk(Long blendId) {
        return blendIngredientRepository.deleteBulk(blendId)
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Void> delete(Long blendId, Long ingredientId) {
        return blendIngredientRepository.delete(blendId, ingredientId);
    }

    @Override
    public Try<Void> deleteIfNotInProcess(Long blendId, Long ingredientId) {
        if (isNotActiveWithinPaymentDate(blendId) && isNotActiveWithinCreationDate(blendId)) {
            return blendIngredientRepository.delete(blendId, ingredientId)
                    .peek(__ -> updateBlendPayment(blendId))
                    .peek(__ -> ingredientService.find(ingredientId).peek(ingredient -> logAction(blendId, LoggingEvent.INGREDIENT_REMOVED, ingredient.getName() + " verwijderd")))
                    .onFailure(peekException())
                    .onFailure(rollbackTransaction());
        } else {
            return Try.failure(new DuplicateKeyException("Payment plan is active and within payment date and delivery date or first delivery"));
        }
    }

    @Override
    public Try<Seq<Ingredient>> getIngredients(Long blendId) {
        return findByBlendId(blendId)
                .map(blendIngredients -> blendIngredients.stream()
                        .filter(blendIngredient -> !UnitCode.UNITLESS.equals(blendIngredient.getIsUnit()))
                        .map(blendIngredient -> ingredientService.find(blendIngredient.getIngredientId()))
                        .collect(Collectors.toList())
                )
                .flatMap(Try::sequence);
    }

    private void logAction(Long blendId, LoggingEvent loggingEvent, String info) {
        paymentPlanRepository.findByBlendId(blendId, PaymentPlanStatus.ACTIVE)
                .peek(paymentPlan -> loggingService.create(paymentPlan.customerId(), loggingEvent, info))
                .orElse(() -> paymentPlanRepository.findByBlendId(blendId, PaymentPlanStatus.STOPPED)
                        .peek(paymentPlan -> loggingService.create(paymentPlan.customerId(), loggingEvent, info)));
    }

    private Try<BlendIngredient> matchAmountAndUnitValuesWithPrice(BlendIngredient blendIngredient) {
        return blendIngredientPriceService.addPrice(blendIngredient);
    }

    private Function<Long, Try<BlendIngredient>> retrieveById() {
        return blendIngredientRepository::find;
    }

    private void updateBlendPayment(Long blendId) {
        paymentPlanRepository.findByBlendId(blendId, PaymentPlanStatus.ACTIVE)
                .peek(this::updateSubscription)
                .orElse(() -> paymentPlanRepository.findByBlendId(blendId, PaymentPlanStatus.STOPPED)
                        .peek(this::updateSubscription));
    }

    private void updateSubscription(PaymentPlan paymentPlan) {
        final String couponCode = couponService.findCouponUsedByPaymentPlan(paymentPlan.id())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(couponUsed -> couponService.find(couponUsed.getCouponId()))
                .map(Coupon::getCouponCode)
                .getOrElse(() -> null);

        blendRepository.find(paymentPlan.blendId())
                .flatMap(blend -> pricingService.getBlendPricing(blend.getExternalReference(), Optional.empty(), couponCode, paymentPlan.recurringMonths(), true))
                .flatMap(pricing -> paymentPlanRepository.update(buildPaymentPlan(paymentPlan, pricing.getRecurringAmount())));
    }

    private BlendIngredient buildBlendIngredient(Long blendId, Long ingredientId, BigDecimal amount, String isUnit) {
        return BlendIngredient.build(ingredientId, blendId, amount, isUnit, null);
    }

    private PaymentPlan buildPaymentPlan(PaymentPlan paymentPlan, BigDecimal recurringAmount) {
        return new PaymentPlan(paymentPlan.id(), paymentPlan.firstAmount(), recurringAmount,
                paymentPlan.recurringMonths(), paymentPlan.customerId(), paymentPlan.blendId(),
                paymentPlan.externalReference(), paymentPlan.creationDate(), paymentPlan.lastModified(),
                paymentPlan .status(), paymentPlan.paymentDate(), paymentPlan.stopReason(),
                paymentPlan.deliveryDate(), paymentPlan.nextPaymentDate(), paymentPlan.nextDeliveryDate(), paymentPlan.paymentMethod());
    }

    private boolean isNotActiveWithinPaymentDate(Long blendId) {
        final LocalDateTime now = LocalDateTime.now();
        return paymentPlanRepository.findByBlendId(blendId, PaymentPlanStatus.ACTIVE)
                .map(paymentPlan -> !(now.isAfter(paymentPlan.paymentDate().plusDays(1)) && now.isBefore(paymentPlan.deliveryDate())))
                .getOrElse(true);
    }

    private boolean isNotActiveWithinCreationDate(Long blendId) {
        final LocalDateTime now = LocalDateTime.now();
        return paymentPlanRepository.findByBlendId(blendId, PaymentPlanStatus.ACTIVE)
                .map(paymentPlan -> now.isAfter(paymentPlan.creationDate().plusDays(paymentConfiguration.getPaymentProcessingInDays() + paymentConfiguration.getDeliveryDateInDays())))
                .getOrElse(true);
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
