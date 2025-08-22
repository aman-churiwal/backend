package viteezy.service.blend;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import viteezy.db.IngredientRepository;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.QuizAggregatedInformationV2;
import viteezy.domain.quiz.code.AnswerCode;
import viteezy.domain.blend.BlendIngredientReasonCode;
import viteezy.domain.ingredient.UnitCode;
import viteezy.domain.quiz.code.VitaminIntakeCode;
import viteezy.domain.ingredient.Ingredient;
import viteezy.domain.quiz.VitaminIntake;
import viteezy.domain.quiz.VitaminIntakeAnswer;
import viteezy.service.blend.v2.BlendIngredientProcessorV2;
import viteezy.service.quiz.*;
import viteezy.traits.EnforcePresenceTrait;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlendProcessorImplV2 implements BlendProcessorV2, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlendProcessorV2.class);

    private final IngredientRepository ingredientRepository;
    private final BlendIngredientPriceService blendIngredientPriceService;
    private final BlendIngredientReasonService blendIngredientReasonService;
    private final Map<String, BlendIngredientProcessorV2> blendIngredientProcessorMap;
    private final VitaminIntakeAnswerService vitaminIntakeAnswerService;
    private final VitaminIntakeService vitaminIntakeService;
    private final AmountOfMeatConsumptionAnswerService amountOfMeatConsumptionAnswerService;
    private final AmountOfMeatConsumptionService amountOfMeatConsumptionService;
    private final AmountOfFishConsumptionAnswerService amountOfFishConsumptionAnswerService;
    private final AmountOfFishConsumptionService amountOfFishConsumptionService;

    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    protected BlendProcessorImplV2(
            IngredientRepository ingredientRepository, BlendIngredientPriceService blendIngredientPriceService,
            BlendIngredientReasonService blendIngredientReasonService,
            Map<String, BlendIngredientProcessorV2> blendIngredientProcessorMap,
            VitaminIntakeAnswerService vitaminIntakeAnswerService,
            VitaminIntakeService vitaminIntakeService,
            AmountOfMeatConsumptionAnswerService amountOfMeatConsumptionAnswerService,
            AmountOfMeatConsumptionService amountOfMeatConsumptionService,
            AmountOfFishConsumptionAnswerService amountOfFishConsumptionAnswerService,
            AmountOfFishConsumptionService amountOfFishConsumptionService) {
        this.ingredientRepository = ingredientRepository;
        this.blendIngredientReasonService = blendIngredientReasonService;
        this.blendIngredientProcessorMap = blendIngredientProcessorMap;
        this.blendIngredientPriceService = blendIngredientPriceService;
        this.vitaminIntakeAnswerService = vitaminIntakeAnswerService;
        this.vitaminIntakeService = vitaminIntakeService;
        this.amountOfMeatConsumptionAnswerService = amountOfMeatConsumptionAnswerService;
        this.amountOfMeatConsumptionService = amountOfMeatConsumptionService;
        this.amountOfFishConsumptionAnswerService = amountOfFishConsumptionAnswerService;
        this.amountOfFishConsumptionService = amountOfFishConsumptionService;
    }

    @Override
    public Try<Seq<BlendIngredient>> compute(QuizAggregatedInformationV2 quizAggregatedInformationV2) {
        final UUID quizExternalReference = quizAggregatedInformationV2.getQuiz().getExternalReference();
        return blendIngredientReasonService.findAllAsMap()
                .flatMap(innerCompute(quizAggregatedInformationV2))
                .map(filterNullBlendIngredients())
                .map(aftermathCalculation(quizExternalReference))
                .map(addVitaminExceptions(quizAggregatedInformationV2.getBlendId()))
                .map(addIronException(quizExternalReference, quizAggregatedInformationV2.getBlendId()))
                .map(addPricesToBlendIngredients())
                .flatMap(Try::sequence);
    }

    // Prevents old ingredients that are not longer computed from being propagated to further steps
    private Function<List<BlendIngredient>, List<BlendIngredient>> filterNullBlendIngredients() {
        return blendIngredients -> blendIngredients
                .stream()
                .filter(Objects::nonNull)
                .filter(blendIngredient -> !UnitCode.UNITLESS.equals(blendIngredient.getIsUnit()))
                .collect(Collectors.toList());
    }

    private Function<Map<BlendIngredientReasonCode, BlendIngredientReason>, Try<List<BlendIngredient>>> innerCompute(QuizAggregatedInformationV2 quizAggregatedInformationV2) {
        return map -> ingredientRepository.findAll()
                .map(ingredients -> collectIngredients(quizAggregatedInformationV2, ingredients, map));
    }

    private Function<List<BlendIngredient>, List<Try<BlendIngredient>>> addPricesToBlendIngredients() {
        return blendIngredients -> blendIngredients.stream()
                .map(blendIngredientPriceService::addPrice)
                .collect(Collectors.toList());
    }

    private List<BlendIngredient> collectIngredients(
            QuizAggregatedInformationV2 quizAggregatedInformationV2, List<Ingredient> ingredients,
            Map<BlendIngredientReasonCode, BlendIngredientReason> blendIngredientReasons
    ) {
        return ingredients.stream()
                .map(ingredient -> getBlendIngredient(quizAggregatedInformationV2, ingredient, blendIngredientReasons))
                .collect(Collectors.toList());
    }

    private BlendIngredient getBlendIngredient(
            QuizAggregatedInformationV2 quizAggregatedInformationV2, Ingredient ingredient,
            Map<BlendIngredientReasonCode, BlendIngredientReason> blendIngredientReasons
    ) {
        return getBlendIngredientProcessor(ingredient.getCode())
                .map(blendIngredientProcessor -> blendIngredientProcessor.compute(quizAggregatedInformationV2, ingredient.getId(), blendIngredientReasons))
                .orElse(null);
    }

    private Optional<BlendIngredientProcessorV2> getBlendIngredientProcessor(String ingredientCode) {
        return Optional.ofNullable(blendIngredientProcessorMap.get(ingredientCode));
    }

    private Function<List<BlendIngredient>, List<BlendIngredient>> aftermathCalculation(@NonNull UUID quizExternalReference) {
        return blendIngredients -> {
            List<BlendIngredient> blendIngredientList = new ArrayList<>();

            Long vitaminIntakeId = vitaminIntakeAnswerService.find(quizExternalReference)
                    .flatMap(optionalToNarrowedEither())
                    .map(VitaminIntakeAnswer::getVitaminIntakeId).getOrElse(0L);
            Either<Throwable, VitaminIntake> vitaminIntakeEither = vitaminIntakeService.find(vitaminIntakeId)
                    .flatMap(optionalToNarrowedEither());

            String vitaminIntakeCode = vitaminIntakeEither.map(VitaminIntake::getCode).getOrElse(VitaminIntakeCode.NONE);
            int maximumVitaminAmount;

            switch (vitaminIntakeCode) {
                case (VitaminIntakeCode.FIVE_PLUS):
                    maximumVitaminAmount = 5;
                    break;
                case (VitaminIntakeCode.ONE_TO_THREE):
                    maximumVitaminAmount = 4;
                    break;
                default:
                    maximumVitaminAmount = 3;
                    break;
            }

            for (BlendIngredient blendIngredient : blendIngredients) {
                if (blendIngredientList.size() == maximumVitaminAmount) {
                    return blendIngredientList;
                }
                if (blendIngredient.getGoal()) {
                    blendIngredientList.add(blendIngredient);
                }
            }

            blendIngredients.sort(Comparator.comparing(BlendIngredient::getPriorityScore).reversed());
            for (BlendIngredient blendIngredient : blendIngredients) {
                if (blendIngredientList.size() == maximumVitaminAmount) {
                    return blendIngredientList;
                }
                if (!blendIngredientList.contains(blendIngredient)) {
                    blendIngredientList.add(blendIngredient);
                }
            }
            return blendIngredientList;
        };
    }

    private String getExplanation(
            List<Optional<BlendIngredientReasonCode>> reasonCodeOptional,
            Map<BlendIngredientReasonCode, BlendIngredientReason> blendIngredientReasons
    ) {
        StringBuilder description = new StringBuilder();
        for (Optional<BlendIngredientReasonCode> reasonCode : reasonCodeOptional.subList(Math.max(0, reasonCodeOptional.size()-2), reasonCodeOptional.size())) {
            description.append(reasonCode
                    .map(blendIngredientReasons::get)
                    .map(BlendIngredientReason::getDescription)
                    .orElse(EMPTY_EXPLANATION)
                    .concat(TILDE));
        }
        return description.toString();
    }

    private Function<List<BlendIngredient>, List<BlendIngredient>> addVitaminExceptions(Long blendId) {
        return blendIngredients -> {
            final Long VITAMIN_C_INGREDIENT_ID = 3L;
            final Long VITAMIN_D_INGREDIENT_ID = 4L;
            final BigDecimal ONE_THOUSAND_UNITS = new BigDecimal(1000L);
            final BigDecimal FIFTY_UNITS = new BigDecimal(50L);
            if (blendIngredients.size() < 3 && blendIngredients.stream().noneMatch(blendIngredient -> blendIngredient.getIngredientId().equals(VITAMIN_C_INGREDIENT_ID))) {
                blendIngredients.add(BlendIngredient.build(VITAMIN_C_INGREDIENT_ID, blendId, ONE_THOUSAND_UNITS, UnitCode.MG, ""));
            }
            if (blendIngredients.size() < 3 && blendIngredients.stream().noneMatch(blendIngredient -> blendIngredient.getIngredientId().equals(VITAMIN_D_INGREDIENT_ID))) {
                blendIngredients.add(BlendIngredient.build(VITAMIN_D_INGREDIENT_ID, blendId, FIFTY_UNITS, UnitCode.MCG, ""));
            }
            return blendIngredients;
        };
    }

    private Function<List<BlendIngredient>, List<BlendIngredient>> addIronException(@NonNull UUID quizExternalReference, Long blendId) {
        return blendIngredients -> {
            final Long IRON_INGREDIENT_ID = 1L;
            final BigDecimal TEN_UNITS = new BigDecimal("10.000");

            boolean isBarelyEatingMeat = amountOfMeatConsumptionAnswerService.find(quizExternalReference)
                    .flatMap(optionalToNarrowedEither())
                    .flatMap(amountOfMeatConsumptionAnswer -> amountOfMeatConsumptionService.find(amountOfMeatConsumptionAnswer.getAmountOfMeatConsumptionId()))
                    .flatMap(optionalToNarrowedEither())
                    .map(amountOfMeatConsumption -> amountOfMeatConsumption.getCode().equals(AnswerCode.NEVER))
                    .getOrElse(false);

            boolean isBarelyEatingFish = amountOfFishConsumptionAnswerService.find(quizExternalReference)
                    .flatMap(optionalToNarrowedEither())
                    .flatMap(amountOfFishConsumptionAnswer -> amountOfFishConsumptionService.find(amountOfFishConsumptionAnswer.getAmountOfFishConsumptionId()))
                    .flatMap(optionalToNarrowedEither())
                    .map(amountOfFishConsumption -> amountOfFishConsumption.getCode().equals(AnswerCode.NEVER))
                    .getOrElse(false);

            List<Optional<BlendIngredientReasonCode>> blendIngredientReasonCodes = new ArrayList<>();
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_B12_NOT_EATING_ENOUGH_MEAT));
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.OMEGA_THREE_VEGAN_NOT_EATING_ENOUGH_FISH));

            final String explanation = blendIngredientReasonService.findAllAsMap()
                    .map(blendIngredientReasonServiceAllAsMap -> getExplanation(blendIngredientReasonCodes, blendIngredientReasonServiceAllAsMap))
                    .getOrElse(EMPTY_EXPLANATION);

            if (isBarelyEatingMeat && isBarelyEatingFish && blendIngredients.stream()
                    .noneMatch(blendIngredient -> blendIngredient.getIngredientId().equals(IRON_INGREDIENT_ID))) {
                blendIngredients.add(BlendIngredient.build(IRON_INGREDIENT_ID, blendId, TEN_UNITS, UnitCode.MG, explanation));
            }
            return blendIngredients;
        };
    }
}
