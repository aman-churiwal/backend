package viteezy.service.blend.preview;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import viteezy.db.BlendRepository;
import viteezy.db.PaymentPlanRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.service.blend.BlendProcessorV2;
import viteezy.service.blend.BlendService;
import viteezy.service.blend.QuizBlendComputeService;
import viteezy.service.quiz.*;

@Configuration("aggregatorConfig")
@Import({
        viteezy.db.IoC.class
})
public class IoC {
    @Bean("quizBlendAggregator")
    public QuizBlendAggregator quizBlendAggregator(
            BlendRepository blendRepository,
            QuizRepository quizRepository,
            PaymentPlanRepository paymentPlanRepository,
            BlendService blendService, QuizBlendComputeService quizBlendComputeService) {
        return new QuizBlendAggregatorImpl(blendRepository, quizRepository, paymentPlanRepository, blendService, quizBlendComputeService);
    }

    @Bean("quizAggregatedInformationServiceV2")
    public QuizBlendPreviewServiceV2 quizV2AggregatedInformationService(
            QuizService quizService, AcnePlaceService acnePlaceService, AcnePlaceAnswerService acnePlaceAnswerService,
            AllergyTypeService allergyTypeService, AllergyTypeAnswerService allergyTypeAnswerService,
            AmountOfFiberConsumptionService amountOfFiberConsumptionService,
            AmountOfFiberConsumptionAnswerService amountOfFiberConsumptionAnswerService,
            AmountOfFishConsumptionService amountOfFishConsumptionService,
            AmountOfFishConsumptionAnswerService amountOfFishConsumptionAnswerService,
            AmountOfFruitConsumptionService amountOfFruitConsumptionService,
            AmountOfFruitConsumptionAnswerService amountOfFruitConsumptionAnswerService,
            AmountOfMeatConsumptionService amountOfMeatConsumptionService,
            AmountOfMeatConsumptionAnswerService amountOfMeatConsumptionAnswerService,
            AmountOfProteinConsumptionService amountOfProteinConsumptionService,
            AmountOfProteinConsumptionAnswerService amountOfProteinConsumptionAnswerService,
            AmountOfVegetableConsumptionService amountOfVegetableConsumptionService,
            AmountOfVegetableConsumptionAnswerService amountOfVegetableConsumptionAnswerService,
            AttentionFocusService attentionFocusService, AttentionFocusAnswerService attentionFocusAnswerService,
            AttentionStateService attentionStateService, AttentionStateAnswerService attentionStateAnswerService,
            AverageSleepingHoursService averageSleepingHoursService,
            AverageSleepingHoursAnswerService averageSleepingHoursAnswerService, ChildrenWishService childrenWishService,
            ChildrenWishAnswerService childrenWishAnswerService, CurrentLibidoAnswerService currentLibidoAnswerService,
            CurrentLibidoService currentLibidoService, DailyFourCoffeeService dailyFourCoffeeService,
            DailyFourCoffeeAnswerService dailyFourCoffeeAnswerService,
            DailySixAlcoholicDrinksService dailySixAlcoholicDrinksService,
            DailySixAlcoholicDrinksAnswerService dailySixAlcoholicDrinksAnswerService,
            DateOfBirthAnswerService dateOfBirthAnswerService, DietIntoleranceService dietIntoleranceService,
            DietIntoleranceAnswerService dietIntoleranceAnswerService, DietTypeService dietTypeService,
            DietTypeAnswerService dietTypeAnswerService, DigestionAmountService digestionAmountService,
            DigestionAmountAnswerService digestionAmountAnswerService,
            DigestionOccurrenceService digestionOccurrenceService,
            DigestionOccurrenceAnswerService digestionOccurrenceAnswerService, DrySkinService drySkinService,
            DrySkinAnswerService drySkinAnswerService, EnergyStateService energyStateService,
            EnergyStateAnswerService energyStateAnswerService,
            GenderService genderService, GenderAnswerService genderAnswerService, HairTypeService hairTypeService,
            HairTypeAnswerService hairTypeAnswerService, IronPrescribedService ironPrescribedService,
            IronPrescribedAnswerService ironPrescribedAnswerService,
            LackOfConcentrationService lackOfConcentrationService,
            LackOfConcentrationAnswerService lackOfConcentrationAnswerService,
            LibidoStressLevelService libidoStressLevelService,
            LibidoStressLevelAnswerService libidoStressLevelAnswerService,
            MentalFitnessService mentalFitnessService, MentalFitnessAnswerService mentalFitnessAnswerService,
            MenstruationIntervalService menstruationIntervalService,
            MenstruationIntervalAnswerService menstruationIntervalAnswerService,
            MenstruationMoodService menstruationMoodService,
            MenstruationMoodAnswerService menstruationMoodAnswerService,
            MenstruationSideIssueService menstruationSideIssueService,
            MenstruationSideIssueAnswerService menstruationSideIssueAnswerService,
            NailImprovementService nailImprovementService, NailImprovementAnswerService nailImprovementAnswerService,
            OftenHavingFluService oftenHavingFluService, OftenHavingFluAnswerService oftenHavingFluAnswerService,
            PregnancyStateService pregnancyStateService, PregnancyStateAnswerService pregnancyStateAnswerService,
            PresentAtCrowdedPlacesService presentAtCrowdedPlacesService,
            PresentAtCrowdedPlacesAnswerService presentAtCrowdedPlacesAnswerService,
            PrimaryGoalService primaryGoalService, PrimaryGoalAnswerService primaryGoalAnswerService,
            SkinProblemService skinProblemService, SkinProblemAnswerService skinProblemAnswerService,
            SkinTypeService skinTypeService, SkinTypeAnswerService skinTypeAnswerService,
            SleepQualityService sleepQualityService, SleepQualityAnswerService sleepQualityAnswerService,
            SmokeService smokeService, SmokeAnswerService smokeAnswerService, SportAmountService sportAmountService,
            SportAmountAnswerService sportAmountAnswerService,
            StressLevelAtEndOfDayService stressLevelAtEndOfDayService,
            StressLevelAtEndOfDayAnswerService stressLevelAtEndOfDayAnswerService,
            ThirtyMinutesOfSunService thirtyMinutesOfSunService,
            ThirtyMinutesOfSunAnswerService thirtyMinutesOfSunAnswerService,
            TiredWhenWakeUpService tiredWhenWakeUpService, TiredWhenWakeUpAnswerService tiredWhenWakeUpAnswerService,
            TrainingIntensivelyService trainingIntensivelyService,
            TrainingIntensivelyAnswerService trainingIntensivelyAnswerService,
            TransitionPeriodComplaintsService transitionPeriodComplaintsService,
            TransitionPeriodComplaintsAnswerService transitionPeriodComplaintsAnswerService,
            TroubleFallingAsleepService troubleFallingAsleepService,
            TroubleFallingAsleepAnswerService troubleFallingAsleepAnswerService,
            UrinaryInfectionService urinaryInfectionService,
            UrinaryInfectionAnswerService urinaryInfectionAnswerService, UsageGoalService usageGoalService,
            UsageGoalAnswerService usageGoalAnswerService, VitaminIntakeService vitaminIntakeService,
            VitaminIntakeAnswerService vitaminIntakeAnswerService,
            WeeklyTwelveAlcoholicDrinksService weeklyTwelveAlcoholicDrinksService,
            WeeklyTwelveAlcoholicDrinksAnswerService weeklyTwelveAlcoholicDrinksAnswerService,
            BlendProcessorV2 blendProcessorV2) {
        return new QuizBlendPreviewServiceImplV2(
                quizService, blendProcessorV2, acnePlaceService, acnePlaceAnswerService, allergyTypeService, allergyTypeAnswerService,
                amountOfFiberConsumptionService, amountOfFiberConsumptionAnswerService, amountOfFishConsumptionService,
                amountOfFishConsumptionAnswerService, amountOfFruitConsumptionService, amountOfFruitConsumptionAnswerService,
                amountOfMeatConsumptionService, amountOfMeatConsumptionAnswerService, amountOfProteinConsumptionService,
                amountOfProteinConsumptionAnswerService, amountOfVegetableConsumptionService, amountOfVegetableConsumptionAnswerService,
                attentionFocusService, attentionFocusAnswerService, attentionStateService, attentionStateAnswerService, averageSleepingHoursService, averageSleepingHoursAnswerService, childrenWishService, childrenWishAnswerService,
                currentLibidoAnswerService, currentLibidoService, dailyFourCoffeeService, dailyFourCoffeeAnswerService, dailySixAlcoholicDrinksService,
                dailySixAlcoholicDrinksAnswerService, dateOfBirthAnswerService, dietIntoleranceService, dietIntoleranceAnswerService,
                dietTypeService, dietTypeAnswerService, digestionAmountService, digestionAmountAnswerService,
                digestionOccurrenceService, digestionOccurrenceAnswerService, drySkinService, drySkinAnswerService,
                energyStateService, energyStateAnswerService, genderService, genderAnswerService, hairTypeService, hairTypeAnswerService, ironPrescribedService,
                ironPrescribedAnswerService, lackOfConcentrationService, lackOfConcentrationAnswerService, libidoStressLevelService,
                libidoStressLevelAnswerService, mentalFitnessService, mentalFitnessAnswerService, menstruationIntervalService, menstruationIntervalAnswerService, menstruationMoodService, menstruationMoodAnswerService, menstruationSideIssueService, menstruationSideIssueAnswerService, nailImprovementService, nailImprovementAnswerService, oftenHavingFluService,
                oftenHavingFluAnswerService, pregnancyStateService, pregnancyStateAnswerService, presentAtCrowdedPlacesService,
                presentAtCrowdedPlacesAnswerService, primaryGoalService, primaryGoalAnswerService, skinProblemService, skinProblemAnswerService,
                skinTypeService, skinTypeAnswerService, sleepQualityService, sleepQualityAnswerService, smokeService,
                smokeAnswerService, sportAmountService, sportAmountAnswerService,stressLevelAtEndOfDayService,
                stressLevelAtEndOfDayAnswerService,thirtyMinutesOfSunService, thirtyMinutesOfSunAnswerService,
                tiredWhenWakeUpService, tiredWhenWakeUpAnswerService, trainingIntensivelyService, trainingIntensivelyAnswerService,
                transitionPeriodComplaintsService, transitionPeriodComplaintsAnswerService, troubleFallingAsleepService, troubleFallingAsleepAnswerService, urinaryInfectionService, urinaryInfectionAnswerService,
                usageGoalService, usageGoalAnswerService, vitaminIntakeService, vitaminIntakeAnswerService,
                weeklyTwelveAlcoholicDrinksService, weeklyTwelveAlcoholicDrinksAnswerService
        );
    }
}
