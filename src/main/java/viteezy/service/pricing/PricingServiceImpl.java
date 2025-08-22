package viteezy.service.pricing;

import com.google.common.base.Throwables;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.BlendIngredientRepository;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.ingredient.Ingredient;
import viteezy.domain.pricing.Coupon;
import viteezy.domain.pricing.Pricing;
import viteezy.domain.pricing.ReferralDiscount;
import viteezy.domain.ingredient.UnitCode;
import viteezy.service.IngredientService;
import viteezy.service.blend.BlendIngredientPriceService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PricingServiceImpl implements PricingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingService.class);

    private final IngredientService ingredientService;
    private final BlendIngredientPriceService blendIngredientPriceService;
    private final BlendIngredientRepository blendIngredientRepository;
    private final CouponService couponService;
    private final ReferralService referralService;
    private final ShippingService shippingService;

    private final ReferralDiscount referralDiscount;

    private static final Integer THREE_MONTHS_RECURRING = 3;
    private static final BigDecimal THREE_MONTHS_DISCOUNT = new BigDecimal("0.15");
    private static final BigDecimal SINGLE_BUY_FEE = new BigDecimal("0.18");

    protected PricingServiceImpl(IngredientService ingredientService,
                                 BlendIngredientPriceService blendIngredientPriceService,
                                 BlendIngredientRepository blendIngredientRepository, CouponService couponService,
                                 ReferralService referralService, ShippingService shippingService) {
        this.ingredientService = ingredientService;
        this.blendIngredientPriceService = blendIngredientPriceService;
        this.blendIngredientRepository = blendIngredientRepository;
        this.couponService = couponService;
        this.referralService = referralService;
        this.shippingService = shippingService;
        this.referralDiscount = referralService.getReferralDiscount();
    }

    @Override
    public Try<Pricing> getBlendPricing(UUID blendExternalReference, Optional<List<Long>> optionalIngredientIds,
                                        String couponCode, Integer monthsSubscribed, Boolean isSubscription) {
        final Integer recurringMonths = monthsSubscribed > 1 ? monthsSubscribed : 1;
        final List<BlendIngredient> ingredients;
        final List<BlendIngredient> currentIngredients = blendIngredientRepository.findWithoutSkuByBlendExternalReference(blendExternalReference)
                .map(blendIngredients -> blendIngredients.stream()
                        .filter(blendIngredient -> !UnitCode.UNITLESS.equals(blendIngredient.getIsUnit()))
                        .collect(Collectors.toList()))
                .getOrElse(Collections.emptyList());
        final List<Long> currentIngredientIds = currentIngredients.stream()
                .map(BlendIngredient::getIngredientId)
                .toList();

        if (optionalIngredientIds.isPresent()) {
            ingredients = ingredientService.findAll()
                    .map(allIngredients -> allIngredients.stream()
                            .filter(ingredient -> optionalIngredientIds.get().contains(ingredient.getId()))
                            .map(this::buildBlendIngredient)
                            .map(blendIngredient -> {
                                if (currentIngredientIds.contains(blendIngredient.getIngredientId())) {
                                    return currentIngredients.stream()
                                            .filter(blendIngredient1 -> blendIngredient1.getIngredientId().equals(blendIngredient.getIngredientId()))
                                            .findAny()
                                            .get();
                                } else {
                                    return blendIngredientPriceService.addPrice(blendIngredient).get();
                                }
                            })
                            .collect(Collectors.toList()))
                    .onFailure(peekException())
                    .get();
        } else {
            ingredients = currentIngredients;
        }

        final List<BlendIngredient> skuIngredients = blendIngredientRepository.findSkuByBlendExternalReference(blendExternalReference)
                .map(blendIngredients -> blendIngredients.stream()
                        .filter(blendIngredient -> !UnitCode.UNITLESS.equals(blendIngredient.getIsUnit()))
                        .collect(Collectors.toList()))
                .getOrElse(Collections.emptyList());

        final BigDecimal oneMonthBlendPrice = ingredients.stream()
                .map(BlendIngredient::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal skuPrice = skuIngredients.stream()
                .map(BlendIngredient::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal basePrice = calculateAmountOfBlends(oneMonthBlendPrice, recurringMonths);
        final BigDecimal noSubscriptionFee = basePrice.multiply(SINGLE_BUY_FEE);
        final BigDecimal threeMonthPlanDiscount = basePrice.multiply(THREE_MONTHS_DISCOUNT);

        // calculate
        final BigDecimal calculatedFee = isSubscription != null && isSubscription ? BigDecimal.ZERO : noSubscriptionFee;
        final BigDecimal basePriceWithFee = basePrice.add(calculatedFee);

        final BigDecimal calculatedPlanDiscount = recurringMonths.equals(THREE_MONTHS_RECURRING) ? threeMonthPlanDiscount : BigDecimal.ZERO;
        final BigDecimal basePriceWithFeeAndPlanDiscount = basePriceWithFee.subtract(calculatedPlanDiscount);
        final BigDecimal basePriceWithFeeAndPlanDiscountWithSku = basePriceWithFeeAndPlanDiscount.add(skuPrice);

        final Tuple2<BigDecimal, BigDecimal> couponDiscount = getCouponDiscountAmount(couponCode, monthsSubscribed, basePriceWithFeeAndPlanDiscount, ingredients, skuIngredients);
        final BigDecimal couponDiscountFirstAmount = couponDiscount._1;
        final BigDecimal couponDiscountRecurringAmount = couponDiscount._2;
        final BigDecimal referralDiscount = getReferralDiscountAmount(couponCode, basePriceWithFeeAndPlanDiscount);

        final BigDecimal firstAmount = basePriceWithFeeAndPlanDiscountWithSku.subtract(couponDiscountFirstAmount).subtract(referralDiscount);
        final BigDecimal recurringAmount = basePriceWithFeeAndPlanDiscount.subtract(couponDiscountRecurringAmount);

        final BigDecimal shippingCostFirst = shippingService.getShippingCostForAmount(firstAmount);
        final BigDecimal shippingCostRecurring = shippingService.getShippingCostForAmount(recurringAmount);

        final BigDecimal firstAmountWithShipping = firstAmount.add(shippingCostFirst);
        final BigDecimal recurringAmountWithShipping = recurringAmount.add(shippingCostRecurring);

        return Try.of(() -> buildPricing(basePrice, noSubscriptionFee, threeMonthPlanDiscount, couponDiscountFirstAmount, referralDiscount, shippingCostFirst, firstAmountWithShipping, recurringAmountWithShipping));
    }

    private BlendIngredient buildBlendIngredient(Ingredient ingredient) {
        return new BlendIngredient(null, ingredient.getId(), null, null, null, null, null, null, null, null, LocalDateTime.now(), LocalDateTime.now());
    }

    private BigDecimal calculateAmountOfBlends(BigDecimal amount, Integer monthsSubscribed) {
        return monthsSubscribed > 1 ? amount.multiply(new BigDecimal(monthsSubscribed)) : amount;
    }

    public Tuple2<BigDecimal, BigDecimal> getCouponDiscountAmount(String code, Integer monthsSubscribed, BigDecimal amount, List<BlendIngredient> ingredients, List<BlendIngredient> skuIngredients) {
        if (StringUtils.isNotBlank(code)) {
            return couponService.findValid(code, monthsSubscribed, amount)
                    .map(coupon -> new Tuple2<>(getDiscountedAmount(amount, monthsSubscribed, coupon, ingredients, skuIngredients), coupon.getRecurring() ? getDiscountedAmount(amount, monthsSubscribed, coupon, ingredients, skuIngredients) : BigDecimal.ZERO))
                    .getOrElse(() -> new Tuple2<>(BigDecimal.ZERO, BigDecimal.ZERO));
        } else {
            return new Tuple2<>(BigDecimal.ZERO, BigDecimal.ZERO);
        }
    }

    public BigDecimal getReferralDiscountAmount(String code, BigDecimal amount) {
        if (StringUtils.isNotBlank(code)) {
            return referralService.findCustomerByReferralCode(code)
                    .filter(Optional::isPresent)
                    .filter(__ -> amount.compareTo(referralDiscount.getMinimumPrice()) > 0 )
                    .map(__ -> BigDecimal.valueOf(referralDiscount.getDiscountAmount()))
                    .getOrElse(() -> BigDecimal.ZERO);
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getDiscountedAmount(BigDecimal amount, Integer monthsSubscribed, Coupon coupon, List<BlendIngredient> ingredients, List<BlendIngredient> skuIngredients) {
        if (coupon.getRecurringTerms().isPresent()) {
            final List<String> recurringTermsList = new ArrayList<>(Arrays.asList(coupon.getRecurringTerms().get().split(",")));
            final BigDecimal firstRecurringTerm = recurringTermsList.stream()
                    .findFirst()
                    .map(BigDecimal::new)
                    .orElse(BigDecimal.ZERO);
            if (coupon.getPercentage()) {
                return getPercentageDiscountedAmount(amount.multiply(firstRecurringTerm), coupon.getMaximumAmount());
            } else {
                return firstRecurringTerm;
            }
        } else if (coupon.getIngredientId().isPresent()) {
            final Optional<BlendIngredient> discountedSkuIngredient = skuIngredients.stream()
                    .filter(blendIngredient -> coupon.getIngredientId().get().equals(blendIngredient.getIngredientId()))
                    .findFirst();
            final Optional<BlendIngredient> discountedIngredient = ingredients.stream()
                    .filter(blendIngredient -> coupon.getIngredientId().get().equals(blendIngredient.getIngredientId()))
                    .findFirst();

            if (discountedSkuIngredient.isPresent()) {
                return discountedSkuIngredient.map(BlendIngredient::getPrice).get();
            } else if (discountedIngredient.isPresent()) {
                return discountedIngredient.map(BlendIngredient::getPrice).get().multiply(BigDecimal.valueOf(monthsSubscribed));
            } else {
                return BigDecimal.ZERO;
            }
        } else if (coupon.getPercentage()) {
            return getPercentageDiscountedAmount(amount.multiply(coupon.getAmount()), coupon.getMaximumAmount());
        } else {
            return coupon.getAmount();
        }
    }

    private BigDecimal getPercentageDiscountedAmount(BigDecimal discount, BigDecimal maximumAmount) {
        if (isDiscountExceedingMaximum(maximumAmount, discount)) {
            return maximumAmount;
        } else {
            return discount;
        }
    }

    private boolean isDiscountExceedingMaximum(BigDecimal maximumAmount, BigDecimal discount) {
        return maximumAmount.compareTo(BigDecimal.ZERO) > 0 && maximumAmount.compareTo(discount) < 0;
    }

    private Pricing buildPricing(BigDecimal basePrice, BigDecimal noSubscriptionFee, BigDecimal threeMonthPlanDiscount,
                                 BigDecimal couponDiscount, BigDecimal referralDiscount, BigDecimal shippingCost,
                                 BigDecimal firstAmount, BigDecimal recurringAmount) {
        return new Pricing(basePrice, noSubscriptionFee, threeMonthPlanDiscount, couponDiscount, referralDiscount, shippingCost, firstAmount, recurringAmount);
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}