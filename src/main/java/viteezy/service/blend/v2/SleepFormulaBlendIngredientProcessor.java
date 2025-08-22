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

public class SleepFormulaBlendIngredientProcessor implements BlendIngredientProcessorV2 {

    private static final BigDecimal TEN_UNITS = new BigDecimal("10.000");
    private static final BigDecimal ZERO_UNITS = new BigDecimal("0.000");
    private static final String MG_UNIT = UnitCode.MG;
    private static final String UNITLESS_UNIT = UnitCode.UNITLESS;
    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    private static final Integer PRIORITY_UNIT = 13;
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

    private BigDecimal getAmount(List<Optional<BlendIngredientReasonCode>> reasonCodeOptional) {
        return reasonCodeOptional.stream().findFirst()
                .filter(Optional::isPresent)
                .map(code -> TEN_UNITS)
                .orElse(ZERO_UNITS);
    }

    private String getUnit(BigDecimal amount) {
        return ZERO_UNITS.equals(amount) ? UNITLESS_UNIT : MG_UNIT;
    }

    private List<Optional<BlendIngredientReasonCode>> getReasonCode(QuizAggregatedInformationV2 quizAggregatedInformationV2) {
        List<Optional<BlendIngredientReasonCode>> blendIngredientReasonCodes = new ArrayList<>();
        if (isPregnantOrBreastfeedingOrPregnantInTwoYears(quizAggregatedInformationV2.getPregnancyState())) {
            return blendIngredientReasonCodes;
        }
        if (isUsageGoalSleep(quizAggregatedInformationV2.getUsageGoals())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SLEEP_FORMULA_SUBJECT));
            isGoal = true;
            PRIORITY_AMOUNT += 1;
        }
        if (troubleFallingAsleep(quizAggregatedInformationV2.getTroubleFallingAsleep())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SLEEP_FORMULA_TROUBLE_FALLING_ASLEEP));
            PRIORITY_AMOUNT += 1;
        }
        if (isTiredWhenWakeUp(quizAggregatedInformationV2.getTiredWhenWakeUp())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SLEEP_FORMULA_TIRED_WHEN_WAKE_UP));
            PRIORITY_AMOUNT += 1;
        }
        if (isSleepingLessThanSevenHours(quizAggregatedInformationV2.getAverageSleepingHours())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SLEEP_FORMULA_LESS_THAN_7_HOURS));
            PRIORITY_AMOUNT += 1;
        }
        if (isSleepingLessThanFiveHours(quizAggregatedInformationV2.getAverageSleepingHours())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SLEEP_FORMULA_LESS_THAN_5_HOURS));
            PRIORITY_AMOUNT += 1;
        }
        if (isLowSleepQuality(quizAggregatedInformationV2.getSleepQuality())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.SLEEP_FORMULA_LIBIDO));
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

    private boolean isUsageGoalSleep(List<UsageGoal> usageGoalList) {
        return usageGoalList.stream().anyMatch(usageGoal ->
                UsageGoalCode.SLEEP.equals(usageGoal.getCode()));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean troubleFallingAsleep(Optional<TroubleFallingAsleep> troubleFallingAsleepOptional) {
        return troubleFallingAsleepOptional
                .map(troubleFallingAsleep -> {
                    final String code = troubleFallingAsleep.getCode();
                    return AnswerCode.YES.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isTiredWhenWakeUp(Optional<TiredWhenWakeUp> tiredWhenWakeUpOptional) {
        return tiredWhenWakeUpOptional
                .map(tiredWhenWakeUp -> {
                    final String code = tiredWhenWakeUp.getCode();
                    return AnswerCode.YES.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isSleepingLessThanSevenHours(Optional<AverageSleepingHours> averageSleepingHoursOptional) {
        return averageSleepingHoursOptional
                .map(averageSleepingHours -> {
                    final String code = averageSleepingHours.getCode();
                    return SleepHourCode.LESS_THAN_SEVEN.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isSleepingLessThanFiveHours(Optional<AverageSleepingHours> averageSleepingHoursOptional) {
        return averageSleepingHoursOptional
                .map(averageSleepingHours -> {
                    final String code = averageSleepingHours.getCode();
                    return SleepHourCode.LESS_THAN_FIVE.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isLowSleepQuality(Optional<SleepQuality> sleepQualityOptional) {
        return sleepQualityOptional
                .map(sleepQuality -> {
                    final String code = sleepQuality.getCode();
                    return AnswerCode.LOW.equals(code);
                }).orElse(false);
    }
}
