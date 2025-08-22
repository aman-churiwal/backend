package viteezy.service.blend.v2;

import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.QuizAggregatedInformationV2;
import viteezy.domain.blend.BlendIngredientReasonCode;
import viteezy.domain.ingredient.UnitCode;
import viteezy.domain.quiz.LibidoStressLevel;
import viteezy.domain.quiz.PregnancyState;
import viteezy.domain.quiz.StressLevelAtEndOfDay;
import viteezy.domain.quiz.UsageGoal;
import viteezy.domain.quiz.code.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StressFormulaBlendIngredientProcessor implements BlendIngredientProcessorV2 {

    private static final BigDecimal TEN_UNITS = new BigDecimal("10.000");
    private static final BigDecimal ZERO_UNITS = new BigDecimal("0.000");
    private static final String MG_UNIT = UnitCode.MG;
    private static final String UNITLESS_UNIT = UnitCode.UNITLESS;
    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    private static final Integer PRIORITY_UNIT = 14;
    private static Integer PRIORITY_AMOUNT = 0;
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
        final Integer PRIORITY_SCORE = PRIORITY_UNIT * PRIORITY_AMOUNT;
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
        if (isUsageGoalStress(quizAggregatedInformationV2.getUsageGoals())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.STRESS_FORMULA_SUBJECT));
            isGoal = true;
            PRIORITY_AMOUNT += 1;
        }
        if (isStressLevelAtEndOfDayBurntOut(quizAggregatedInformationV2.getStressLevelAtEndOfDay())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.STRESS_FORMULA_BURNT_OUT));
            PRIORITY_AMOUNT += 1;
        }
        if (isStressLevelAtEndOfDayHyper(quizAggregatedInformationV2.getStressLevelAtEndOfDay())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.STRESS_FORMULA_HYPER));
            PRIORITY_AMOUNT += 1;
        }
        if (isHighLibidoStressLevel(quizAggregatedInformationV2.getLibidoStressLevel())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.STRESS_FORMULA_LIBIDO));
            PRIORITY_AMOUNT += 1;
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

    private boolean isUsageGoalStress(List<UsageGoal> usageGoalList) {
        return usageGoalList.stream().anyMatch(usageGoal ->
                UsageGoalCode.STRESS.equals(usageGoal.getCode()));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isStressLevelAtEndOfDayBurntOut(Optional<StressLevelAtEndOfDay> stressLevelAtEndOfDayOptional) {
        return stressLevelAtEndOfDayOptional
                .map(stressLevelAtEndOfDay -> {
                    final String code = stressLevelAtEndOfDay.getCode();
                    return StressLevelAtEndOfDayCode.BURNT_OUT.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isStressLevelAtEndOfDayHyper(Optional<StressLevelAtEndOfDay> stressLevelAtEndOfDayOptional) {
        return stressLevelAtEndOfDayOptional
                .map(stressLevelAtEndOfDay -> {
                    final String code = stressLevelAtEndOfDay.getCode();
                    return StressLevelAtEndOfDayCode.HYPER.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isHighLibidoStressLevel(Optional<LibidoStressLevel> libidoStressLevelOptional) {
        return libidoStressLevelOptional
                .map(libidoStressLevel -> {
                    final String code = libidoStressLevel.getCode();
                    return AnswerCode.HIGH.equals(code);
                }).orElse(false);
    }
}
