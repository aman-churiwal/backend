package viteezy.service.blend.v2;

import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.QuizAggregatedInformationV2;
import viteezy.domain.quiz.code.AnswerCode;
import viteezy.domain.blend.BlendIngredientReasonCode;
import viteezy.domain.ingredient.UnitCode;
import viteezy.domain.quiz.UrinaryInfection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CranberryBlendIngredientProcessor implements BlendIngredientProcessorV2 {

    private static final BigDecimal ONE_UNIT = new BigDecimal("1.000");
    private static final BigDecimal ZERO_UNITS = new BigDecimal("0.000");
    private static final String MG_UNIT = UnitCode.MG;
    private static final String UNITLESS_UNIT = UnitCode.UNITLESS;
    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    private static final Integer PRIORITY_UNIT = 2;
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
        if (isUrinaryInfection(quizAggregatedInformationV2.getUrinaryInfection())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.CRANBERRY_CYSTITIS));
            PRIORITY_AMOUNT += 1;
        }
        return blendIngredientReasonCodes;

    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isUrinaryInfection(Optional<UrinaryInfection> urinaryInfectionOptional) {
        return urinaryInfectionOptional
                .map(urinaryInfection -> {
                    final String code = urinaryInfection.getCode();
                    return AnswerCode.YES.equals(code);
                }).orElse(false);
    }
}
