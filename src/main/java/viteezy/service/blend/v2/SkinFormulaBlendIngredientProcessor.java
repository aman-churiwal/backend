package viteezy.service.blend.v2;

import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.QuizAggregatedInformationV2;
import viteezy.domain.blend.BlendIngredientReasonCode;
import viteezy.domain.ingredient.UnitCode;
import viteezy.domain.quiz.*;
import viteezy.domain.quiz.code.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SkinFormulaBlendIngredientProcessor implements BlendIngredientProcessorV2 {

    private static final BigDecimal TEN_UNITS = new BigDecimal("10.000");
    private static final BigDecimal ZERO_UNITS = new BigDecimal("0.000");
    private static final String MG_UNIT = UnitCode.MG;
    private static final String UNITLESS_UNIT = UnitCode.UNITLESS;
    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    private static final Integer PRIORITY_SCORE = 0;
    private static Boolean isGoal = false;

    @Override
    public BlendIngredient compute(
            QuizAggregatedInformationV2 quizAggregatedInformationV2, Long ingredientId,
            Map<BlendIngredientReasonCode, BlendIngredientReason> blendIngredientReasons
    ) {
        final List<Optional<BlendIngredientReasonCode>> reasonCodeOptional = getReasonCode(quizAggregatedInformationV2);
        final BigDecimal amount = getAmount(reasonCodeOptional);
        final String unit = getUnit(amount);
        final String explanation = getExplanation(reasonCodeOptional, blendIngredientReasons);
        return BlendIngredient.buildV2(ingredientId, quizAggregatedInformationV2.getBlendId(), amount, unit, explanation, isGoal, PRIORITY_SCORE);
    }

    private BigDecimal getAmount(List<Optional<BlendIngredientReasonCode>> reasonCodeOptional) {
        return reasonCodeOptional.stream().findFirst()
                .filter(Optional::isPresent)
                .map(code -> TEN_UNITS)
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
        if (isPregnantOrBreastfeedingOrPregnantInTwoYears(quizAggregatedInformationV2.getPregnancyState())) {
            return blendIngredientReasonCodes;
        }
        if (isUsageGoalHairAndNails(quizAggregatedInformationV2.getPrimaryGoal())) {
            return blendIngredientReasonCodes;
        }
        if (isUsageGoalSkin(quizAggregatedInformationV2.getUsageGoals())) {
            isGoal = true;
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SKIN_FORMULA_SUBJECT));
        }
        if (isDrySkin(quizAggregatedInformationV2.getSkinType())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SKIN_FORMULA_DRY));
        }
        if (isFatSkin(quizAggregatedInformationV2.getSkinType())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SKIN_FORMULA_FAT));
        }
        if (isRestlessSkin(quizAggregatedInformationV2.getSkinType())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SKIN_FORMULA_RESTLESS));
        }
        if (isDullSkin(quizAggregatedInformationV2.getSkinType())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SKIN_FORMULA_DULL));
        }
        if (isSkinProblemAcne(quizAggregatedInformationV2.getSkinProblem())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SKIN_FORMULA_ACNE));
        }
        if (isSkinProblemPigmentSpots(quizAggregatedInformationV2.getSkinProblem())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SKIN_FORMULA_PIGMENT_SPOTS));
        }
        if (isSkinProblemWrinkles(quizAggregatedInformationV2.getSkinProblem())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SKIN_FORMULA_WRINKLES));
        }
        if (isSkinProblemElasticity(quizAggregatedInformationV2.getSkinProblem())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SKIN_FORMULA_ELASTICITY));
        }
        if (isSkinProblemAging(quizAggregatedInformationV2.getSkinProblem())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SKIN_FORMULA_AGING));
        }
        return blendIngredientReasonCodes;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isPregnantOrBreastfeedingOrPregnantInTwoYears(Optional<PregnancyState> pregnancyStateOptional) {
        return pregnancyStateOptional
                .map(pregnancyState -> {
                    final String code = pregnancyState.getCode();
                    return PregnancyStateCode.PREGNANT.equals(code) || PregnancyStateCode.BREASTFEEDING.equals(code)
                            || PregnancyStateCode.PREGNANT_IN_TWO_YEARS.equals(code);
                }).orElse(false);
    }

    private boolean isUsageGoalHairAndNails(Optional<PrimaryGoal> optionalPrimaryGoal) {
        return optionalPrimaryGoal
                .map(primaryGoal -> {
                    final String code = primaryGoal.getCode();
                    return UsageGoalCode.HAIR_AND_NAILS.equals(code);
                }).orElse(false);
    }

    private boolean isUsageGoalSkin(List<UsageGoal> usageGoalList) {
        return usageGoalList.stream().anyMatch(usageGoal ->
                UsageGoalCode.SKIN.equals(usageGoal.getCode()));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isDrySkin(Optional<SkinType> skinTypeOptional) {
        return skinTypeOptional
                .map(skinType -> {
                    final String code = skinType.getCode();
                    return SkinTypeCode.DRY.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isFatSkin(Optional<SkinType> skinTypeOptional) {
        return skinTypeOptional
                .map(skinType -> {
                    final String code = skinType.getCode();
                    return SkinTypeCode.FAT.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isRestlessSkin(Optional<SkinType> skinTypeOptional) {
        return skinTypeOptional
                .map(skinType -> {
                    final String code = skinType.getCode();
                    return SkinTypeCode.RESTLESS.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isDullSkin(Optional<SkinType> skinTypeOptional) {
        return skinTypeOptional
                .map(skinType -> {
                    final String code = skinType.getCode();
                    return SkinTypeCode.DULL.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isSkinProblemAcne(Optional<SkinProblem> skinProblemOptional) {
        return skinProblemOptional
                .map(skinProblem -> {
                    final String code = skinProblem.getCode();
                    return SkinProblemCode.ACNE.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isSkinProblemPigmentSpots(Optional<SkinProblem> skinProblemOptional) {
        return skinProblemOptional
                .map(skinProblem -> {
                    final String code = skinProblem.getCode();
                    return SkinProblemCode.PIGMENT_SPOTS.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isSkinProblemWrinkles(Optional<SkinProblem> skinProblemOptional) {
        return skinProblemOptional
                .map(skinProblem -> {
                    final String code = skinProblem.getCode();
                    return SkinProblemCode.WRINKLES.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isSkinProblemElasticity(Optional<SkinProblem> skinProblemOptional) {
        return skinProblemOptional
                .map(skinProblem -> {
                    final String code = skinProblem.getCode();
                    return SkinProblemCode.ELASTICITY.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isSkinProblemAging(Optional<SkinProblem> skinProblemOptional) {
        return skinProblemOptional
                .map(skinProblem -> {
                    final String code = skinProblem.getCode();
                    return SkinProblemCode.SKIN_AGING.equals(code);
                }).orElse(false);
    }
}
