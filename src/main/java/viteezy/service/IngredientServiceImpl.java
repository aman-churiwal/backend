package viteezy.service;

import com.google.common.base.Throwables;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.controller.dto.IngredientGetResponse;
import viteezy.controller.dto.dashboard.IngredientPostRequest;
import viteezy.db.IngredientRepository;
import viteezy.domain.ingredient.*;
import viteezy.service.pricing.IngredientPriceService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class IngredientServiceImpl implements IngredientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);
    private static final String CURRENCY = "EUR";
    private final IngredientRepository ingredientRepository;
    private final IngredientPriceService ingredientPriceService;
    private final IngredientUnitService ingredientUnitService;


    protected IngredientServiceImpl(IngredientRepository ingredientRepository,
                                    IngredientPriceService ingredientPriceService,
                                    IngredientUnitService ingredientUnitService) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientPriceService = ingredientPriceService;
        this.ingredientUnitService = ingredientUnitService;
    }

    @Override
    public Try<Ingredient> find(Long id) {
        return ingredientRepository.find(id)
                .onFailure(peekException());
    }

    @Override
    public Try<IngredientGetResponse> findWithComponentsAndContent(Long id) {
        return ingredientRepository.find(id)
                .flatMap(this::addIngredientComponents)
                .flatMap(this::addIngredientContent)
                .onFailure(peekException());
    }

    @Override
    public Try<List<Ingredient>> findAll() {
        return ingredientRepository.findAll()
                .onFailure(peekException());
    }

    @Override
    public Try<List<IngredientGetResponse>> findAllWithComponents() {
        return ingredientRepository.findAll()
                .map(ingredients -> ingredients.stream()
                        .map(this::addIngredientComponents)
                        .collect(Collectors.toList()))
                .flatMap(Try::sequence)
                .map(Seq::asJava)
                .onFailure(peekException());
    }

    @Override
    public Try<List<Ingredient>> findAdditional() {
        return ingredientRepository.findAll()
                .map(ingredients -> ingredients.stream()
                .filter(ingredient -> ingredient.getIsActive() && ingredient.getPriority() != null)
                .collect(Collectors.toList()))
                .onFailure(peekException());
    }

    @Override
    public Try<Ingredient> save(Ingredient ingredient) {
        return ingredientRepository
                .save(ingredient)
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Ingredient> save(IngredientPostRequest ingredientPostRequest) {
        return ingredientRepository.save(buildIngredient(ingredientPostRequest))
                .flatMap(ingredient -> ingredientPriceService.save(buildIngredientPrice(ingredient.getId(), ingredientPostRequest))
                .peek(ingredientPrice -> saveIngredientUnitIfNoSku(ingredientPostRequest, ingredientPrice))
                .map(__ -> ingredient))
                .onFailure(peekException());
    }

    @Override
    public Try<Ingredient> update(IngredientPostRequest ingredientPostRequest) {
        return ingredientRepository.find(ingredientPostRequest.getId())
                .flatMap(ingredient -> ingredientRepository.update(buildUpdatedIngredient(ingredient, ingredientPostRequest)))
                .onFailure(peekException());
    }

    private void saveIngredientUnitIfNoSku(IngredientPostRequest ingredientPostRequest, IngredientPrice ingredientPrice) {
        if (ingredientPostRequest.getSku().isEmpty() && ingredientPostRequest.getPharmacistCode().isPresent()
                && ingredientPostRequest.getPharmacistUnit().isPresent()
                && ingredientPostRequest.getPharmacistSize().isPresent()) {
            ingredientUnitService.save(buildIngredientUnit(ingredientPrice.getIngredientId(), ingredientPostRequest));
        } else {
            LOGGER.debug("IngredientUnit can't be saved={}", ingredientPostRequest);
        }
    }

    private Try<IngredientGetResponse> addIngredientComponents(Ingredient ingredient) {
        return ingredientRepository.findComponents(ingredient.getId())
                .map(ingredientComponents -> new IngredientGetResponse(ingredient.getId(), ingredient.getName(),
                        ingredient.getType(), ingredient.getDescription(), null,
                        ingredientComponents, ingredient.getCode(), ingredient.getUrl(), ingredient.getStrapiContentId(),
                        ingredient.getIsAFlavour(), ingredient.getIsVegan(), ingredient.getPriority(), ingredient.getIsActive(),
                        ingredient.getSku(), ingredient.getCreationTimestamp(), ingredient.getModificationTimestamp()));
    }

    private Try<IngredientGetResponse> addIngredientContent(IngredientGetResponse ingredient) {
        final List<IngredientArticle> ingredientArticles = ingredientRepository.findArticles(ingredient.getId())
                .getOrElse(Collections::emptyList);
        return ingredientRepository.findContent(ingredient.getId())
                .map(ingredientContent -> new IngredientGetResponse(ingredient.getId(), ingredient.getName(),
                        ingredient.getType(), ingredient.getDescription(),
                        buildIngredientContent(ingredientContent, ingredientArticles),
                        ingredient.getIngredientComponents(), ingredient.getCode(), ingredient.getUrl(),
                        ingredient.getStrapiContentId(), ingredient.isAFlavour(), ingredient.getVegan(),
                        ingredient.getPriority(), ingredient.isActive(), ingredient.getSku(),
                        ingredient.getCreationTimestamp(), ingredient.getModificationTimestamp()));
    }

    private IngredientContent buildIngredientContent(Optional<IngredientContent> optionalIngredientContent, List<IngredientArticle> ingredientArticles) {
        return optionalIngredientContent
                .map(ingredientContent -> new IngredientContent(
                        ingredientContent.getId(), ingredientContent.getIngredientId(),
                        ingredientContent.getDescription(), ingredientContent.getBullets(),
                        ingredientContent.getTitle1(), ingredientContent.getText1(), ingredientContent.getTitle2(),
                        ingredientContent.getText2(), ingredientContent.getTitle3(), ingredientContent.getText3(),
                        ingredientContent.getNotice(), ingredientArticles,
                        ingredientContent.getExcipients(), ingredientContent.getClaim(),
                        ingredientContent.getCreationTimestamp(), ingredientContent.getModificationTimestamp()))
                .orElse(null);
    }

    private Ingredient buildIngredient(IngredientPostRequest ingredientPostRequest) {
        final LocalDateTime now = LocalDateTime.now();
        return new Ingredient(null, ingredientPostRequest.getName(), ingredientPostRequest.getType(),
                ingredientPostRequest.getName().toLowerCase(), ingredientPostRequest.getName().toLowerCase(),
                ingredientPostRequest.getUrl(), ingredientPostRequest.getStrapiContentId().orElse(null),
                false, ingredientPostRequest.getVegan(), ingredientPostRequest.getPriority().orElse(null),
                ingredientPostRequest.getActive(), ingredientPostRequest.getSku().orElse(null), now, now);
    }

    private Ingredient buildUpdatedIngredient(Ingredient ingredient, IngredientPostRequest ingredientPostRequest) {
        final LocalDateTime now = LocalDateTime.now();
        return new Ingredient(ingredient.getId(), ingredientPostRequest.getName(), ingredientPostRequest.getType(),
                ingredient.getDescription(), ingredient.getCode(), ingredientPostRequest.getUrl(),
                ingredientPostRequest.getStrapiContentId().orElse(null), ingredient.getIsAFlavour(),
                ingredientPostRequest.getVegan(), ingredientPostRequest.getPriority().orElse(null),
                ingredientPostRequest.getActive(), ingredientPostRequest.getSku().orElse(null),
                ingredient.getCreationTimestamp(), now);
    }

    private IngredientPrice buildIngredientPrice(Long ingredientId, IngredientPostRequest ingredientPostRequest) {
        return new IngredientPrice(null, ingredientId, BigDecimal.TEN, UnitCode.MG, ingredientPostRequest.getPrice(), CURRENCY);
    }

    private IngredientUnit buildIngredientUnit(Long ingredientId, IngredientPostRequest ingredientPostRequest) {
        final LocalDateTime now = LocalDateTime.now();
        return new IngredientUnit(null, ingredientId, ingredientPostRequest.getPharmacistCode().get(),
                ingredientPostRequest.getPharmacistSize().get(), ingredientPostRequest.getPharmacistUnit().get(), now, now);
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}