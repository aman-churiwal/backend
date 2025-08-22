package viteezy.service.blend.preview;

import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.QuizAggregatedInformationV2;
import viteezy.domain.quiz.*;
import viteezy.service.blend.BlendProcessorV2;
import viteezy.service.quiz.*;
import viteezy.traits.EnforcePresenceTrait;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuizBlendPreviewServiceImplV2 implements QuizBlendPreviewServiceV2, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizBlendPreviewServiceImplV2.class);

    private final QuizService quizService;
    private final BlendProcessorV2 blendProcessorV2;

    private final AcnePlaceService acnePlaceService;
    private final AcnePlaceAnswerService acnePlaceAnswerService;
    private final AllergyTypeService allergyTypeService;
    private final AllergyTypeAnswerService allergyTypeAnswerService;
    private final AmountOfFiberConsumptionService amountOfFiberConsumptionService;
    private final AmountOfFiberConsumptionAnswerService amountOfFiberConsumptionAnswerService;
    private final AmountOfFishConsumptionService amountOfFishConsumptionService;
    private final AmountOfFishConsumptionAnswerService amountOfFishConsumptionAnswerService;
    private final AmountOfFruitConsumptionService amountOfFruitConsumptionService;
    private final AmountOfFruitConsumptionAnswerService amountOfFruitConsumptionAnswerService;
    private final AmountOfMeatConsumptionService amountOfMeatConsumptionService;
    private final AmountOfMeatConsumptionAnswerService amountOfMeatConsumptionAnswerService;
    private final AmountOfProteinConsumptionService amountOfProteinConsumptionService;
    private final AmountOfProteinConsumptionAnswerService amountOfProteinConsumptionAnswerService;
    private final AmountOfVegetableConsumptionService amountOfVegetableConsumptionService;
    private final AmountOfVegetableConsumptionAnswerService amountOfVegetableConsumptionAnswerService;
    private final AttentionFocusService attentionFocusService;
    private final AttentionFocusAnswerService attentionFocusAnswerService;
    private final AttentionStateService attentionStateService;
    private final AttentionStateAnswerService attentionStateAnswerService;
    private final AverageSleepingHoursService averageSleepingHoursService;
    private final AverageSleepingHoursAnswerService averageSleepingHoursAnswerService;
    private final ChildrenWishService childrenWishService;
    private final ChildrenWishAnswerService childrenWishAnswerService;
    private final CurrentLibidoAnswerService currentLibidoAnswerService;
    private final CurrentLibidoService currentLibidoService;
    private final DailyFourCoffeeService dailyFourCoffeeService;
    private final DailyFourCoffeeAnswerService dailyFourCoffeeAnswerService;
    private final DailySixAlcoholicDrinksService dailySixAlcoholicDrinksService;
    private final DailySixAlcoholicDrinksAnswerService dailySixAlcoholicDrinksAnswerService;
    private final DateOfBirthAnswerService dateOfBirthAnswerService;
    private final DietIntoleranceService dietIntoleranceService;
    private final DietIntoleranceAnswerService dietIntoleranceAnswerService;
    private final DietTypeService dietTypeService;
    private final DietTypeAnswerService dietTypeAnswerService;
    private final DigestionAmountService digestionAmountService;
    private final DigestionAmountAnswerService digestionAmountAnswerService;
    private final DigestionOccurrenceService digestionOccurrenceService;
    private final DigestionOccurrenceAnswerService digestionOccurrenceAnswerService;
    private final DrySkinService drySkinService;
    private final DrySkinAnswerService drySkinAnswerService;
    private final EnergyStateService energyStateService;
    private final EnergyStateAnswerService energyStateAnswerService;
    private final GenderService genderService;
    private final GenderAnswerService genderAnswerService;
    private final HairTypeService hairTypeService;
    private final HairTypeAnswerService hairTypeAnswerService;
    private final IronPrescribedService ironPrescribedService;
    private final IronPrescribedAnswerService ironPrescribedAnswerService;
    private final LackOfConcentrationService lackOfConcentrationService;
    private final LackOfConcentrationAnswerService lackOfConcentrationAnswerService;
    private final LibidoStressLevelService libidoStressLevelService;
    private final LibidoStressLevelAnswerService libidoStressLevelAnswerService;
    private final MentalFitnessService mentalFitnessService;
    private final MentalFitnessAnswerService mentalFitnessAnswerService;
    private final MenstruationIntervalService menstruationIntervalService;
    private final MenstruationIntervalAnswerService menstruationIntervalAnswerService;
    private final MenstruationMoodService menstruationMoodService;
    private final MenstruationMoodAnswerService menstruationMoodAnswerService;
    private final MenstruationSideIssueService menstruationSideIssueService;
    private final MenstruationSideIssueAnswerService menstruationSideIssueAnswerService;
    private final NailImprovementService nailImprovementService;
    private final NailImprovementAnswerService nailImprovementAnswerService;
    private final OftenHavingFluService oftenHavingFluService;
    private final OftenHavingFluAnswerService oftenHavingFluAnswerService;
    private final PregnancyStateService pregnancyStateService;
    private final PregnancyStateAnswerService pregnancyStateAnswerService;
    private final PresentAtCrowdedPlacesService presentAtCrowdedPlacesService;
    private final PresentAtCrowdedPlacesAnswerService presentAtCrowdedPlacesAnswerService;
    private final PrimaryGoalService primaryGoalService;
    private final PrimaryGoalAnswerService primaryGoalAnswerService;
    private final SkinProblemService skinProblemService;
    private final SkinProblemAnswerService skinProblemAnswerService;
    private final SkinTypeService skinTypeService;
    private final SkinTypeAnswerService skinTypeAnswerService;
    private final SleepQualityService sleepQualityService;
    private final SleepQualityAnswerService sleepQualityAnswerService;
    private final SmokeService smokeService;
    private final SmokeAnswerService smokeAnswerService;
    private final SportAmountService sportAmountService;
    private final SportAmountAnswerService sportAmountAnswerService;
    private final StressLevelAtEndOfDayService stressLevelAtEndOfDayService;
    private final StressLevelAtEndOfDayAnswerService stressLevelAtEndOfDayAnswerService;
    private final ThirtyMinutesOfSunService thirtyMinutesOfSunService;
    private final ThirtyMinutesOfSunAnswerService thirtyMinutesOfSunAnswerService;
    private final TiredWhenWakeUpService tiredWhenWakeUpService;
    private final TiredWhenWakeUpAnswerService tiredWhenWakeUpAnswerService;
    private final TrainingIntensivelyService trainingIntensivelyService;
    private final TrainingIntensivelyAnswerService trainingIntensivelyAnswerService;
    private final TransitionPeriodComplaintsService transitionPeriodComplaintsService;
    private final TransitionPeriodComplaintsAnswerService transitionPeriodComplaintsAnswerService;
    private final TroubleFallingAsleepService troubleFallingAsleepService;
    private final TroubleFallingAsleepAnswerService troubleFallingAsleepAnswerService;
    private final UrinaryInfectionService urinaryInfectionService;
    private final UrinaryInfectionAnswerService urinaryInfectionAnswerService;
    private final UsageGoalService usageGoalService;
    private final UsageGoalAnswerService usageGoalAnswerService;
    private final VitaminIntakeService vitaminIntakeService;
    private final VitaminIntakeAnswerService vitaminIntakeAnswerService;
    private final WeeklyTwelveAlcoholicDrinksService weeklyTwelveAlcoholicDrinksService;
    private final WeeklyTwelveAlcoholicDrinksAnswerService weeklyTwelveAlcoholicDrinksAnswerService;

    protected QuizBlendPreviewServiceImplV2(
            QuizService quizService, BlendProcessorV2 blendProcessorV2, AcnePlaceService acnePlaceService, AcnePlaceAnswerService acnePlaceAnswerService,
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
            DigestionAmountAnswerService digestionAmountAnswerService, DigestionOccurrenceService digestionOccurrenceService,
            DigestionOccurrenceAnswerService digestionOccurrenceAnswerService, DrySkinService drySkinService,
            DrySkinAnswerService drySkinAnswerService, EnergyStateService energyStateService,
            EnergyStateAnswerService energyStateAnswerService, GenderService genderService, GenderAnswerService genderAnswerService, HairTypeService hairTypeService,
            HairTypeAnswerService hairTypeAnswerService, IronPrescribedService ironPrescribedService,
            IronPrescribedAnswerService ironPrescribedAnswerService, LackOfConcentrationService lackOfConcentrationService,
            LackOfConcentrationAnswerService lackOfConcentrationAnswerService, LibidoStressLevelService libidoStressLevelService,
            LibidoStressLevelAnswerService libidoStressLevelAnswerService, MentalFitnessService mentalFitnessService,
            MentalFitnessAnswerService mentalFitnessAnswerService, MenstruationIntervalService menstruationIntervalService,
            MenstruationIntervalAnswerService menstruationIntervalAnswerService, MenstruationMoodService menstruationMoodService,
            MenstruationMoodAnswerService menstruationMoodAnswerService, MenstruationSideIssueService menstruationSideIssueService,
            MenstruationSideIssueAnswerService menstruationSideIssueAnswerService, NailImprovementService nailImprovementService,
            NailImprovementAnswerService nailImprovementAnswerService, OftenHavingFluService oftenHavingFluService,
            OftenHavingFluAnswerService oftenHavingFluAnswerService, PregnancyStateService pregnancyStateService,
            PregnancyStateAnswerService pregnancyStateAnswerService, PresentAtCrowdedPlacesService presentAtCrowdedPlacesService,
            PresentAtCrowdedPlacesAnswerService presentAtCrowdedPlacesAnswerService, PrimaryGoalService primaryGoalService,
            PrimaryGoalAnswerService primaryGoalAnswerService, SkinProblemService skinProblemService,
            SkinProblemAnswerService skinProblemAnswerService, SkinTypeService skinTypeService,
            SkinTypeAnswerService skinTypeAnswerService, SleepQualityService sleepQualityService,
            SleepQualityAnswerService sleepQualityAnswerService, SmokeService smokeService, SmokeAnswerService smokeAnswerService,
            SportAmountService sportAmountService, SportAmountAnswerService sportAmountAnswerService, StressLevelAtEndOfDayService stressLevelAtEndOfDayService,
            StressLevelAtEndOfDayAnswerService stressLevelAtEndOfDayAnswerService,
            ThirtyMinutesOfSunService thirtyMinutesOfSunService, ThirtyMinutesOfSunAnswerService thirtyMinutesOfSunAnswerService,
            TiredWhenWakeUpService tiredWhenWakeUpService, TiredWhenWakeUpAnswerService tiredWhenWakeUpAnswerService,
            TrainingIntensivelyService trainingIntensivelyService, TrainingIntensivelyAnswerService trainingIntensivelyAnswerService,
            TransitionPeriodComplaintsService transitionPeriodComplaintsService, TransitionPeriodComplaintsAnswerService transitionPeriodComplaintsAnswerService, TroubleFallingAsleepService troubleFallingAsleepService,
            TroubleFallingAsleepAnswerService troubleFallingAsleepAnswerService, UrinaryInfectionService urinaryInfectionService,
            UrinaryInfectionAnswerService urinaryInfectionAnswerService, UsageGoalService usageGoalService,
            UsageGoalAnswerService usageGoalAnswerService, VitaminIntakeService vitaminIntakeService,
            VitaminIntakeAnswerService vitaminIntakeAnswerService,
            WeeklyTwelveAlcoholicDrinksService weeklyTwelveAlcoholicDrinksService,
            WeeklyTwelveAlcoholicDrinksAnswerService weeklyTwelveAlcoholicDrinksAnswerService) {
        this.quizService = quizService;
        this.blendProcessorV2 = blendProcessorV2;
        this.acnePlaceService = acnePlaceService;
        this.acnePlaceAnswerService = acnePlaceAnswerService;
        this.allergyTypeService = allergyTypeService;
        this.allergyTypeAnswerService = allergyTypeAnswerService;
        this.amountOfFiberConsumptionService = amountOfFiberConsumptionService;
        this.amountOfFiberConsumptionAnswerService = amountOfFiberConsumptionAnswerService;
        this.amountOfFishConsumptionService = amountOfFishConsumptionService;
        this.amountOfFishConsumptionAnswerService = amountOfFishConsumptionAnswerService;
        this.amountOfFruitConsumptionService = amountOfFruitConsumptionService;
        this.amountOfFruitConsumptionAnswerService = amountOfFruitConsumptionAnswerService;
        this.amountOfMeatConsumptionService = amountOfMeatConsumptionService;
        this.amountOfMeatConsumptionAnswerService = amountOfMeatConsumptionAnswerService;
        this.amountOfProteinConsumptionService = amountOfProteinConsumptionService;
        this.amountOfProteinConsumptionAnswerService = amountOfProteinConsumptionAnswerService;
        this.amountOfVegetableConsumptionService = amountOfVegetableConsumptionService;
        this.amountOfVegetableConsumptionAnswerService = amountOfVegetableConsumptionAnswerService;
        this.attentionFocusService = attentionFocusService;
        this.attentionFocusAnswerService = attentionFocusAnswerService;
        this.attentionStateService = attentionStateService;
        this.attentionStateAnswerService = attentionStateAnswerService;
        this.averageSleepingHoursService = averageSleepingHoursService;
        this.averageSleepingHoursAnswerService = averageSleepingHoursAnswerService;
        this.childrenWishService = childrenWishService;
        this.childrenWishAnswerService = childrenWishAnswerService;
        this.currentLibidoAnswerService = currentLibidoAnswerService;
        this.currentLibidoService = currentLibidoService;
        this.dailyFourCoffeeService = dailyFourCoffeeService;
        this.dailyFourCoffeeAnswerService = dailyFourCoffeeAnswerService;
        this.dailySixAlcoholicDrinksService = dailySixAlcoholicDrinksService;
        this.dailySixAlcoholicDrinksAnswerService = dailySixAlcoholicDrinksAnswerService;
        this.dateOfBirthAnswerService = dateOfBirthAnswerService;
        this.dietIntoleranceService = dietIntoleranceService;
        this.dietIntoleranceAnswerService = dietIntoleranceAnswerService;
        this.dietTypeService = dietTypeService;
        this.dietTypeAnswerService = dietTypeAnswerService;
        this.digestionAmountService = digestionAmountService;
        this.digestionAmountAnswerService = digestionAmountAnswerService;
        this.digestionOccurrenceService = digestionOccurrenceService;
        this.digestionOccurrenceAnswerService = digestionOccurrenceAnswerService;
        this.drySkinService = drySkinService;
        this.drySkinAnswerService = drySkinAnswerService;
        this.energyStateService = energyStateService;
        this.energyStateAnswerService = energyStateAnswerService;
        this.genderService = genderService;
        this.genderAnswerService = genderAnswerService;
        this.hairTypeService = hairTypeService;
        this.hairTypeAnswerService = hairTypeAnswerService;
        this.ironPrescribedService = ironPrescribedService;
        this.ironPrescribedAnswerService = ironPrescribedAnswerService;
        this.lackOfConcentrationService = lackOfConcentrationService;
        this.lackOfConcentrationAnswerService = lackOfConcentrationAnswerService;
        this.libidoStressLevelService = libidoStressLevelService;
        this.libidoStressLevelAnswerService = libidoStressLevelAnswerService;
        this.mentalFitnessService = mentalFitnessService;
        this.mentalFitnessAnswerService = mentalFitnessAnswerService;
        this.menstruationIntervalService = menstruationIntervalService;
        this.menstruationIntervalAnswerService = menstruationIntervalAnswerService;
        this.menstruationMoodService = menstruationMoodService;
        this.menstruationMoodAnswerService = menstruationMoodAnswerService;
        this.menstruationSideIssueService = menstruationSideIssueService;
        this.menstruationSideIssueAnswerService = menstruationSideIssueAnswerService;
        this.nailImprovementService = nailImprovementService;
        this.nailImprovementAnswerService = nailImprovementAnswerService;
        this.oftenHavingFluService = oftenHavingFluService;
        this.oftenHavingFluAnswerService = oftenHavingFluAnswerService;
        this.pregnancyStateService = pregnancyStateService;
        this.pregnancyStateAnswerService = pregnancyStateAnswerService;
        this.presentAtCrowdedPlacesService = presentAtCrowdedPlacesService;
        this.presentAtCrowdedPlacesAnswerService = presentAtCrowdedPlacesAnswerService;
        this.primaryGoalService = primaryGoalService;
        this.primaryGoalAnswerService = primaryGoalAnswerService;
        this.skinProblemService = skinProblemService;
        this.skinProblemAnswerService = skinProblemAnswerService;
        this.skinTypeService = skinTypeService;
        this.skinTypeAnswerService = skinTypeAnswerService;
        this.sleepQualityService = sleepQualityService;
        this.sleepQualityAnswerService = sleepQualityAnswerService;
        this.smokeService = smokeService;
        this.smokeAnswerService = smokeAnswerService;
        this.sportAmountService = sportAmountService;
        this.sportAmountAnswerService = sportAmountAnswerService;
        this.stressLevelAtEndOfDayService = stressLevelAtEndOfDayService;
        this.stressLevelAtEndOfDayAnswerService = stressLevelAtEndOfDayAnswerService;
        this.thirtyMinutesOfSunService = thirtyMinutesOfSunService;
        this.thirtyMinutesOfSunAnswerService = thirtyMinutesOfSunAnswerService;
        this.tiredWhenWakeUpService = tiredWhenWakeUpService;
        this.tiredWhenWakeUpAnswerService = tiredWhenWakeUpAnswerService;
        this.trainingIntensivelyService = trainingIntensivelyService;
        this.trainingIntensivelyAnswerService = trainingIntensivelyAnswerService;
        this.transitionPeriodComplaintsService = transitionPeriodComplaintsService;
        this.transitionPeriodComplaintsAnswerService = transitionPeriodComplaintsAnswerService;
        this.troubleFallingAsleepService = troubleFallingAsleepService;
        this.troubleFallingAsleepAnswerService = troubleFallingAsleepAnswerService;
        this.urinaryInfectionService = urinaryInfectionService;
        this.urinaryInfectionAnswerService = urinaryInfectionAnswerService;
        this.usageGoalService = usageGoalService;
        this.usageGoalAnswerService = usageGoalAnswerService;
        this.vitaminIntakeService = vitaminIntakeService;
        this.vitaminIntakeAnswerService = vitaminIntakeAnswerService;
        this.weeklyTwelveAlcoholicDrinksService = weeklyTwelveAlcoholicDrinksService;
        this.weeklyTwelveAlcoholicDrinksAnswerService = weeklyTwelveAlcoholicDrinksAnswerService;
    }

    @Override
    public Try<QuizAggregatedInformationV2> find(Blend blend) {
        return quizService.find(blend.getQuizId())
                .flatMap(quiz -> findInner(quiz, blend.getId()));
    }

    @Override
    public Try<List<BlendIngredient>> getBlendPreviewV2(UUID quizExternalReference) {
        return quizService.find(quizExternalReference)
                .flatMap(quiz -> findInner(quiz, null))
                .flatMap(blendProcessorV2::compute)
                .map(Seq::asJava);
    }

    private Try<QuizAggregatedInformationV2> findInner(Quiz quiz, Long blendId) {
        UUID quizExternalReference = quiz.getExternalReference();
        final Either<Throwable, Optional<AcnePlace>> acnePlace = findAcnePlace(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, List<AllergyType>> allergyTypes = findAllergyTypes(quizExternalReference).orElse(Either.right(Collections.emptyList()));
        final Either<Throwable, Optional<AmountOfFiberConsumption>> amountOfFiberConsumption = findAmountOfFiberConsumption(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<AmountOfFishConsumption>> amountOfFishConsumption = findAmountOfFishConsumption(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<AmountOfFruitConsumption>> amountOfFruitConsumptions = findAmountOfFruitConsumption(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<AmountOfMeatConsumption>> amountOfMeatConsumption = findAmountOfMeatConsumption(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<AmountOfProteinConsumption>> amountOfProteinConsumption = findAmountOfProteinConsumption(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<AmountOfVegetableConsumption>> amountOfVegetableConsumption = findAmountOfVegetableConsumption(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<AttentionFocus>> attentionFocus = findAttentionFocus(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<AttentionState>> attentionState = findAttentionState(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<AverageSleepingHours>> averageSleepingHours = findAverageSleepingHours(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<ChildrenWish>> childrenWish = findChildrenWish(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<CurrentLibido>> currentLibido = findCurrentLibido(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<DailyFourCoffee>> dailyFourCoffee = findDailyFourCoffee(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<DailySixAlcoholicDrinks>> dailySixAlcoholicDrink = findDailySixAlcoholicDrinks(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<DateOfBirthAnswer>> dateOfBirthAnswer = findDateOfBirth(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, List<DietIntolerance>> dietIntolerance = findDietIntolerances(quizExternalReference).orElse(Either.right(Collections.emptyList()));
        final Either<Throwable, Optional<DietType>> dietType = findDietType(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<DigestionAmount>> digestionAmount = findDigestionAmount(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<DigestionOccurrence>> digestionOccurrence = findDigestionOccurrence(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<DrySkin>> drySkin = findDrySkin(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<EnergyState>> energyState = findEnergyState(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<Gender>> gender = findGender(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<HairType>> hairType = findHairType(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<IronPrescribed>> ironPrescribed = findIronPrescribed(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<LackOfConcentration>> lackOfConcentration = findLackOfConcentration(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<LibidoStressLevel>> libidoStressLevel = findLibidoStressLevel(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<MentalFitness>> mentalFitness = findMentalFitness(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<MenstruationInterval>> menstruationInterval = findMenstruationInterval(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<MenstruationMood>> menstruationMood = findMenstruationMood(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<MenstruationSideIssue>> menstruationSideIssue = findMenstruationSideIssue(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<NailImprovement>> nailImprovement = findNailImprovement(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<OftenHavingFlu>> oftenHavingFlu = findOftenHavingFlu(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<PregnancyState>> pregnancyState = findPregnancyState(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<PresentAtCrowdedPlaces>> presentAtCrowdedPlaces = findPresentAtCrowdedPlaces(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<PrimaryGoal>> primaryGoal = findPrimaryGoal(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<SkinProblem>> skinProblem = findSkinProblem(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<SkinType>> skinType = findSkinType(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<SleepQuality>> sleepQuality = findSleepQuality(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<Smoke>> smoke = findSmoke(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<SportAmount>> sportAmount = findSportAmount(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<StressLevelAtEndOfDay>> stressLevelAtEndOfDay = findStressLevelAtEndOfDay(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<ThirtyMinutesOfSun>> thirtyMinutesOfSun = findThirtyMinutesOfSun(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<TiredWhenWakeUp>> tiredWhenWakeUp = findTiredWhenWakeUp(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<TrainingIntensively>> trainingIntensively = findTrainingIntensively(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<TransitionPeriodComplaints>> transitionPeriodComplaints = findTransitionPeriodComplaints(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<TroubleFallingAsleep>> troubleFallingAsleep = findTroubleFallingAsleep(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<UrinaryInfection>> urinaryInfection = findUrinaryInfection(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, List<UsageGoal>> usageGoal = findUsageGoal(quizExternalReference).orElse(Either.right(Collections.emptyList()));
        final Either<Throwable, Optional<VitaminIntake>> vitaminIntake = findVitaminIntake(quizExternalReference).orElse(Either.right(Optional.empty()));
        final Either<Throwable, Optional<WeeklyTwelveAlcoholicDrinks>> weeklyTwelveAlcoholicDrink = findWeeklyTwelveAlcoholicDrinks(quizExternalReference).orElse(Either.right(Optional.empty()));

        if (anyEntityIsMissing(
                acnePlace, allergyTypes, amountOfFiberConsumption, amountOfFishConsumption,
                amountOfFruitConsumptions, amountOfMeatConsumption, amountOfProteinConsumption,
                amountOfVegetableConsumption, attentionFocus, attentionState, averageSleepingHours, childrenWish,
                currentLibido, dailyFourCoffee, dailySixAlcoholicDrink, dateOfBirthAnswer, dietIntolerance, dietType,
                digestionAmount, digestionOccurrence, drySkin, energyState, gender, hairType, ironPrescribed,
                lackOfConcentration, libidoStressLevel, mentalFitness, menstruationInterval, menstruationMood,
                menstruationSideIssue,    nailImprovement, oftenHavingFlu, pregnancyState, presentAtCrowdedPlaces,
                primaryGoal, skinProblem, skinType, sleepQuality, smoke, sportAmount, stressLevelAtEndOfDay,
                thirtyMinutesOfSun, tiredWhenWakeUp, trainingIntensively, transitionPeriodComplaints,
                troubleFallingAsleep, urinaryInfection, usageGoal, vitaminIntake, weeklyTwelveAlcoholicDrink
        )) {
            return Try.failure(new NoSuchElementException());
        } else {
            return Try.success(buildQuizAggregatedInformation(
                    quiz, blendId, acnePlace.get(), allergyTypes.get(), amountOfFiberConsumption.get(),
                    amountOfFishConsumption.get(), amountOfFruitConsumptions.get(), amountOfMeatConsumption.get(),
                    amountOfProteinConsumption.get(), amountOfVegetableConsumption.get(), attentionFocus.get(),
                    attentionState.get(), averageSleepingHours.get(), childrenWish.get(), currentLibido.get(),
                    dailyFourCoffee.get(), dailySixAlcoholicDrink.get(), dateOfBirthAnswer.get(), dietIntolerance.get(),
                    dietType.get(), digestionAmount.get(), digestionOccurrence.get(), drySkin.get(), energyState.get(),
                    gender.get(), hairType.get(), ironPrescribed.get(), lackOfConcentration.get(), libidoStressLevel.get(),
                    mentalFitness.get(), menstruationInterval.get(), menstruationMood.get(), menstruationSideIssue.get(),
                    nailImprovement.get(), oftenHavingFlu.get(), pregnancyState.get(), presentAtCrowdedPlaces.get(),
                    primaryGoal.get(), skinProblem.get(), skinType.get(), sleepQuality.get(), smoke.get(),
                    sportAmount.get(), stressLevelAtEndOfDay.get(), thirtyMinutesOfSun.get(), tiredWhenWakeUp.get(),
                    trainingIntensively.get(), transitionPeriodComplaints.get(), troubleFallingAsleep.get(),
                    urinaryInfection.get(), usageGoal.get(), vitaminIntake.get(), weeklyTwelveAlcoholicDrink.get()
            ));
        }
    }

    private Either<Throwable, Optional<AcnePlace>> findAcnePlace(UUID quizExternalReference) {
        return acnePlaceAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(acnePlaceAnswer -> acnePlaceService.find(acnePlaceAnswer.getAcnePlaceId()));

    }

    private Either<Throwable, List<AllergyType>> findAllergyTypes(UUID quizExternalReference) {
        return allergyTypeAnswerService.find(quizExternalReference)
                .flatMap(answers -> {
                            final List<Either<Throwable, AllergyType>> collect = answers
                                    .stream()
                                    .map(answer -> allergyTypeService.find(answer.getAllergyTypeId())
                                            .flatMap(optionalToNarrowedEither())
                                    ).collect(Collectors.toList());
                            return collapseListOfEitherToOneEither(collect);
                        }
                );
    }

    private Either<Throwable, Optional<AmountOfFiberConsumption>> findAmountOfFiberConsumption(UUID quizExternalReference) {
        return amountOfFiberConsumptionAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(amountOfFiberConsumptionAnswer -> amountOfFiberConsumptionService.find(amountOfFiberConsumptionAnswer.getAmountOfFiberConsumptionId()));
    }

    private Either<Throwable, Optional<AmountOfFishConsumption>> findAmountOfFishConsumption(UUID quizExternalReference) {
        return amountOfFishConsumptionAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(amountOfFishConsumptionAnswer -> amountOfFishConsumptionService.find(amountOfFishConsumptionAnswer.getAmountOfFishConsumptionId()));
    }

    private Either<Throwable, Optional<AmountOfFruitConsumption>> findAmountOfFruitConsumption(UUID quizExternalReference) {
        return amountOfFruitConsumptionAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(amountOfFruitConsumptionAnswer -> amountOfFruitConsumptionService.find(amountOfFruitConsumptionAnswer.getAmountOfFruitConsumptionId()));
    }

    private Either<Throwable, Optional<AmountOfMeatConsumption>> findAmountOfMeatConsumption(UUID quizExternalReference) {
        return amountOfMeatConsumptionAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(amountOfMeatConsumptionAnswer -> amountOfMeatConsumptionService.find(amountOfMeatConsumptionAnswer.getAmountOfMeatConsumptionId()));
    }


    private Either<Throwable, Optional<AmountOfProteinConsumption>> findAmountOfProteinConsumption(UUID quizExternalReference) {
        return amountOfProteinConsumptionAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(amountOfProteinConsumptionAnswer -> amountOfProteinConsumptionService.find(amountOfProteinConsumptionAnswer.getAmountOfProteinConsumptionId()));
    }

    private Either<Throwable, Optional<AmountOfVegetableConsumption>> findAmountOfVegetableConsumption(UUID quizExternalReference) {
        return amountOfVegetableConsumptionAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(amountOfVegetableConsumptionAnswer -> amountOfVegetableConsumptionService.find(amountOfVegetableConsumptionAnswer.getAmountOfVegetableConsumptionId()));
    }

    private Either<Throwable, Optional<AttentionFocus>> findAttentionFocus(UUID quizExternalReference) {
        return attentionFocusAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(attentionFocusAnswer -> attentionFocusService.find(attentionFocusAnswer.getAttentionFocusId()));
    }

    private Either<Throwable, Optional<AttentionState>> findAttentionState(UUID quizExternalReference) {
        return attentionStateAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(attentionStateAnswer -> attentionStateService.find(attentionStateAnswer.getAttentionStateId()));
    }

    private Either<Throwable, Optional<AverageSleepingHours>> findAverageSleepingHours(UUID quizExternalReference) {
        return averageSleepingHoursAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(averageSleepingHoursAnswer -> averageSleepingHoursService.find(averageSleepingHoursAnswer.getAverageSleepingHoursId()));
    }

    private Either<Throwable, Optional<ChildrenWish>> findChildrenWish(UUID quizExternalReference) {
        return childrenWishAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(childrenWishAnswer -> childrenWishService.find(childrenWishAnswer.getChildrenWishId()));
    }

    private Either<Throwable, Optional<CurrentLibido>> findCurrentLibido(UUID quizExternalReference) {
        return currentLibidoAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(currentLibidoAnswer -> currentLibidoService.find(currentLibidoAnswer.getCurrentLibidoId()));
    }

    private Either<Throwable, Optional<DailyFourCoffee>> findDailyFourCoffee(UUID quizExternalReference) {
        return dailyFourCoffeeAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(dailyFourCoffeeAnswer -> dailyFourCoffeeService.find(dailyFourCoffeeAnswer.getDailyFourCoffeeId()));
    }

    private Either<Throwable, Optional<DailySixAlcoholicDrinks>> findDailySixAlcoholicDrinks(UUID quizExternalReference) {
        return dailySixAlcoholicDrinksAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(dailySixAlcoholicDrinksAnswer -> dailySixAlcoholicDrinksService.find(dailySixAlcoholicDrinksAnswer.getDailySixAlcoholicDrinksId()));
    }

    private Either<Throwable, Optional<DateOfBirthAnswer>> findDateOfBirth(UUID quizExternalReference) {
        return dateOfBirthAnswerService.find(quizExternalReference);
    }

    private Either<Throwable, List<DietIntolerance>> findDietIntolerances(UUID quizExternalReference) {
        return dietIntoleranceAnswerService.find(quizExternalReference)
                .flatMap(answers -> {
                            final List<Either<Throwable, DietIntolerance>> collect = answers
                                    .stream()
                                    .map(answer -> dietIntoleranceService.find(answer.getDietIntoleranceId())
                                            .flatMap(optionalToNarrowedEither())
                                    ).collect(Collectors.toList());
                            return collapseListOfEitherToOneEither(collect);
                        }
                );
    }

    private Either<Throwable, Optional<DietType>> findDietType(UUID quizExternalReference) {
        return dietTypeAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(dietTypeAnswer -> dietTypeService.find(dietTypeAnswer.getDietTypeId()));
    }

    private Either<Throwable, Optional<DigestionAmount>> findDigestionAmount(UUID quizExternalReference) {
        return digestionAmountAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(digestionAmountAnswer -> digestionAmountService.find(digestionAmountAnswer.getDigestionAmountId()));
    }

    private Either<Throwable, Optional<DigestionOccurrence>> findDigestionOccurrence(UUID quizExternalReference) {
        return digestionOccurrenceAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(digestionOccurrenceAnswer -> digestionOccurrenceService.find(digestionOccurrenceAnswer.getDigestionOccurrenceId()));
    }

    private Either<Throwable, Optional<DrySkin>> findDrySkin(UUID quizExternalReference) {
        return drySkinAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(drySkinAnswer -> drySkinService.find(drySkinAnswer.getDrySkinId()));
    }

    private Either<Throwable, Optional<EnergyState>> findEnergyState(UUID quizExternalReference) {
        return energyStateAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(energyStateAnswer -> energyStateService.find(energyStateAnswer.getEnergyStateId()));
    }

    private Either<Throwable, Optional<Gender>> findGender(UUID quizExternalReference) {
        return genderAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(genderAnswer -> genderService.find(genderAnswer.getGenderId()));
    }

    private Either<Throwable, Optional<HairType>> findHairType(UUID quizExternalReference) {
        return hairTypeAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(hairTypeAnswer -> hairTypeService.find(hairTypeAnswer.getHairTypeId()));
    }

    private Either<Throwable, Optional<IronPrescribed>> findIronPrescribed(UUID quizExternalReference) {
        return ironPrescribedAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(ironPrescribedAnswer -> ironPrescribedService.find(ironPrescribedAnswer.getIronPrescribedId()));
    }

    private Either<Throwable, Optional<LackOfConcentration>> findLackOfConcentration(UUID quizExternalReference) {
        return lackOfConcentrationAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(lackOfConcentrationAnswer -> lackOfConcentrationService.find(lackOfConcentrationAnswer.getLackOfConcentrationId()));
    }

    private Either<Throwable, Optional<LibidoStressLevel>> findLibidoStressLevel(UUID quizExternalReference) {
        return libidoStressLevelAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(libidoStressLevelAnswer -> libidoStressLevelService.find(libidoStressLevelAnswer.getLibidoStressLevelId()));
    }

    private Either<Throwable, Optional<MentalFitness>> findMentalFitness(UUID quizExternalReference) {
        return mentalFitnessAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(mentalFitnessAnswer -> mentalFitnessService.find(mentalFitnessAnswer.getMentalFitnessId()));
    }

    private Either<Throwable, Optional<MenstruationInterval>> findMenstruationInterval(UUID quizExternalReference) {
        return menstruationIntervalAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(menstruationIntervalAnswer -> menstruationIntervalService.find(menstruationIntervalAnswer.getMenstruationIntervalId()));
    }

    private Either<Throwable, Optional<MenstruationMood>> findMenstruationMood(UUID quizExternalReference) {
        return menstruationMoodAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(menstruationMoodAnswer -> menstruationMoodService.find(menstruationMoodAnswer.getMenstruationMoodId()));
    }

    private Either<Throwable, Optional<MenstruationSideIssue>> findMenstruationSideIssue(UUID quizExternalReference) {
        return menstruationSideIssueAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(menstruationSideIssueAnswer -> menstruationSideIssueService.find(menstruationSideIssueAnswer.getMenstruationSideIssueId()));
    }

    private Either<Throwable, Optional<NailImprovement>> findNailImprovement(UUID quizExternalReference) {
        return nailImprovementAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(nailImprovementAnswer -> nailImprovementService.find(nailImprovementAnswer.getNailImprovementId()));
    }

    private Either<Throwable, Optional<OftenHavingFlu>> findOftenHavingFlu(UUID quizExternalReference) {
        return oftenHavingFluAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(oftenHavingFluAnswer -> oftenHavingFluService.find(oftenHavingFluAnswer.getOftenHavingFluId()));
    }

    private Either<Throwable, Optional<PregnancyState>> findPregnancyState(UUID quizExternalReference) {
        return pregnancyStateAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(pregnancyStateAnswer -> pregnancyStateService.find(pregnancyStateAnswer.getPregnancyStateId()));
    }

    private Either<Throwable, Optional<PresentAtCrowdedPlaces>> findPresentAtCrowdedPlaces(UUID quizExternalReference) {
        return presentAtCrowdedPlacesAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(presentAtCrowdedPlacesAnswer -> presentAtCrowdedPlacesService.find(presentAtCrowdedPlacesAnswer.getPresentAtCrowdedPlacesId()));
    }

    private Either<Throwable, Optional<PrimaryGoal>> findPrimaryGoal(UUID quizExternalReference) {
        return primaryGoalAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(primaryGoalAnswer -> primaryGoalService.find(primaryGoalAnswer.getPrimaryGoalId()));
    }

    private Either<Throwable, Optional<SkinProblem>> findSkinProblem(UUID quizExternalReference) {
        return skinProblemAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(skinProblemAnswer -> skinProblemService.find(skinProblemAnswer.getSkinProblemId()));
    }

    private Either<Throwable, Optional<SkinType>> findSkinType(UUID quizExternalReference) {
        return skinTypeAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(skinTypeAnswer -> skinTypeService.find(skinTypeAnswer.getSkinTypeId()));
    }

    private Either<Throwable, Optional<SleepQuality>> findSleepQuality(UUID quizExternalReference) {
        return sleepQualityAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(sleepQualityAnswer -> sleepQualityService.find(sleepQualityAnswer.getSleepQualityId()));
    }

    private Either<Throwable, Optional<Smoke>> findSmoke(UUID quizExternalReference) {
        return smokeAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(smokeAnswer -> smokeService.find(smokeAnswer.getSmokeId()));
    }

    private Either<Throwable, Optional<SportAmount>> findSportAmount(UUID quizExternalReference) {
        return sportAmountAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(sportAmountAnswer -> sportAmountService.find(sportAmountAnswer.getSportAmountId()));
    }

    private Either<Throwable, Optional<StressLevelAtEndOfDay>> findStressLevelAtEndOfDay(UUID quizExternalReference) {
        return stressLevelAtEndOfDayAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(stressLevelAtEndOfDayAnswer -> stressLevelAtEndOfDayService.find(stressLevelAtEndOfDayAnswer.getStressLevelAtEndOfDayId()));
    }

    private Either<Throwable, Optional<ThirtyMinutesOfSun>> findThirtyMinutesOfSun(UUID quizExternalReference) {
        return thirtyMinutesOfSunAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(thirtyMinutesOfSunAnswer -> thirtyMinutesOfSunService.find(thirtyMinutesOfSunAnswer.getThirtyMinutesOfSunId()));
    }

    private Either<Throwable, Optional<TiredWhenWakeUp>> findTiredWhenWakeUp(UUID quizExternalReference) {
        return tiredWhenWakeUpAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(tiredWhenWakeUpAnswer -> tiredWhenWakeUpService.find(tiredWhenWakeUpAnswer.getTiredWhenWakeUpId()));
    }

    private Either<Throwable, Optional<TrainingIntensively>> findTrainingIntensively(UUID quizExternalReference) {
        return trainingIntensivelyAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(trainingIntensivelyAnswer -> trainingIntensivelyService.find(trainingIntensivelyAnswer.getTrainingIntensivelyId()));
    }

    private Either<Throwable, Optional<TransitionPeriodComplaints>> findTransitionPeriodComplaints(UUID quizExternalReference) {
        return transitionPeriodComplaintsAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(transitionPeriodComplaintsAnswer -> transitionPeriodComplaintsService.find(transitionPeriodComplaintsAnswer.getTransitionPeriodComplaintsId()));
    }

    private Either<Throwable, Optional<TroubleFallingAsleep>> findTroubleFallingAsleep(UUID quizExternalReference) {
        return troubleFallingAsleepAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(troubleFallingAsleepAnswer -> troubleFallingAsleepService.find(troubleFallingAsleepAnswer.getTroubleFallingAsleepId()));
    }

    private Either<Throwable, Optional<UrinaryInfection>> findUrinaryInfection(UUID quizExternalReference) {
        return urinaryInfectionAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(urinaryInfectionAnswer -> urinaryInfectionService.find(urinaryInfectionAnswer.getUrinaryInfectionId()));
    }

    private Either<Throwable, List<UsageGoal>> findUsageGoal(UUID quizExternalReference) {
        return usageGoalAnswerService.find(quizExternalReference)
                .flatMap(answers -> {
                            final List<Either<Throwable, UsageGoal>> collect = answers
                                    .stream()
                                    .map(answer -> usageGoalService.find(answer.getUsageGoalId())
                                            .flatMap(optionalToNarrowedEither())
                                    ).collect(Collectors.toList());
                            return collapseListOfEitherToOneEither(collect);
                        }
                );
    }

    private Either<Throwable, Optional<VitaminIntake>> findVitaminIntake(UUID quizExternalReference) {
        return vitaminIntakeAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(vitaminIntakeAnswer -> vitaminIntakeService.find(vitaminIntakeAnswer.getVitaminIntakeId()));
    }

    private Either<Throwable, Optional<WeeklyTwelveAlcoholicDrinks>> findWeeklyTwelveAlcoholicDrinks(UUID quizExternalReference) {
        return weeklyTwelveAlcoholicDrinksAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(weeklyTwelveAlcoholicDrinksAnswer -> weeklyTwelveAlcoholicDrinksService.find(weeklyTwelveAlcoholicDrinksAnswer.getWeeklyTwelveAlcoholicDrinksId()));
    }

    private QuizAggregatedInformationV2 buildQuizAggregatedInformation(
            Quiz quiz, Long blendId, Optional<AcnePlace> acnePlace, List<AllergyType> allergyTypes,
            Optional<AmountOfFiberConsumption> amountOfFiberConsumption,
            Optional<AmountOfFishConsumption> amountOfFishConsumption,
            Optional<AmountOfFruitConsumption> amountOfFruitConsumption,
            Optional<AmountOfMeatConsumption> amountOfMeatConsumption,
            Optional<AmountOfProteinConsumption> amountOfProteinConsumption,
            Optional<AmountOfVegetableConsumption> amountOfVegetableConsumption,
            Optional<AttentionFocus> attentionFocus, Optional<AttentionState> attentionState,
            Optional<AverageSleepingHours> averageSleepingHours, Optional<ChildrenWish> childrenWish,
            Optional<CurrentLibido> currentLibido, Optional<DailyFourCoffee> dailyFourCoffee,
            Optional<DailySixAlcoholicDrinks> dailySixAlcoholicDrinks, Optional<DateOfBirthAnswer> dateOfBirthAnswer,
            List<DietIntolerance> dietIntolerances, Optional<DietType> dietType, Optional<DigestionAmount> digestionAmount,
            Optional<DigestionOccurrence> digestionOccurrence, Optional<DrySkin> drySkin,
            Optional<EnergyState> energyState, Optional<Gender> gender, Optional<HairType> hairType, Optional<IronPrescribed> ironPrescribed,
            Optional<LackOfConcentration> lackOfConcentration, Optional<LibidoStressLevel> libidoStressLevel,
            Optional<MentalFitness> mentalFitness, Optional<MenstruationInterval> menstruationInterval,
            Optional<MenstruationMood> menstruationMood, Optional<MenstruationSideIssue> menstruationSideIssue,
            Optional<NailImprovement> nailImprovement, Optional<OftenHavingFlu> oftenHavingFlu,
            Optional<PregnancyState> pregnancyState, Optional<PresentAtCrowdedPlaces> presentAtCrowdedPlaces,
            Optional<PrimaryGoal> primaryGoal, Optional<SkinProblem> skinProblem, Optional<SkinType> skinType,
            Optional<SleepQuality> sleepQuality, Optional<Smoke> smoke, Optional<SportAmount> sportAmount,
            Optional<StressLevelAtEndOfDay> stressLevelAtEndOfDay, Optional<ThirtyMinutesOfSun> thirtyMinutesOfSun,
            Optional<TiredWhenWakeUp> tiredWhenWakeUp, Optional<TrainingIntensively> trainingIntensively,
            Optional<TransitionPeriodComplaints> transitionPeriodComplaints,
            Optional<TroubleFallingAsleep> troubleFallingAsleep, Optional<UrinaryInfection> urinaryInfection,
            List<UsageGoal> usageGoals, Optional<VitaminIntake> vitaminIntake,
            Optional<WeeklyTwelveAlcoholicDrinks> weeklyTwelveAlcoholicDrinks) {

        return new QuizAggregatedInformationV2(quiz, blendId, acnePlace, allergyTypes, amountOfFiberConsumption,
                amountOfFishConsumption, amountOfFruitConsumption, amountOfMeatConsumption, amountOfProteinConsumption,
                amountOfVegetableConsumption, attentionFocus, attentionState, averageSleepingHours, childrenWish,
                currentLibido, dailyFourCoffee, dailySixAlcoholicDrinks, dateOfBirthAnswer, dietIntolerances, dietType,
                digestionAmount, digestionOccurrence, drySkin, energyState, gender, hairType, ironPrescribed,
                lackOfConcentration, libidoStressLevel, mentalFitness, menstruationInterval, menstruationMood,
                menstruationSideIssue, nailImprovement, oftenHavingFlu, pregnancyState, presentAtCrowdedPlaces,
                primaryGoal, skinProblem, skinType, sleepQuality, smoke, sportAmount, stressLevelAtEndOfDay,
                thirtyMinutesOfSun, tiredWhenWakeUp, trainingIntensively, transitionPeriodComplaints,
                troubleFallingAsleep, urinaryInfection, usageGoals, vitaminIntake, weeklyTwelveAlcoholicDrinks
        );
    }

    private <T> Either<Throwable, List<T>> collapseListOfEitherToOneEither(List<Either<Throwable, T>> collect) {
        return Either.sequenceRight(collect)
                .map(Seq::asJava);
    }

    @SafeVarargs
    private boolean anyEntityIsMissing(Either<Throwable, ?>... eitherArray) {
        final boolean anyEntityIsMissing = Stream.of(eitherArray).anyMatch(Either::isLeft);
        if (anyEntityIsMissing) {
            LOGGER.error("QuizAggregatedInformationV2 entity missing in array ={}", Arrays.toString(eitherArray));
            return true;
        } else {
            return false;
        }
    }
}
