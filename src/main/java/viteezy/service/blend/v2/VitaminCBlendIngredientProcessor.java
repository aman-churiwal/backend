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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VitaminCBlendIngredientProcessor implements BlendIngredientProcessorV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(VitaminCBlendIngredientProcessor.class);

    private static final BigDecimal ONE_THOUSAND_UNITS = new BigDecimal("1000.000");
    private static final BigDecimal ZERO_UNITS = new BigDecimal("0.000");
    private static final String MG_UNIT = UnitCode.MG;
    private static final String UNITLESS_UNIT = UnitCode.UNITLESS;
    private static final String EMPTY_EXPLANATION = "";
    private static final String TILDE = "~";

    private static final Integer PRIORITY_UNIT = 3;
    private static Integer PRIORITY_AMOUNT = 0;
    private static Boolean isGoal = false;

    @Override
    public BlendIngredient compute(
            QuizAggregatedInformationV2 quizAggregatedInformationV2, Long ingredientId,
            Map<BlendIngredientReasonCode, BlendIngredientReason> blendIngredientReasons) {
        final List<Optional<BlendIngredientReasonCode>> reasonCodeOptional = getReasonCode(quizAggregatedInformationV2);
        final BigDecimal amount = getAmount(reasonCodeOptional);
        final String unit = getUnit(amount);
        final String explanation = getExplanation(reasonCodeOptional, blendIngredientReasons);
        final Integer PRIORITY_SCORE = PRIORITY_UNIT * PRIORITY_AMOUNT;
        return BlendIngredient.buildV2(ingredientId, quizAggregatedInformationV2.getBlendId(), amount, unit, explanation, isGoal, PRIORITY_SCORE);
    }

    private String getUnit(BigDecimal amount) {
        return ZERO_UNITS.equals(amount) ? UNITLESS_UNIT : MG_UNIT;
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

    private List<Optional<BlendIngredientReasonCode>> getReasonCode(QuizAggregatedInformationV2 quizAggregatedInformationV2) {
        List<Optional<BlendIngredientReasonCode>> blendIngredientReasonCodes = new ArrayList<>();
        if (isPregnantInTwoYears(quizAggregatedInformationV2.getPregnancyState())) {
            return blendIngredientReasonCodes;
        }
        if (isUsageGoalResistance(quizAggregatedInformationV2.getUsageGoals())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_C_AND_ZINC_SUBJECT_RESISTANCE));
            isGoal = true;
            PRIORITY_AMOUNT += 1;
        }
        if (isOlderThanSixtyFive(quizAggregatedInformationV2.getDateOfBirthAnswer())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_C_OLDER_THAN_65));
            PRIORITY_AMOUNT += 1;
        }
        if (isPresentAtCrowdedPlaces(quizAggregatedInformationV2.getPresentAtCrowdedPlaces())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_C_AND_ZINC_PRESENT_AT_CROWDED_PLACES));
            PRIORITY_AMOUNT += 1;
        }
        if (oftenHavingFlu(quizAggregatedInformationV2.getOftenHavingFlu())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_C_AND_ZINC_OFTEN_HAVING_FLU));
            PRIORITY_AMOUNT += 1;
        }
        if (isBarelyEatingFruit(quizAggregatedInformationV2.getAmountOfFruitConsumption())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_C_NOT_EATING_ENOUGH_FRUIT));
            PRIORITY_AMOUNT += 1;
        }
        if (isBarelyEatingVegetable(quizAggregatedInformationV2.getAmountOfVegetableConsumption())) {
            blendIngredientReasonCodes.add(Optional.of(BlendIngredientReasonCode.VITAMIN_C_NOT_EATING_ENOUGH_VEGETABLE));
            PRIORITY_AMOUNT += 1;
        }
        return blendIngredientReasonCodes;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isPregnantInTwoYears(Optional<PregnancyState> pregnancyStateOptional) {
        return pregnancyStateOptional
                .map(pregnancyState -> {
                    final String code = pregnancyState.getCode();
                    return PregnancyStateCode.PREGNANT_IN_TWO_YEARS.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isOlderThanSixtyFive(Optional<DateOfBirthAnswer> dateOfBirthAnswerOptional) {
        return dateOfBirthAnswerOptional
                .map(dateOfBirthAnswer -> dateOfBirthAnswer.getDate().isBefore(LocalDate.now().minusYears(65)))
                .orElse(false);
    }

    private boolean isUsageGoalResistance(List<UsageGoal> usageGoalList) {
        return usageGoalList.stream().anyMatch(usageGoal ->
                UsageGoalCode.RESISTANCE.equals(usageGoal.getCode()));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isPresentAtCrowdedPlaces(Optional<PresentAtCrowdedPlaces> presentAtCrowdedPlacesOptional) {
        return presentAtCrowdedPlacesOptional
                .map(presentAtCrowdedPlaces -> {
                    final String code = presentAtCrowdedPlaces.getCode();
                    return AnswerCode.YES.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean oftenHavingFlu(Optional<OftenHavingFlu> oftenHavingFluOptional) {
        return oftenHavingFluOptional
                .map(oftenHavingFlu -> {
                    final String code = oftenHavingFlu.getCode();
                    return AnswerCode.YES.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isBarelyEatingFruit(Optional<AmountOfFruitConsumption> amountOfFruitConsumptionOptional) {
        return amountOfFruitConsumptionOptional
                .map(amountOfFruitConsumption -> {
                    final String code = amountOfFruitConsumption.getCode();
                    return AnswerCode.BARELY.equals(code);
                }).orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean isBarelyEatingVegetable(Optional<AmountOfVegetableConsumption> amountOfVegetableConsumptionOptional) {
        return amountOfVegetableConsumptionOptional
                .map(amountOfVegetableConsumption -> {
                    final String code = amountOfVegetableConsumption.getCode();
                    return AnswerCode.BARELY.equals(code);
                }).orElse(false);
    }
}