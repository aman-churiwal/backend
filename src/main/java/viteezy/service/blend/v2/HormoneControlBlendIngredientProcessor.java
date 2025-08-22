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

public class HormoneControlBlendIngredientProcessor implements BlendIngredientProcessorV2 {

    private static final BigDecimal TEN_UNITS = new BigDecimal("10.000");
    private static final BigDecimal ZERO_UNITS = new BigDecimal("0.000");
    private static final String MG_UNIT = UnitCode.MG;
    private static final String UNITLESS_UNIT = UnitCode.UNITLESS;
    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    private static final Integer PRIORITY_UNIT = 15;
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
        if (isMan(quizAggregatedInformationV2.getGender())) {
            return blendIngredientReasonCodes;
        }
        if (isPregnantOrBreastfeedingOrPregnantInTwoYears(quizAggregatedInformationV2.getPregnancyState())) {
            return blendIngredientReasonCodes;
        }
        if (isUsageGoalMenstruation(quizAggregatedInformationV2.getUsageGoals())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.HORMONE_CONTROL_SUBJECT));
            isGoal = true;
            PRIORITY_AMOUNT += 1;
        }
        if (isMenstruationSideIssueBingeEating(quizAggregatedInformationV2.getMenstruationSideIssue())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.HORMONE_CONTROL_BINGE_EATING));
            PRIORITY_AMOUNT += 1;
        }
        if (isMenstruationSideIssueBloatedFeeling(quizAggregatedInformationV2.getMenstruationSideIssue())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.HORMONE_CONTROL_BLOATED_FEELING));
            PRIORITY_AMOUNT += 1;
        }
        if (isMenstruationSideIssueCramp(quizAggregatedInformationV2.getMenstruationSideIssue())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.HORMONE_CONTROL_CRAMP));
            PRIORITY_AMOUNT += 1;
        }
        if (isMenstruationSideIssuePhysicalDiscomfort(quizAggregatedInformationV2.getMenstruationSideIssue())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.HORMONE_CONTROL_PHYSICAL_DISCOMFORT));
            PRIORITY_AMOUNT += 1;
        }
        if (isMenstruationSideIssuePimples(quizAggregatedInformationV2.getMenstruationSideIssue())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.HORMONE_CONTROL_SPOTS));
            PRIORITY_AMOUNT += 1;
        }
        if (isMenstruationMoodMoodSwings(quizAggregatedInformationV2.getMenstruationMood())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.HORMONE_CONTROL_MOOD_SWINGS));
            PRIORITY_AMOUNT += 1;
        }
        if (isMenstruationMoodIrritable(quizAggregatedInformationV2.getMenstruationMood())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.HORMONE_CONTROL_IRRITABLE));
            PRIORITY_AMOUNT += 1;
        }
        if (isMenstruationMoodLifeless(quizAggregatedInformationV2.getMenstruationMood())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.HORMONE_CONTROL_LIFELESS));
            PRIORITY_AMOUNT += 1;
        }
        if (isMenstruationMoodTense(quizAggregatedInformationV2.getMenstruationMood())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.HORMONE_CONTROL_TENSE));
            PRIORITY_AMOUNT += 1;
        }
        if (isTransitionPeriodComplaints(quizAggregatedInformationV2.getTransitionPeriodComplaints())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.HORMONE_CONTROL_TRANSITION_PERIOD_COMPLAINTS));
            PRIORITY_AMOUNT += 1;
        }
        return blendIngredientReasonCodes;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMan(Optional<Gender> genderOptional) {
        return genderOptional
                .map(gender -> {
                    final String code = gender.getCode();
                    return GenderCode.MAN.equals(code);
                }).orElse(false);
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

    private boolean isUsageGoalMenstruation(List<UsageGoal> usageGoalList) {
        return usageGoalList.stream().anyMatch(usageGoal ->
                UsageGoalCode.MENSTRUATION.equals(usageGoal.getCode()));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMenstruationSideIssueBingeEating(Optional<MenstruationSideIssue> menstruationSideIssueOptional) {
        return menstruationSideIssueOptional
                .map(menstruationSideIssue -> {
                    final String code = menstruationSideIssue.getCode();
                    return MenstruationSideIssueCode.BINGE_EATING.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMenstruationSideIssueBloatedFeeling(Optional<MenstruationSideIssue> menstruationSideIssueOptional) {
        return menstruationSideIssueOptional
                .map(menstruationSideIssue -> {
                    final String code = menstruationSideIssue.getCode();
                    return MenstruationSideIssueCode.BLOATED_FEELING.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMenstruationSideIssueCramp(Optional<MenstruationSideIssue> menstruationSideIssueOptional) {
        return menstruationSideIssueOptional
                .map(menstruationSideIssue -> {
                    final String code = menstruationSideIssue.getCode();
                    return MenstruationSideIssueCode.CRAMP.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMenstruationSideIssuePhysicalDiscomfort(Optional<MenstruationSideIssue> menstruationSideIssueOptional) {
        return menstruationSideIssueOptional
                .map(menstruationSideIssue -> {
                    final String code = menstruationSideIssue.getCode();
                    return MenstruationSideIssueCode.PHYSICAL_DISCOMFORT.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMenstruationSideIssuePimples(Optional<MenstruationSideIssue> menstruationSideIssueOptional) {
        return menstruationSideIssueOptional
                .map(menstruationSideIssue -> {
                    final String code = menstruationSideIssue.getCode();
                    return MenstruationSideIssueCode.PIMPLES.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMenstruationMoodMoodSwings(Optional<MenstruationMood> menstruationMoodOptional) {
        return menstruationMoodOptional
                .map(menstruationMood -> {
                    final String code = menstruationMood.getCode();
                    return MenstruationMoodCode.MOOD_SWINGS.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMenstruationMoodIrritable(Optional<MenstruationMood> menstruationMoodOptional) {
        return menstruationMoodOptional
                .map(menstruationMood -> {
                    final String code = menstruationMood.getCode();
                    return MenstruationMoodCode.IRRITABLE.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMenstruationMoodLifeless(Optional<MenstruationMood> menstruationMoodOptional) {
        return menstruationMoodOptional
                .map(menstruationMood -> {
                    final String code = menstruationMood.getCode();
                    return MenstruationMoodCode.LIFELESS.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMenstruationMoodTense(Optional<MenstruationMood> menstruationMoodOptional) {
        return menstruationMoodOptional
                .map(menstruationMood -> {
                    final String code = menstruationMood.getCode();
                    return MenstruationMoodCode.TENSE.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isTransitionPeriodComplaints(Optional<TransitionPeriodComplaints> transitionPeriodComplaintsOptional) {
        return transitionPeriodComplaintsOptional
                .map(transitionPeriodComplaints -> {
                    final String code = transitionPeriodComplaints.getCode();
                    return AnswerCode.YES.equals(code);
                }).orElse(false);
    }
}
