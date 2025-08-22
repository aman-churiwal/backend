package viteezy.service;

import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.controller.dto.BundleGetResponse;
import viteezy.controller.dto.IngredientGetResponse;
import viteezy.db.BundleRepository;
import viteezy.domain.ingredient.IngredientPrice;
import viteezy.service.pricing.IngredientPriceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class BundleServiceImpl implements BundleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BundleService.class);

    private final IngredientService ingredientService;
    private final IngredientPriceService ingredientPriceService;
    private final BundleRepository bundleRepository;

    protected BundleServiceImpl(IngredientService ingredientService, IngredientPriceService ingredientPriceService,
                                BundleRepository bundleRepository) {
        this.ingredientService = ingredientService;
        this.ingredientPriceService = ingredientPriceService;
        this.bundleRepository = bundleRepository;
    }

    @Override
    public Try<List<BundleGetResponse>> findAllBundles() {
        final List<IngredientPrice> ingredientPrices = ingredientPriceService.findAllActive().get();
        return bundleRepository.findAllBundles()
                .map(bundles -> bundles.stream().map(product -> {
                    Try<List<IngredientGetResponse>> tryIngredientGetResponses = bundleRepository.find(product.getId())
                            .map(productIngredients -> productIngredients.stream()
                                    .map(productIngredient -> ingredientService.findWithComponentsAndContent(productIngredient.getIngredientId())
                            ).collect(Collectors.toList()))
                            .flatMap(Try::sequence)
                            .map(Seq::asJava);

            Try<BigDecimal> price = tryIngredientGetResponses.map(ingredientGetResponses -> ingredientGetResponses.stream()
                    .flatMap(ingredientGetResponse -> ingredientPrices.stream()
                            .filter(ingredientPrice -> ingredientGetResponse.getId().equals(ingredientPrice.getIngredientId()))
                            .map(IngredientPrice::getPrice)
                    ).reduce(BigDecimal.ZERO, BigDecimal::add));
            return new BundleGetResponse(product.getName(),product.getCategory(), product.getDescription(), price.get(), product.getCode(), product.getUrl(), product.isVegan(), product.isActive(), tryIngredientGetResponses.get());
        }).collect(Collectors.toList()));
    }
}
