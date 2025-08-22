package viteezy.service.blend.v2;

import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.QuizAggregatedInformationV2;
import viteezy.domain.blend.BlendIngredientReasonCode;
import viteezy.domain.ingredient.UnitCode;
import viteezy.domain.quiz.EnergyState;
import viteezy.domain.quiz.PregnancyState;
import viteezy.domain.quiz.UsageGoal;
import viteezy.domain.quiz.code.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EnergyFormulaBlendIngredientProcessor implements BlendIngredientProcessorV2 {

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
        if (isUsageGoalEnergy(quizAggregatedInformationV2.getUsageGoals())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.ENERGY_FORMULA_SUBJECT));
            isGoal = true;
        }
        if (isEnergyStateLifeless(quizAggregatedInformationV2.getEnergyState())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.ENERGY_FORMULA_LIFELESS));
        }
        if (isEnergyStateAfternoonDip(quizAggregatedInformationV2.getEnergyState())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.ENERGY_FORMULA_AFTERNOON_DIP));
        }
        if (isEnergyStatePeaksAndTroughs(quizAggregatedInformationV2.getEnergyState())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.ENERGY_FORMULA_PEAKS_AND_TROUGHS));
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

    private boolean isUsageGoalEnergy(List<UsageGoal> usageGoalList) {
        return usageGoalList.stream().anyMatch(usageGoal ->
                UsageGoalCode.ENERGY.equals(usageGoal.getCode()));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isEnergyStateLifeless(Optional<EnergyState> energyStateOptional) {
        return energyStateOptional
                .map(energyState -> {
                    final String code = energyState.getCode();
                    return EnergyStateCode.LIFELESS.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isEnergyStateAfternoonDip(Optional<EnergyState> energyStateOptional) {
        return energyStateOptional
                .map(energyState -> {
                    final String code = energyState.getCode();
                    return EnergyStateCode.AFTERNOON_DIP.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isEnergyStatePeaksAndTroughs(Optional<EnergyState> energyStateOptional) {
        return energyStateOptional
                .map(energyState -> {
                    final String code = energyState.getCode();
                    return EnergyStateCode.PEAKS_AND_TROUGHS.equals(code);
                }).orElse(false);
    }
}
