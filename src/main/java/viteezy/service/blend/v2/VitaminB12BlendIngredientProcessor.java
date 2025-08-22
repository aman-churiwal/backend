package viteezy.service.blend.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.QuizAggregatedInformationV2;
import viteezy.domain.quiz.code.AnswerCode;
import viteezy.domain.blend.BlendIngredientReasonCode;
import viteezy.domain.quiz.code.DietTypeCode;
import viteezy.domain.ingredient.UnitCode;
import viteezy.domain.quiz.AmountOfMeatConsumption;
import viteezy.domain.quiz.DietType;
import viteezy.domain.quiz.LackOfConcentration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VitaminB12BlendIngredientProcessor implements BlendIngredientProcessorV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(VitaminB12BlendIngredientProcessor.class);

    private static final BigDecimal ONE_UNIT = new BigDecimal("1.000");
    private static final BigDecimal ZERO_UNITS = new BigDecimal("0.000");
    private static final String MG_UNIT = UnitCode.MG;
    private static final String UNITLESS_UNIT = UnitCode.UNITLESS;
    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    private static final Integer PRIORITY_UNIT = 11;
    private static Integer PRIORITY_AMOUNT = 0;
    private static final Boolean isGoal = false;

    @Override
    public BlendIngredient compute(
            QuizAggregatedInformationV2 quizAggregatedInformationV2, Long ingredientId,
            Map<BlendIngredientReasonCode, BlendIngredientReason> blendIngredientReasons
    ) {
        final List<Optional<BlendIngredientReasonCode>> reasonCodeOptional = getReasonCode(quizAggregatedInformationV2);
        final BigDecimal amount = getAmount(reasonCodeOptional);
        final String unit = getUnit(amount);
        final String explanation = getExplanation(reasonCodeOptional, blendIngredientReasons);
        final Integer PRIORITY_SCORE = PRIORITY_UNIT * PRIORITY_AMOUNT;
        return BlendIngredient.buildV2(ingredientId, quizAggregatedInformationV2.getBlendId(), amount, unit, explanation, isGoal, PRIORITY_SCORE);
    }

    private BigDecimal getAmount(List<Optional<BlendIngredientReasonCode>> reasonCodeOptional) {
        return reasonCodeOptional.stream().findFirst()
                .filter(Optional::isPresent)
                .map(code -> ONE_UNIT)
                .orElse(ZERO_UNITS);
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

    private String getUnit(BigDecimal amount) {
        return ZERO_UNITS.equals(amount) ? UNITLESS_UNIT : MG_UNIT;
    }

    private List<Optional<BlendIngredientReasonCode>> getReasonCode(QuizAggregatedInformationV2 quizAggregatedInformationV2) {
        List<Optional<BlendIngredientReasonCode>> blendIngredientReasonCodes = new ArrayList<>();
        if (isLackOfConcentration(quizAggregatedInformationV2.getLackOfConcentration())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_B12_LACK_OF_CONCENTRATION));
            PRIORITY_AMOUNT += 1;
        }
        if (isNotEatingMeat(quizAggregatedInformationV2.getAmountOfMeatConsumption())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_B12_NOT_EATING_ENOUGH_MEAT));
            PRIORITY_AMOUNT += 1;
        }
        if (isVegetarian(quizAggregatedInformationV2.getDietType())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_B12_D_OMEGA_THREE_VEGAN_VEGETARIAN));
            PRIORITY_AMOUNT += 1;
        }
        if (isVegan(quizAggregatedInformationV2.getDietType())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_B12_OMEGA_THREE_VEGAN_VEGAN));
            PRIORITY_AMOUNT += 1;
        }
        return blendIngredientReasonCodes;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isLackOfConcentration(Optional<LackOfConcentration> lackOfConcentrationOptional) {
        return lackOfConcentrationOptional
                .map(lackOfConcentration -> {
                    final String code = lackOfConcentration.getCode();
                    return AnswerCode.YES.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isNotEatingMeat(Optional<AmountOfMeatConsumption> amountOfMeatConsumptionOptional) {
        return amountOfMeatConsumptionOptional
                .map(amountOfMeatConsumption -> {
                    final String code = amountOfMeatConsumption.getCode();
                    return AnswerCode.NEVER.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isVegetarian(Optional<DietType> dietTypeOptional) {
        return dietTypeOptional
                .map(dietType -> {
                    final String code = dietType.getCode();
                    return DietTypeCode.VEGETARIAN.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isVegan(Optional<DietType> dietTypeOptional) {
        return dietTypeOptional
                .map(dietType -> {
                    final String code = dietType.getCode();
                    return DietTypeCode.VEGAN.equals(code);
                }).orElse(false);
    }
}
