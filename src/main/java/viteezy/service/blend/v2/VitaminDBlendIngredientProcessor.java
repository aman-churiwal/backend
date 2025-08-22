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
import viteezy.domain.quiz.DietType;
import viteezy.domain.quiz.ThirtyMinutesOfSun;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VitaminDBlendIngredientProcessor implements BlendIngredientProcessorV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(VitaminDBlendIngredientProcessor.class);

    private static final BigDecimal FIFTY_UNITS = new BigDecimal("50.000");
    private static final BigDecimal ZERO_UNITS = new BigDecimal("0.000");
    private static final String MCG_UNIT = UnitCode.MCG;
    private static final String UNITLESS_UNIT = UnitCode.UNITLESS;
    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    private static final Integer PRIORITY_UNIT = 10;
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

    private String getUnit(BigDecimal amount) {
        return ZERO_UNITS.equals(amount) ? UNITLESS_UNIT : MCG_UNIT;
    }

    private BigDecimal getAmount(List<Optional<BlendIngredientReasonCode>> reasonCodeOptional) {
        return reasonCodeOptional.stream().findFirst()
                .filter(Optional::isPresent)
                .map(code -> FIFTY_UNITS)
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

    private List<Optional<BlendIngredientReasonCode>> getReasonCode(QuizAggregatedInformationV2 quizAggregatedInformationV2) {
        List<Optional<BlendIngredientReasonCode>> blendIngredientReasonCodes = new ArrayList<>();
        if (isVegan(quizAggregatedInformationV2.getDietType())) {
            return blendIngredientReasonCodes;
        }
        if (isVegetarian(quizAggregatedInformationV2.getDietType())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_B12_D_OMEGA_THREE_VEGAN_VEGETARIAN));
            PRIORITY_AMOUNT += 1;
        }
        if (noThirtyMinutesOfSun(quizAggregatedInformationV2.getThirtyMinutesOfSun())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_D_NOT_GETTING_ENOUGH_SUN));
            PRIORITY_AMOUNT += 1;
        }
        return blendIngredientReasonCodes;
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

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean noThirtyMinutesOfSun(Optional<ThirtyMinutesOfSun> thirtyMinutesOfSunOptional) {
        return thirtyMinutesOfSunOptional
                .map(thirtyMinutesOfSun -> {
                    final String code = thirtyMinutesOfSun.getCode();
                    return AnswerCode.NO.equals(code);
                }).orElse(false);
    }
}