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

public class LibidoFormulaBlendIngredientProcessor implements BlendIngredientProcessorV2 {

    private static final BigDecimal TEN_UNITS = new BigDecimal("10.000");
    private static final BigDecimal ZERO_UNITS = new BigDecimal("0.000");
    private static final String MG_UNIT = UnitCode.MG;
    private static final String UNITLESS_UNIT = UnitCode.UNITLESS;
    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    private static final Integer PRIORITY_UNIT = 12;
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
        if (isLowSleepQuality(quizAggregatedInformationV2.getSleepQuality())) {
            return blendIngredientReasonCodes;
        }
        if (isHighLibidoStressLevel(quizAggregatedInformationV2.getLibidoStressLevel())) {
            return blendIngredientReasonCodes;
        }
        if (isUsageGoalLibido(quizAggregatedInformationV2.getUsageGoals())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.LIBIDO_FORMULA_SUBJECT));
            isGoal = true;
            PRIORITY_AMOUNT += 1;
        }
        if (isAcnePlaceChinOrJawline(quizAggregatedInformationV2.getAcnePlace())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.LIBIDO_FORMULA_ACNE_FACE));
            PRIORITY_AMOUNT += 1;
        }
        if (isLowLibido(quizAggregatedInformationV2.getCurrentLibido())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.LIBIDO_LOW));
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

    private boolean isUsageGoalLibido(List<UsageGoal> usageGoalList) {
        return usageGoalList.stream().anyMatch(usageGoal ->
                UsageGoalCode.LIBIDO.equals(usageGoal.getCode()));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isAcnePlaceChinOrJawline(Optional<AcnePlace> acnePlaceOptional) {
        return acnePlaceOptional
                .map(acnePlace -> {
                    final String code = acnePlace.getCode();
                    return AcnePlaceCode.CHIN_OR_JAWLINE.equals(code);
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

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isHighLibidoStressLevel(Optional<LibidoStressLevel> libidoStressLevelOptional) {
        return libidoStressLevelOptional
                .map(libidoStressLevel -> {
                    final String code = libidoStressLevel.getCode();
                    return AnswerCode.HIGH.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isLowLibido(Optional<CurrentLibido> currentLibidoOptional) {
        return currentLibidoOptional
                .map(currentLibido -> {
                    final String code = currentLibido.getCode();
                    return AnswerCode.LOW.equals(code);
                }).orElse(false);
    }
}
