package viteezy.service.blend.v2;

import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.QuizAggregatedInformationV2;
import viteezy.domain.quiz.code.AnswerCode;
import viteezy.domain.blend.BlendIngredientReasonCode;
import viteezy.domain.quiz.code.SportAmountCode;
import viteezy.domain.ingredient.UnitCode;
import viteezy.domain.quiz.DailyFourCoffee;
import viteezy.domain.quiz.SportAmount;
import viteezy.domain.quiz.TrainingIntensively;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MagnesiumBlendIngredientProcessor implements BlendIngredientProcessorV2 {

    private static final BigDecimal ONE_THOUSAND_UNITS = new BigDecimal("1000.000");
    private static final BigDecimal ZERO_UNITS = new BigDecimal("0.000");
    private static final String MG_UNIT = UnitCode.MG;
    private static final String UNITLESS_UNIT = UnitCode.UNITLESS;
    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    private static final Integer PRIORITY_UNIT = 7;
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
                .map(code -> ONE_THOUSAND_UNITS)
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

    private List<Optional<BlendIngredientReasonCode>> getReasonCode(QuizAggregatedInformationV2 quizAggregatedInformation) {
        List<Optional<BlendIngredientReasonCode>> blendIngredientReasonCodes = new ArrayList<>();
        if (isHighSportAmount(quizAggregatedInformation.getSportAmount())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.MAGNESIUM_HIGH_SPORT_AMOUNT));
            PRIORITY_AMOUNT += 1;
        }
        if (isTrainingIntensively(quizAggregatedInformation.getTrainingIntensively())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.MAGNESIUM_TRAINING_INTENSIVELY));
            PRIORITY_AMOUNT += 1;
        }
        if (isDailyFourCoffee(quizAggregatedInformation.getDailyFourCoffee())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.MAGNESIUM_COFFEE));
            PRIORITY_AMOUNT += 1;
        }
        return blendIngredientReasonCodes;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isHighSportAmount(Optional<SportAmount> sportAmountOptional) {
        return sportAmountOptional
                .map(sportAmount -> {
                    final String code = sportAmount.getCode();
                    return SportAmountCode.TWO_TO_FOUR.equals(code) || SportAmountCode.FIVE_OR_MORE.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isTrainingIntensively(Optional<TrainingIntensively> trainingIntensivelyOptional) {
        return trainingIntensivelyOptional
                .map(trainingIntensively -> {
                    final String code = trainingIntensively.getCode();
                    return AnswerCode.YES.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isDailyFourCoffee(Optional<DailyFourCoffee> dailyFourCoffeeOptional) {
        return dailyFourCoffeeOptional
                .map(dailyFourCoffee -> {
                    final String code = dailyFourCoffee.getCode();
                    return AnswerCode.YES.equals(code);
                }).orElse(false);
    }
}
