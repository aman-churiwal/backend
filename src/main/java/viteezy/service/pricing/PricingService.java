package viteezy.service.pricing;

import io.vavr.control.Try;
import viteezy.domain.pricing.Pricing;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PricingService {

    Try<Pricing> getBlendPricing(UUID blendExternalReference, Optional<List<Long>> ingredientIds, String couponCode, Integer monthsSubscribed, Boolean isSubscription);
}
