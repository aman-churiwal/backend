package viteezy.service.blend.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class OmegaThreeVeganBlendIngredientProcessor implements BlendIngredientProcessorV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmegaThreeVeganBlendIngredientProcessor.class);

    private static final BigDecimal FIVE_HUNDRED_UNITS = new BigDecimal("500.000");
    private static final BigDecimal ZERO_UNITS = new BigDecimal("0.000");
    private static final String MG_UNIT = UnitCode.MG;
    private static final String UNITLESS_UNIT = UnitCode.UNITLESS;
    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    private static final Integer PRIORITY_UNIT = 9;
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

    private List<Optional<BlendIngredientReasonCode>> getReasonCode(QuizAggregatedInformationV2 quizAggregatedInformationV2) {
        List<Optional<BlendIngredientReasonCode>> blendIngredientReasonCodes = new ArrayList<>();
        if (isVegan(quizAggregatedInformationV2.getDietType())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_B12_OMEGA_THREE_VEGAN_VEGAN));
            PRIORITY_AMOUNT += 1;
            if (isEatingMuchMeat(quizAggregatedInformationV2.getAmountOfMeatConsumption())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.OMEGA_THREE_VEGAN_EATING_MUCH_MEAT));
                PRIORITY_AMOUNT += 1;
            }
            if (isNotEatingFish(quizAggregatedInformationV2.getAmountOfFishConsumption())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.OMEGA_THREE_VEGAN_NOT_EATING_ENOUGH_FISH));
                PRIORITY_AMOUNT += 1;
            }
            if (isAcnePlaceBack(quizAggregatedInformationV2.getAcnePlace())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.OMEGA_THREE_VEGAN_ACNE_BACK));
                PRIORITY_AMOUNT += 1;
            }
            if (isAcnePlaceCheeksNoseOrForehead(quizAggregatedInformationV2.getAcnePlace())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.OMEGA_THREE_VEGAN_ACNE_FACE));
                PRIORITY_AMOUNT += 1;
            }
        } else if (isVegetarian(quizAggregatedInformationV2.getDietType())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_B12_D_OMEGA_THREE_VEGAN_VEGETARIAN));
            PRIORITY_AMOUNT += 1;
            if (isEatingMuchMeat(quizAggregatedInformationV2.getAmountOfMeatConsumption())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.OMEGA_THREE_VEGAN_EATING_MUCH_MEAT));
                PRIORITY_AMOUNT += 1;
            }
            if (isNotEatingFish(quizAggregatedInformationV2.getAmountOfFishConsumption())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.OMEGA_THREE_VEGAN_NOT_EATING_ENOUGH_FISH));
                PRIORITY_AMOUNT += 1;
            }
            if (isAcnePlaceBack(quizAggregatedInformationV2.getAcnePlace())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.OMEGA_THREE_VEGAN_ACNE_BACK));
                PRIORITY_AMOUNT += 1;
            }
            if (isAcnePlaceCheeksNoseOrForehead(quizAggregatedInformationV2.getAcnePlace())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.OMEGA_THREE_VEGAN_ACNE_FACE));
                PRIORITY_AMOUNT += 1;
            }
        }
        if (isVegan(quizAggregatedInformationV2.getDietType()) || isVegetarian(quizAggregatedInformationV2.getDietType())) {
            if (isAttentionStateCantComeUpWithWords(quizAggregatedInformationV2.getAttentionState())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_CANT_COME_UP_WITH_WORDS));
                PRIORITY_AMOUNT += 1;
            }
            if (isAttentionStateDifficultyWithFocus(quizAggregatedInformationV2.getAttentionState())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_TROUBLE_FOCUSING));
                PRIORITY_AMOUNT += 1;
            }
            if (isAttentionStateForgetfulness(quizAggregatedInformationV2.getAttentionState())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_FORGETFULNESS));
                PRIORITY_AMOUNT += 1;
            }
            if (isAttentionStateLackOfConcentration(quizAggregatedInformationV2.getAttentionState())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_LACK_OF_CONCENTRATION));
                PRIORITY_AMOUNT += 1;
            }
            if (isMentalFitnessDejection(quizAggregatedInformationV2.getMentalFitness())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_DEJECTED));
                PRIORITY_AMOUNT += 1;
            }
            if (isMentalFitnessFearFeelings(quizAggregatedInformationV2.getMentalFitness())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_FEAR));
                PRIORITY_AMOUNT += 1;
            }
            if (isMentalFitnessLackOfMotivation(quizAggregatedInformationV2.getMentalFitness())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_LACK_OF_MOTIVATION));
                PRIORITY_AMOUNT += 1;
            }
            if (isMentalFitnessWorry(quizAggregatedInformationV2.getMentalFitness())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_WORRY));
            }
            if (isAttentionFocusConcentration(quizAggregatedInformationV2.getAttentionFocus())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_FOCUS_CONCENTRATION));
                PRIORITY_AMOUNT += 1;
            }
            if (isAttentionFocusMemory(quizAggregatedInformationV2.getAttentionFocus())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_FOCUS_MEMORY));
                PRIORITY_AMOUNT += 1;
            }
            if (isAttentionFocusMentalFitness(quizAggregatedInformationV2.getAttentionFocus())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_FOCUS_MENTAL_FITNESS));
                PRIORITY_AMOUNT += 1;
            }
            if (isAttentionFocusStaySharp(quizAggregatedInformationV2.getAttentionFocus())) {
                blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.BRAIN_BOOST_FOCUS_SHARP));
                PRIORITY_AMOUNT += 1;
            }
        }
        return blendIngredientReasonCodes;
    }

    private String getUnit(BigDecimal amount) {
        return ZERO_UNITS.equals(amount) ? UNITLESS_UNIT : MG_UNIT;
    }

    private BigDecimal getAmount(List<Optional<BlendIngredientReasonCode>> reasonCodeOptional) {
        return reasonCodeOptional.stream().findFirst()
                .filter(Optional::isPresent)
                .map(code -> FIVE_HUNDRED_UNITS)
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

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isVegan(Optional<DietType> dietTypeOptional) {
        return dietTypeOptional
                .map(dietType -> {
                    final String code = dietType.getCode();
                    return DietTypeCode.VEGAN.equals(code);
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
    private boolean isEatingMuchMeat(Optional<AmountOfMeatConsumption> amountOfMeatConsumptionOptional) {
        return amountOfMeatConsumptionOptional
                .map(amountOfMeatConsumption -> {
                    final String code = amountOfMeatConsumption.getCode();
                    return AnswerCode.THREE_OR_MORE.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isNotEatingFish(Optional<AmountOfFishConsumption> amountOfFishConsumptionOptional) {
        return amountOfFishConsumptionOptional
                .map(amountOfFishConsumption -> {
                    final String code = amountOfFishConsumption.getCode();
                    return AnswerCode.NEVER.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isAcnePlaceBack(Optional<AcnePlace> acnePlaceOptional) {
        return acnePlaceOptional
                .map(acnePlace -> {
                    final String code = acnePlace.getCode();
                    return AcnePlaceCode.CHEST_OR_BACK.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isAcnePlaceCheeksNoseOrForehead(Optional<AcnePlace> acnePlaceOptional) {
        return acnePlaceOptional
                .map(acnePlace -> {
                    final String code = acnePlace.getCode();
                    return AcnePlaceCode.CHEEKS_NOSE_OR_FOREHEAD.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isAttentionStateCantComeUpWithWords(Optional<AttentionState> attentionStateOptional) {
        return attentionStateOptional
                .map(attentionState -> {
                    final String code = attentionState.getCode();
                    return AttentionStateCode.CANT_COME_UP_WITH_WORDS.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isAttentionStateDifficultyWithFocus(Optional<AttentionState> attentionStateOptional) {
        return attentionStateOptional
                .map(attentionState -> {
                    final String code = attentionState.getCode();
                    return AttentionStateCode.DIFFICULTY_WITH_FOCUS.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isAttentionStateForgetfulness(Optional<AttentionState> attentionStateOptional) {
        return attentionStateOptional
                .map(attentionState -> {
                    final String code = attentionState.getCode();
                    return AttentionStateCode.FORGETFULNESS.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isAttentionStateLackOfConcentration(Optional<AttentionState> attentionStateOptional) {
        return attentionStateOptional
                .map(attentionState -> {
                    final String code = attentionState.getCode();
                    return AttentionStateCode.LACK_OF_CONCENTRATION.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMentalFitnessDejection(Optional<MentalFitness> mentalFitnessOptional) {
        return mentalFitnessOptional
                .map(mentalFitness -> {
                    final String code = mentalFitness.getCode();
                    return MentalFitnessCode.DEJECTION.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMentalFitnessFearFeelings(Optional<MentalFitness> mentalFitnessOptional) {
        return mentalFitnessOptional
                .map(mentalFitness -> {
                    final String code = mentalFitness.getCode();
                    return MentalFitnessCode.FEAR_FEELINGS.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMentalFitnessLackOfMotivation(Optional<MentalFitness> mentalFitnessOptional) {
        return mentalFitnessOptional
                .map(mentalFitness -> {
                    final String code = mentalFitness.getCode();
                    return MentalFitnessCode.LACK_OF_MOTIVATION.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isMentalFitnessWorry(Optional<MentalFitness> mentalFitnessOptional) {
        return mentalFitnessOptional
                .map(mentalFitness -> {
                    final String code = mentalFitness.getCode();
                    return MentalFitnessCode.WORRY.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isAttentionFocusConcentration(Optional<AttentionFocus> attentionFocusCodeOptional) {
        return attentionFocusCodeOptional
                .map(attentionFocus -> {
                    final String code = attentionFocus.getCode();
                    return AttentionFocusCode.CONCENTRATION.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isAttentionFocusMemory(Optional<AttentionFocus> attentionFocusCodeOptional) {
        return attentionFocusCodeOptional
                .map(attentionFocus -> {
                    final String code = attentionFocus.getCode();
                    return AttentionFocusCode.MEMORY.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isAttentionFocusMentalFitness(Optional<AttentionFocus> attentionFocusCodeOptional) {
        return attentionFocusCodeOptional
                .map(attentionFocus -> {
                    final String code = attentionFocus.getCode();
                    return AttentionFocusCode.MENTAL_FITNESS.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isAttentionFocusStaySharp(Optional<AttentionFocus> attentionFocusCodeOptional) {
        return attentionFocusCodeOptional
                .map(attentionFocus -> {
                    final String code = attentionFocus.getCode();
                    return AttentionFocusCode.STAY_SHARP.equals(code);
                }).orElse(false);
    }
}
