package viteezy.service.blend;

import com.google.common.base.Throwables;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.BlendRepository;
import viteezy.db.BundleRepository;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.ProductIngredient;
import viteezy.domain.ingredient.IngredientPrice;
import viteezy.domain.blend.BlendStatus;
import viteezy.service.pricing.IngredientPriceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BlendServiceImpl implements BlendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlendService.class);
    private final IngredientPriceService ingredientPriceService;
    private final BlendIngredientService blendIngredientService;
    private final BlendIngredientPriceService blendIngredientPriceService;
    private final BlendRepository blendRepository;
    private final BundleRepository bundleRepository;

    protected BlendServiceImpl(IngredientPriceService ingredientPriceService,
                               BlendIngredientService blendIngredientService, BlendIngredientPriceService blendIngredientPriceService,
                               BlendRepository blendRepository, BundleRepository bundleRepository) {
        this.ingredientPriceService = ingredientPriceService;
        this.blendIngredientService = blendIngredientService;
        this.blendIngredientPriceService = blendIngredientPriceService;
        this.blendRepository = blendRepository;
        this.bundleRepository = bundleRepository;
    }

    @Override
    public Try<Blend> find(Long id) {
        return blendRepository.find(id);
    }

    @Override
    public Try<Blend> find(UUID externalReference) {
        return blendRepository.find(externalReference);
    }

    @Override
    public Try<Blend> findByCustomerExternalReference(UUID customerExternalReference) {
        return blendRepository.findByCustomerExternalReference(customerExternalReference);
    }

    @Override
    public Try<List<Blend>> findAllByCustomerExternalReference(UUID customerExternalReference) {
        return blendRepository.findAllByCustomerExternalReference(customerExternalReference);
    }

    @Override
    public Try<Optional<Blend>> findByQuizId(Long quizId) {
        return blendRepository.findByQuizId(quizId);
    }

    @Override
    public Try<Blend> create(Long customerId, Long quizId, BlendStatus blendStatus) {
        Blend blend = buildBlend(customerId, quizId, blendStatus);
        return blendRepository.save(blend)
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Blend> update(Blend blend) {
        return blendRepository.update(blend)
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Blend> updateStatus(Long id, BlendStatus blendStatus) {
        return blendRepository.updateStatus(id, blendStatus)
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Blend> createBundle(String bundleCode) {
        return bundleRepository.find(bundleCode)
                .flatMap(product -> create(null, null, BlendStatus.BUNDLE)
                        .flatMap(blend -> bundleRepository.find(product.getId())
                                .map(productIngredients -> productIngredients.stream().map(ProductIngredient::getIngredientId).collect(Collectors.toList()))
                                .flatMap(ingredientIdList -> ingredientPriceService.findAllActive()
                                        .map(ingredientPrices -> ingredientPrices.stream()
                                                .filter(ingredientPrice -> ingredientIdList.contains(ingredientPrice.getIngredientId()))
                                                .collect(Collectors.toList())))
                                .map(ingredientPrices -> ingredientPrices.stream()
                                        .map(ingredientPrice -> matchAmountAndUnitValuesWithPrice(buildBlendIngredient(blend.getId(), ingredientPrice))
                                                .flatMap(blendIngredientService::save))
                                        .collect(Collectors.toList()))
                                .flatMap(Try::sequence)
                                .map(__ -> blend)
                                .onFailure(peekException()))
                        .onFailure(rollbackTransaction()));
    }

    private Blend buildBlend(Long customerId, Long quizId, BlendStatus blendStatus) {
        return new Blend(null, blendStatus, UUID.randomUUID(), customerId, quizId, LocalDateTime.now(), LocalDateTime.now());
    }

    private BlendIngredient buildBlendIngredient(Long blendId, IngredientPrice ingredientPrice) {
        return BlendIngredient.buildV2(ingredientPrice.getIngredientId(), blendId, ingredientPrice.getAmount(), ingredientPrice.getInternationalSystemUnit(), null, false, null);
    }

    private Try<BlendIngredient> matchAmountAndUnitValuesWithPrice(BlendIngredient blendIngredient) {
        return blendIngredientPriceService.addPrice(blendIngredient);
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}