package viteezy.service.blend;

import io.vavr.control.Try;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.ingredient.IngredientPrice;
import viteezy.domain.ingredient.UnitCode;
import viteezy.service.pricing.IngredientPriceService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlendIngredientPriceServiceImpl implements BlendIngredientPriceService {

    private final IngredientPriceService ingredientPriceService;

    protected BlendIngredientPriceServiceImpl(IngredientPriceService ingredientPriceService) {
        this.ingredientPriceService = ingredientPriceService;
    }

    @Override
    public Try<BlendIngredient> addPrice(BlendIngredient blendIngredient) {
        return ingredientPriceService.findAllActive()
                .map(filterPricesForCurrentBlendIngredient(blendIngredient))
                .flatMap(hitOrMissSinglePrice())
                .map(buildNewBlendIngredientObject(blendIngredient));
    }

    private Function<IngredientPrice, BlendIngredient> buildNewBlendIngredientObject(BlendIngredient blendIngredient) {
        return ingredientPrice -> BlendIngredient.addPrice(blendIngredient, ingredientPrice.getPrice(), ingredientPrice.getCurrency());
    }

    private Function<List<IngredientPrice>, Try<IngredientPrice>> hitOrMissSinglePrice() {
        return ingredientPrices -> {
            if (ingredientPrices.size() == 1) {
                return Try.of(() -> ingredientPrices.get(0));
            } else {
                return Try.failure(new IndexOutOfBoundsException());
            }
        };
    }

    private Function<List<IngredientPrice>, List<IngredientPrice>> filterPricesForCurrentBlendIngredient(BlendIngredient blendIngredient) {
        return ingredientPrices -> ingredientPrices.stream()
                .filter(ingredientPrice -> ingredientPrice.getIngredientId().equals(blendIngredient.getIngredientId()))
                .filter(ingredientPrice -> !UnitCode.UNITLESS.equals(ingredientPrice.getInternationalSystemUnit()))
                .collect(Collectors.toList());
    }

}
