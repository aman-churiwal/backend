package viteezy.service.blend;

import io.vavr.control.Try;
import viteezy.domain.blend.BlendIngredient;

public interface BlendIngredientPriceService {
    Try<BlendIngredient> addPrice(BlendIngredient blendIngredient);
}
