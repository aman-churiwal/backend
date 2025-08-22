package viteezy.service.quiz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import viteezy.db.quiz.*;
import viteezy.gateways.facebook.FacebookService;
import viteezy.service.CustomerService;

@Configuration("quizServicesConfig")
@Import({
        viteezy.db.IoC.class
})
public class IoC {

    @Bean("acnePlaceService")
    public AcnePlaceService acnePlaceService(
            AcnePlaceRepository acnePlaceRepository
    ) {
        return new AcnePlaceServiceImpl(acnePlaceRepository);
    }

    @Bean("acnePlaceAnswerService")
    public AcnePlaceAnswerService acnePlaceAnswerService(
            AcnePlaceAnswerRepository acnePlaceAnswerRepository,
            QuizService quizService
    ) {
        return new AcnePlaceAnswerServiceImpl(acnePlaceAnswerRepository, quizService);
    }

    @Bean("allergyTypeService")
    public AllergyTypeService allergyTypeService(
            AllergyTypeRepository allergyTypeRepository
    ) {
        return new AllergyTypeServiceImpl(allergyTypeRepository);
    }

    @Bean("allergyTypeAnswerService")
    public AllergyTypeAnswerService allergyTypeAnswerService(
            AllergyTypeAnswerRepository allergyTypeAnswerRepository,
            QuizRepository quizRepository
    ) {
        return new AllergyTypeAnswerServiceImpl(allergyTypeAnswerRepository, quizRepository);
    }

    @Bean("amountOfDairyConsumptionService")
    public AmountOfDairyConsumptionService amountOfDairyConsumptionService(
            AmountOfDairyConsumptionRepository amountOfDairyConsumptionRepository
    ) {
        return new AmountOfDairyConsumptionServiceImpl(amountOfDairyConsumptionRepository);
    }

    @Bean("amountOfDairyConsumptionAnswerService")
    public AmountOfDairyConsumptionAnswerService amountOfDairyConsumptionAnswerService(
            AmountOfDairyConsumptionAnswerRepository amountOfDairyConsumptionAnswerRepository,
            QuizService quizService
    ) {
        return new AmountOfDairyConsumptionAnswerServiceImpl(amountOfDairyConsumptionAnswerRepository, quizService);
    }

    @Bean("amountOfFiberConsumptionService")
    public AmountOfFiberConsumptionService amountOfFiberConsumptionService(
            AmountOfFiberConsumptionRepository amountOfFiberConsumptionRepository
    ) {
        return new AmountOfFiberConsumptionServiceImpl(amountOfFiberConsumptionRepository);
    }

    @Bean("amountOfFiberConsumptionAnswerService")
    public AmountOfFiberConsumptionAnswerService amountOfFiberConsumptionAnswerService(
            AmountOfFiberConsumptionAnswerRepository amountOfFiberConsumptionAnswerRepository,
            QuizService quizService
    ) {
        return new AmountOfFiberConsumptionAnswerServiceImpl(amountOfFiberConsumptionAnswerRepository, quizService);
    }

    @Bean("amountOfFishConsumptionService")
    public AmountOfFishConsumptionService amountOfFishConsumptionService(
            AmountOfFishConsumptionRepository amountOfFishConsumptionRepository
    ) {
        return new AmountOfFishConsumptionServiceImpl(amountOfFishConsumptionRepository);
    }

    @Bean("amountOfFishConsumptionAnswerService")
    public AmountOfFishConsumptionAnswerService amountOfFishConsumptionAnswerService(
            AmountOfFishConsumptionAnswerRepository amountOfFishConsumptionAnswerRepository,
            QuizService quizService
    ) {
        return new AmountOfFishConsumptionAnswerServiceImpl(amountOfFishConsumptionAnswerRepository, quizService);
    }

    @Bean("amountOfFruitConsumptionService")
    public AmountOfFruitConsumptionService amountOfFruitConsumptionService(
            AmountOfFruitConsumptionRepository amountOfFruitConsumptionRepository
    ) {
        return new AmountOfFruitConsumptionServiceImpl(amountOfFruitConsumptionRepository);
    }

    @Bean("amountOfFruitConsumptionAnswerService")
    public AmountOfFruitConsumptionAnswerService amountOfFruitConsumptionAnswerService(
            AmountOfFruitConsumptionAnswerRepository amountOfFruitConsumptionAnswerRepository,
            QuizService quizService
    ) {
        return new AmountOfFruitConsumptionAnswerServiceImpl(amountOfFruitConsumptionAnswerRepository, quizService);
    }

    @Bean("amountOfMeatConsumptionService")
    public AmountOfMeatConsumptionService amountOfMeatConsumptionService(
            AmountOfMeatConsumptionRepository amountOfMeatConsumptionRepository
    ) {
        return new AmountOfMeatConsumptionServiceImpl(amountOfMeatConsumptionRepository);
    }

    @Bean("amountOfMeatConsumptionAnswerService")
    public AmountOfMeatConsumptionAnswerService amountOfMeatConsumptionAnswerService(
            AmountOfMeatConsumptionAnswerRepository amountOfMeatConsumptionAnswerRepository,
            QuizService quizService
    ) {
        return new AmountOfMeatConsumptionAnswerServiceImpl(amountOfMeatConsumptionAnswerRepository, quizService);
    }

    @Bean("amountOfProteinConsumptionService")
    public AmountOfProteinConsumptionService amountOfProteinConsumptionService(
            AmountOfProteinConsumptionRepository amountOfProteinConsumptionRepository
    ) {
        return new AmountOfProteinConsumptionServiceImpl(amountOfProteinConsumptionRepository);
    }

    @Bean("amountOfProteinConsumptionAnswerService")
    public AmountOfProteinConsumptionAnswerService amountOfProteinConsumptionAnswerService(
            AmountOfProteinConsumptionAnswerRepository amountOfProteinConsumptionAnswerRepository,
            QuizService quizService
    ) {
        return new AmountOfProteinConsumptionAnswerServiceImpl(amountOfProteinConsumptionAnswerRepository, quizService);
    }

    @Bean("amountOfVegetableConsumptionService")
    public AmountOfVegetableConsumptionService amountOfVegetableConsumptionService(
            AmountOfVegetableConsumptionRepository amountOfVegetableConsumptionRepository
    ) {
        return new AmountOfVegetableConsumptionServiceImpl(amountOfVegetableConsumptionRepository);
    }

    @Bean("amountOfVegetableConsumptionAnswerService")
    public AmountOfVegetableConsumptionAnswerService amountOfVegetableConsumptionAnswerService(
            AmountOfVegetableConsumptionAnswerRepository amountOfVegetableConsumptionAnswerRepository,
            QuizService quizService
    ) {
        return new AmountOfVegetableConsumptionAnswerServiceImpl(amountOfVegetableConsumptionAnswerRepository, quizService);
    }

    @Bean("attentionFocusService")
    public AttentionFocusService attentionFocusService(
            AttentionFocusRepository attentionFocusRepository
    ) {
        return new AttentionFocusServiceImpl(attentionFocusRepository);
    }

    @Bean("attentionFocusAnswerService")
    public AttentionFocusAnswerService attentionFocusAnswerService(
            AttentionFocusAnswerRepository attentionFocusAnswerRepository,
            QuizService quizService
    ) {
        return new AttentionFocusAnswerServiceImpl(attentionFocusAnswerRepository, quizService);
    }

    @Bean("attentionStateService")
    public AttentionStateService attentionStateService(
            AttentionStateRepository attentionStateRepository
    ) {
        return new AttentionStateServiceImpl(attentionStateRepository);
    }

    @Bean("attentionStateAnswerService")
    public AttentionStateAnswerService attentionStateAnswerService(
            AttentionStateAnswerRepository attentionStateAnswerRepository,
            QuizService quizService
    ) {
        return new AttentionStateAnswerServiceImpl(attentionStateAnswerRepository, quizService);
    }

    @Bean("averageSleepingHoursService")
    public AverageSleepingHoursService averageSleepingHoursService(
            AverageSleepingHoursRepository averageSleepingHoursRepository
    ) {
        return new AverageSleepingHoursServiceImpl(averageSleepingHoursRepository);
    }

    @Bean("averageSleepingHoursAnswerService")
    public AverageSleepingHoursAnswerService averageSleepingHoursAnswerService(
            AverageSleepingHoursAnswerRepository averageSleepingHoursAnswerRepository,
            QuizService quizService
    ) {
        return new AverageSleepingHoursAnswerServiceImpl(averageSleepingHoursAnswerRepository, quizService);
    }

    @Bean("bingeEatingService")
    public BingeEatingService bingeEatingService(
            BingeEatingRepository bingeEatingRepository
    ) {
        return new BingeEatingServiceImpl(bingeEatingRepository);
    }

    @Bean("bingeEatingAnswerService")
    public BingeEatingAnswerService bingeEatingAnswerService(
            BingeEatingAnswerRepository bingeEatingAnswerRepository,
            QuizService quizService
    ) {
        return new BingeEatingAnswerServiceImpl(bingeEatingAnswerRepository, quizService);
    }

    @Bean("bingeEatingReasonService")
    public BingeEatingReasonService bingeEatingReasonService(
            BingeEatingReasonRepository bingeEatingReasonRepository
    ) {
        return new BingeEatingReasonServiceImpl(bingeEatingReasonRepository);
    }

    @Bean("bingeEatingReasonAnswerService")
    public BingeEatingReasonAnswerService bingeEatingReasonAnswerService(
            BingeEatingReasonAnswerRepository bingeEatingReasonAnswerRepository,
            QuizService quizService
    ) {
        return new BingeEatingReasonAnswerServiceImpl(bingeEatingReasonAnswerRepository, quizService);
    }

    @Bean("birthHealthService")
    public BirthHealthService birthHealthService(
            BirthHealthRepository birthHealthRepository
    ) {
        return new BirthHealthServiceImpl(birthHealthRepository);
    }

    @Bean("birthHealthAnswerService")
    public BirthHealthAnswerService birthHealthAnswerService(
            BirthHealthAnswerRepository birthHealthAnswerRepository,
            QuizService quizService
    ) {
        return new BirthHealthAnswerServiceImpl(birthHealthAnswerRepository, quizService);
    }

    @Bean("childrenWishService")
    public ChildrenWishService childrenWishService(
            ChildrenWishRepository childrenWishRepository
    ) {
        return new ChildrenWishServiceImpl(childrenWishRepository);
    }

    @Bean("childrenWishAnswerService")
    public ChildrenWishAnswerService childrenWishAnswerService(
            ChildrenWishAnswerRepository childrenWishAnswerRepository,
            QuizService quizService
    ) {
        return new ChildrenWishAnswerServiceImpl(childrenWishAnswerRepository, quizService);
    }

    @Bean("currentLibidoService")
    public CurrentLibidoService currentLibidoService(
            CurrentLibidoRepository currentLibidoRepository
    ) {
        return new CurrentLibidoServiceImpl(currentLibidoRepository);
    }

    @Bean("currentLibidoAnswerService")
    public CurrentLibidoAnswerService currentLibidoAnswerService(
            CurrentLibidoAnswerRepository currentLibidoAnswerRepository,
            QuizService quizService
    ) {
        return new CurrentLibidoAnswerServiceImpl(currentLibidoAnswerRepository, quizService);
    }

    @Bean("dailyFourCoffeeService")
    public DailyFourCoffeeService dailyFourCoffeeService(
            DailyFourCoffeeRepository dailyFourCoffeeRepository
    ) {
        return new DailyFourCoffeeServiceImpl(dailyFourCoffeeRepository);
    }

    @Bean("dailyFourCoffeeAnswerService")
    public DailyFourCoffeeAnswerService dailyFourCoffeeAnswerService(
            DailyFourCoffeeAnswerRepository dailyFourCoffeeAnswerRepository,
            QuizService quizService
    ) {
        return new DailyFourCoffeeAnswerServiceImpl(dailyFourCoffeeAnswerRepository, quizService);
    }

    @Bean("dailySixAlcoholicDrinksService")
    public DailySixAlcoholicDrinksService dailySixAlcoholicDrinksService(
            DailySixAlcoholicDrinksRepository dailySixAlcoholicDrinksRepository
    ) {
        return new DailySixAlcoholicDrinksServiceImpl(dailySixAlcoholicDrinksRepository);
    }

    @Bean("dailySixAlcoholicDrinksAnswerService")
    public DailySixAlcoholicDrinksAnswerService dailySixAlcoholicDrinksAnswerService(
            DailySixAlcoholicDrinksAnswerRepository dailySixAlcoholicDrinksAnswerRepository,
            QuizService quizService
    ) {
        return new DailySixAlcoholicDrinksAnswerServiceImpl(dailySixAlcoholicDrinksAnswerRepository, quizService);
    }

    @Bean("dateOfBirthAnswerService")
    public DateOfBirthAnswerService dateOfBirthAnswerService(
            DateOfBirthAnswerRepository dateOfBirthAnswerRepository, QuizRepository quizRepository
    ) {
        return new DateOfBirthAnswerServiceImpl(dateOfBirthAnswerRepository, quizRepository);
    }

    @Bean("dietIntoleranceService")
    public DietIntoleranceService dietIntoleranceService(
            DietIntoleranceRepository dietIntoleranceRepository
    ) {
        return new DietIntoleranceServiceImpl(dietIntoleranceRepository);
    }

    @Bean("dietIntoleranceAnswerService")
    public DietIntoleranceAnswerService dietIntoleranceAnswerService(
            DietIntoleranceAnswerRepository dietIntoleranceAnswerRepository,
            QuizService quizService
    ) {
        return new DietIntoleranceAnswerServiceImpl(dietIntoleranceAnswerRepository, quizService);
    }

    @Bean("dietTypeService")
    public DietTypeService dietTypeService(
            DietTypeRepository dietTypeRepository
    ) {
        return new DietTypeServiceImpl(dietTypeRepository);
    }

    @Bean("dietTypeAnswerService")
    public DietTypeAnswerService dietTypeAnswerService(
            DietTypeAnswerRepository dietTypeAnswerRepository,
            QuizRepository quizRepository
    ) {
        return new DietTypeAnswerServiceImpl(dietTypeAnswerRepository, quizRepository);
    }

    @Bean("digestionAmountService")
    public DigestionAmountService digestionAmountService(
            DigestionAmountRepository digestionAmountRepository
    ) {
        return new DigestionAmountServiceImpl(digestionAmountRepository);
    }

    @Bean("digestionAmountAnswerService")
    public DigestionAmountAnswerService digestionAmountAnswerService(
            DigestionAmountAnswerRepository digestionAmountAnswerRepository,
            QuizService quizService
    ) {
        return new DigestionAmountAnswerServiceImpl(digestionAmountAnswerRepository, quizService);
    }

    @Bean("digestionOccurrenceService")
    public DigestionOccurrenceService digestionOccurrenceService(
            DigestionOccurrenceRepository digestionOccurrenceRepository
    ) {
        return new DigestionOccurrenceServiceImpl(digestionOccurrenceRepository);
    }

    @Bean("digestionOccurrenceAnswerService")
    public DigestionOccurrenceAnswerService digestionOccurrenceAnswerService(
            DigestionOccurrenceAnswerRepository digestionOccurrenceAnswerRepository,
            QuizService quizService
    ) {
        return new DigestionOccurrenceAnswerServiceImpl(digestionOccurrenceAnswerRepository, quizService);
    }

    @Bean("drySkinService")
    public DrySkinService drySkinService(
            DrySkinRepository drySkinRepository
    ) {
        return new DrySkinServiceImpl(drySkinRepository);
    }

    @Bean("drySkinAnswerService")
    public DrySkinAnswerService drySkinAnswerService(
            DrySkinAnswerRepository drySkinAnswerRepository,
            QuizService quizService
    ) {
        return new DrySkinAnswerServiceImpl(drySkinAnswerRepository, quizService);
    }

    @Bean("easternMedicineOpinionService")
    public EasternMedicineOpinionService easternMedicineOpinionService(
            EasternMedicineOpinionRepository easternMedicineOpinionRepository
    ) {
        return new EasternMedicineOpinionServiceImpl(easternMedicineOpinionRepository);
    }

    @Bean("easternMedicineOpinionAnswerService")
    public EasternMedicineOpinionAnswerService easternMedicineOpinionAnswerService(
            EasternMedicineOpinionAnswerRepository easternMedicineOpinionAnswerRepository,
            QuizService quizService
    ) {
        return new EasternMedicineOpinionAnswerServiceImpl(easternMedicineOpinionAnswerRepository, quizService);
    }

    @Bean("energyStateService")
    public EnergyStateService energyStateService(
            EnergyStateRepository energyStateRepository
    ) {
        return new EnergyStateServiceImpl(energyStateRepository);
    }

    @Bean("energyStateAnswerService")
    public EnergyStateAnswerService energyStateAnswerService(
            EnergyStateAnswerRepository energyStateAnswerRepository,
            QuizService quizService
    ) {
        return new EnergyStateAnswerServiceImpl(energyStateAnswerRepository, quizService);
    }

    @Bean("genderService")
    public GenderService genderService(GenderRepository genderRepository) {
        return new GenderServiceImpl(genderRepository);
    }

    @Bean("genderAnswerService")
    public GenderAnswerService genderAnswerService(
            GenderAnswerRepository genderAnswerRepository,
            QuizService quizService
    ) {
        return new GenderAnswerServiceImpl(genderAnswerRepository, quizService);
    }

    @Bean("hairTypeService")
    public HairTypeService hairTypeService(
            HairTypeRepository hairTypeRepository
    ) {
        return new HairTypeServiceImpl(hairTypeRepository);
    }

    @Bean("hairTypeAnswerService")
    public HairTypeAnswerService hairTypeAnswerService(
            HairTypeAnswerRepository hairTypeAnswerRepository,
            QuizService quizService
    ) {
        return new HairTypeAnswerServiceImpl(hairTypeAnswerRepository, quizService);
    }

    @Bean("healthComplaintsService")
    public HealthComplaintsService healthComplaintsService(
            HealthComplaintsRepository healthComplaintsRepository
    ) {
        return new HealthComplaintsServiceImpl(healthComplaintsRepository);
    }

    @Bean("healthComplaintsAnswerService")
    public HealthComplaintsAnswerService healthComplaintsAnswerService(
            HealthComplaintsAnswerRepository healthComplaintsAnswerRepository,
            QuizService quizService
    ) {
        return new HealthComplaintsAnswerServiceImpl(healthComplaintsAnswerRepository, quizService);
    }

    @Bean("healthyLifestyleService")
    public HealthyLifestyleService healthyLifestyleService(
            HealthyLifestyleRepository healthyLifestyleRepository
    ) {
        return new HealthyLifestyleServiceImpl(healthyLifestyleRepository);
    }

    @Bean("healthyLifestyleAnswerService")
    public HealthyLifestyleAnswerService healthyLifestyleAnswerService(
            HealthyLifestyleAnswerRepository healthyLifestyleAnswerRepository,
            QuizService quizService
    ) {
        return new HealthyLifestyleAnswerServiceImpl(healthyLifestyleAnswerRepository, quizService);
    }

    @Bean("helpGoalService")
    public HelpGoalService helpGoalService(
            HelpGoalRepository helpGoalRepository
    ) {
        return new HelpGoalServiceImpl(helpGoalRepository);
    }

    @Bean("helpGoalAnswerService")
    public HelpGoalAnswerService helpGoalAnswerService(
            HelpGoalAnswerRepository helpGoalAnswerRepository,
            QuizService quizService
    ) {
        return new HelpGoalAnswerServiceImpl(helpGoalAnswerRepository, quizService);
    }

    @Bean("ironPrescribedService")
    public IronPrescribedService ironPrescribedService(
            IronPrescribedRepository ironPrescribedRepository
    ) {
        return new IronPrescribedServiceImpl(ironPrescribedRepository);
    }

    @Bean("ironPrescribedAnswerService")
    public IronPrescribedAnswerService ironPrescribedAnswerService(
            IronPrescribedAnswerRepository ironPrescribedAnswerRepository,
            QuizService quizService
    ) {
        return new IronPrescribedAnswerServiceImpl(ironPrescribedAnswerRepository, quizService);
    }

    @Bean("lackOfConcentrationService")
    public LackOfConcentrationService lackOfConcentrationService(
            LackOfConcentrationRepository lackOfConcentrationRepository
    ) {
        return new LackOfConcentrationServiceImpl(lackOfConcentrationRepository);
    }

    @Bean("lackOfConcentrationAnswerService")
    public LackOfConcentrationAnswerService lackOfConcentrationAnswerService(
            LackOfConcentrationAnswerRepository lackOfConcentrationAnswerRepository,
            QuizService quizService
    ) {
        return new LackOfConcentrationAnswerServiceImpl(lackOfConcentrationAnswerRepository, quizService);
    }

    @Bean("libidoStressLevelService")
    public LibidoStressLevelService libidoStressLevelService(
            LibidoStressLevelRepository libidoStressLevelRepository
    ) {
        return new LibidoStressLevelServiceImpl(libidoStressLevelRepository);
    }

    @Bean("libidoStressLevelAnswerService")
    public LibidoStressLevelAnswerService libidoStressLevelAnswerService(
            LibidoStressLevelAnswerRepository libidoStressLevelAnswerRepository,
            QuizService quizService
    ) {
        return new LibidoStressLevelAnswerServiceImpl(libidoStressLevelAnswerRepository, quizService);
    }

    @Bean("loseWeightChallengeService")
    public LoseWeightChallengeService loseWeightChallengeService(
            LoseWeightChallengeRepository loseWeightChallengeRepository
    ) {
        return new LoseWeightChallengeServiceImpl(loseWeightChallengeRepository);
    }

    @Bean("loseWeightChallengeAnswerService")
    public LoseWeightChallengeAnswerService loseWeightChallengeAnswerService(
            LoseWeightChallengeAnswerRepository loseWeightChallengeAnswerRepository,
            QuizService quizService
    ) {
        return new LoseWeightChallengeAnswerServiceImpl(loseWeightChallengeAnswerRepository, quizService);
    }

    @Bean("menstruationIntervalService")
    public MenstruationIntervalService menstruationIntervalService(
            MenstruationIntervalRepository menstruationIntervalRepository
    ) {
        return new MenstruationIntervalServiceImpl(menstruationIntervalRepository);
    }

    @Bean("menstruationIntervalAnswerService")
    public MenstruationIntervalAnswerService menstruationIntervalAnswerService(
            MenstruationIntervalAnswerRepository menstruationIntervalAnswerRepository,
            QuizService quizService
    ) {
        return new MenstruationIntervalAnswerServiceImpl(menstruationIntervalAnswerRepository, quizService);
    }

    @Bean("menstruationMoodService")
    public MenstruationMoodService menstruationMoodService(
            MenstruationMoodRepository menstruationMoodRepository
    ) {
        return new MenstruationMoodServiceImpl(menstruationMoodRepository);
    }

    @Bean("menstruationMoodAnswerService")
    public MenstruationMoodAnswerService menstruationMoodAnswerService(
            MenstruationMoodAnswerRepository menstruationMoodAnswerRepository,
            QuizService quizService
    ) {
        return new MenstruationMoodAnswerServiceImpl(menstruationMoodAnswerRepository, quizService);
    }

    @Bean("menstruationSideIssueService")
    public MenstruationSideIssueService menstruationSideIssueService(
            MenstruationSideIssueRepository menstruationSideIssueRepository
    ) {
        return new MenstruationSideIssueServiceImpl(menstruationSideIssueRepository);
    }

    @Bean("menstruationSideIssueAnswerService")
    public MenstruationSideIssueAnswerService menstruationSideIssueAnswerService(
            MenstruationSideIssueAnswerRepository menstruationSideIssueAnswerRepository,
            QuizService quizService
    ) {
        return new MenstruationSideIssueAnswerServiceImpl(menstruationSideIssueAnswerRepository, quizService);
    }

    @Bean("mentalFitnessService")
    public MentalFitnessService mentalFitnessService(
            MentalFitnessRepository mentalFitnessRepository
    ) {
        return new MentalFitnessServiceImpl(mentalFitnessRepository);
    }

    @Bean("mentalFitnessAnswerService")
    public MentalFitnessAnswerService mentalFitnessAnswerService(
            MentalFitnessAnswerRepository mentalFitnessAnswerRepository,
            QuizService quizService
    ) {
        return new MentalFitnessAnswerServiceImpl(mentalFitnessAnswerRepository, quizService);
    }

    @Bean("nailImprovementService")
    public NailImprovementService nailImprovementService(
            NailImprovementRepository nailImprovementRepository
    ) {
        return new NailImprovementServiceImpl(nailImprovementRepository);
    }

    @Bean("nailImprovementAnswerService")
    public NailImprovementAnswerService nailImprovementAnswerService(
            NailImprovementAnswerRepository nailImprovementAnswerRepository,
            QuizService quizService
    ) {
        return new NailImprovementAnswerServiceImpl(nailImprovementAnswerRepository, quizService);
    }

    @Bean("nameAnswerService")
    public NameAnswerService nameAnswerService(
            NameAnswerRepository nameAnswerRepository, QuizRepository quizRepository
    ) {
        return new NameAnswerServiceImpl(nameAnswerRepository, quizRepository);
    }

    @Bean("newProductAvailableService")
    public NewProductAvailableService newProductAvailableService(
            NewProductAvailableRepository newProductAvailableRepository
    ) {
        return new NewProductAvailableServiceImpl(newProductAvailableRepository);
    }

    @Bean("newProductAvailableAnswerService")
    public NewProductAvailableAnswerService newProductAvailableAnswerService(
            NewProductAvailableAnswerRepository newProductAvailableAnswerRepository,
            QuizService quizService
    ) {
        return new NewProductAvailableAnswerServiceImpl(newProductAvailableAnswerRepository, quizService);
    }

    @Bean("oftenHavingFluService")
    public OftenHavingFluService oftenHavingFluService(
            OftenHavingFluRepository oftenHavingFluRepository
    ) {
        return new OftenHavingFluServiceImpl(oftenHavingFluRepository);
    }

    @Bean("oftenHavingFluAnswerService")
    public OftenHavingFluAnswerService oftenHavingFluAnswerService(
            OftenHavingFluAnswerRepository oftenHavingFluAnswerRepository,
            QuizService quizService
    ) {
        return new OftenHavingFluAnswerServiceImpl(oftenHavingFluAnswerRepository, quizService);
    }

    @Bean("pregnancyStateService")
    public PregnancyStateService pregnancyStateService(
            PregnancyStateRepository pregnancyStateRepository
    ) {
        return new PregnancyStateServiceImpl(pregnancyStateRepository);
    }

    @Bean("pregnancyStateAnswerService")
    public PregnancyStateAnswerService pregnancyStateAnswerService(
            PregnancyStateAnswerRepository pregnancyStateAnswerRepository,
            QuizService quizService
    ) {
        return new PregnancyStateAnswerServiceImpl(pregnancyStateAnswerRepository, quizService);
    }

    @Bean("presentAtCrowdedPlacesService")
    public PresentAtCrowdedPlacesService presentAtCrowdedPlacesService(
            PresentAtCrowdedPlacesRepository presentAtCrowdedPlacesRepository
    ) {
        return new PresentAtCrowdedPlacesServiceImpl(presentAtCrowdedPlacesRepository);
    }

    @Bean("presentAtCrowdedPlacesAnswerService")
    public PresentAtCrowdedPlacesAnswerService presentAtCrowdedPlacesAnswerService(
            PresentAtCrowdedPlacesAnswerRepository presentAtCrowdedPlacesAnswerRepository,
            QuizService quizService
    ) {
        return new PresentAtCrowdedPlacesAnswerServiceImpl(presentAtCrowdedPlacesAnswerRepository, quizService);
    }

    @Bean("primaryGoalService")
    public PrimaryGoalService primaryGoalService(
            PrimaryGoalRepository primaryGoalRepository
    ) {
        return new PrimaryGoalServiceImpl(primaryGoalRepository);
    }

    @Bean("primaryGoalAnswerService")
    public PrimaryGoalAnswerService primaryGoalAnswerService(
            PrimaryGoalAnswerRepository primaryGoalAnswerRepository,
            QuizRepository quizRepository
    ) {
        return new PrimaryGoalAnswerServiceImpl(primaryGoalAnswerRepository, quizRepository);
    }

    @Bean("quizService")
    public QuizService quizService(
            QuizRepository quizRepository, CustomerService customerService, FacebookService facebookService) {
        return new QuizServiceImpl(
                quizRepository, customerService, facebookService);
    }

    @Bean("skinProblemService")
    public SkinProblemService skinProblemService(
            SkinProblemRepository skinProblemRepository
    ) {
        return new SkinProblemServiceImpl(skinProblemRepository);
    }

    @Bean("skinProblemAnswerService")
    public SkinProblemAnswerService skinProblemAnswerService(
            SkinProblemAnswerRepository skinProblemAnswerRepository,
            QuizService quizService
    ) {
        return new SkinProblemAnswerServiceImpl(skinProblemAnswerRepository, quizService);
    }

    @Bean("skinTypeService")
    public SkinTypeService skinTypeService(
            SkinTypeRepository skinTypeRepository
    ) {
        return new SkinTypeServiceImpl(skinTypeRepository);
    }

    @Bean("skinTypeAnswerService")
    public SkinTypeAnswerService skinTypeAnswerService(
            SkinTypeAnswerRepository skinTypeAnswerRepository,
            QuizService quizService
    ) {
        return new SkinTypeAnswerServiceImpl(skinTypeAnswerRepository, quizService);
    }

    @Bean("sleepHoursLessThanSevenService")
    public SleepHoursLessThanSevenService sleepHoursLessThanSevenService(
            SleepHoursLessThanSevenRepository sleepHoursLessThanSevenRepository
    ) {
        return new SleepHoursLessThanSevenServiceImpl(sleepHoursLessThanSevenRepository);
    }

    @Bean("sleepHoursLessThanSevenAnswerService")
    public SleepHoursLessThanSevenAnswerService sleepHoursLessThanSevenAnswerService(
            SleepHoursLessThanSevenAnswerRepository sleepHoursLessThanSevenAnswerRepository,
            QuizService quizService
    ) {
        return new SleepHoursLessThanSevenAnswerServiceImpl(sleepHoursLessThanSevenAnswerRepository, quizService);
    }

    @Bean("sleepQualityService")
    public SleepQualityService sleepQualityService(
            SleepQualityRepository sleepQualityRepository
    ) {
        return new SleepQualityServiceImpl(sleepQualityRepository);
    }

    @Bean("sleepQualityAnswerService")
    public SleepQualityAnswerService sleepQualityAnswerService(
            SleepQualityAnswerRepository sleepQualityAnswerRepository,
            QuizService quizService
    ) {
        return new SleepQualityAnswerServiceImpl(sleepQualityAnswerRepository, quizService);
    }

    @Bean("smokeService")
    public SmokeService smokeService(
            SmokeRepository smokeRepository
    ) {
        return new SmokeServiceImpl(smokeRepository);
    }

    @Bean("smokeAnswerService")
    public SmokeAnswerService smokeAnswerService(
            SmokeAnswerRepository smokeAnswerRepository,
            QuizService quizService
    ) {
        return new SmokeAnswerServiceImpl(smokeAnswerRepository, quizService);
    }

    @Bean("sportAmountService")
    public SportAmountService sportAmountService(
            SportAmountRepository sportAmountRepository
    ) {
        return new SportAmountServiceImpl(sportAmountRepository);
    }

    @Bean("sportAmountAnswerService")
    public SportAmountAnswerService sportAmountAnswerService(
            SportAmountAnswerRepository sportAmountAnswerRepository,
            QuizService quizService
    ) {
        return new SportAmountAnswerServiceImpl(sportAmountAnswerRepository, quizService);
    }

    @Bean("sportReasonService")
    public SportReasonService sportReasonService(
            SportReasonRepository sportReasonRepository
    ) {
        return new SportReasonServiceImpl(sportReasonRepository);
    }

    @Bean("sportReasonAnswerService")
    public SportReasonAnswerService sportReasonAnswerService(
            SportReasonAnswerRepository sportReasonAnswerRepository,
            QuizService quizService
    ) {
        return new SportReasonAnswerServiceImpl(sportReasonAnswerRepository, quizService);
    }

    @Bean("stressLevelService")
    public StressLevelService stressLevelService(
            StressLevelRepository stressLevelRepository
    ) {
        return new StressLevelServiceImpl(stressLevelRepository);
    }

    @Bean("stressLevelAnswerService")
    public StressLevelAnswerService stressLevelAnswerService(
            StressLevelAnswerRepository stressLevelAnswerRepository,
            QuizService quizService
    ) {
        return new StressLevelAnswerServiceImpl(stressLevelAnswerRepository, quizService);
    }

    @Bean("stressLevelConditionService")
    public StressLevelConditionService stressLevelConditionService(
            StressLevelConditionRepository stressLevelConditionRepository
    ) {
        return new StressLevelConditionServiceImpl(stressLevelConditionRepository);
    }

    @Bean("stressLevelConditionAnswerService")
    public StressLevelConditionAnswerService stressLevelConditionAnswerService(
            StressLevelConditionAnswerRepository stressLevelConditionAnswerRepository,
            QuizService quizService
    ) {
        return new StressLevelConditionAnswerServiceImpl(stressLevelConditionAnswerRepository, quizService);
    }

    @Bean("stressLevelAtEndOfDayService")
    public StressLevelAtEndOfDayService stressLevelAtEndOfDayService(
            StressLevelAtEndOfDayRepository stressLevelAtEndOfDayRepository
    ) {
        return new StressLevelAtEndOfDayServiceImpl(stressLevelAtEndOfDayRepository);
    }

    @Bean("stressLevelAtEndOfDayAnswerService")
    public StressLevelAtEndOfDayAnswerService stressLevelAtEndOfDayAnswerService(
            StressLevelAtEndOfDayAnswerRepository stressLevelAtEndOfDayAnswerRepository,
            QuizService quizService
    ) {
        return new StressLevelAtEndOfDayAnswerServiceImpl(stressLevelAtEndOfDayAnswerRepository, quizService);
    }

    @Bean("thirtyMinutesOfSunService")
    public ThirtyMinutesOfSunService thirtyMinutesOfSunService(
            ThirtyMinutesOfSunRepository thirtyMinutesOfSunRepository
    ) {
        return new ThirtyMinutesOfSunServiceImpl(thirtyMinutesOfSunRepository);
    }

    @Bean("thirtyMinutesOfSunAnswerService")
    public ThirtyMinutesOfSunAnswerService thirtyMinutesOfSunAnswerService(
            ThirtyMinutesOfSunAnswerRepository thirtyMinutesOfSunAnswerRepository,
            QuizService quizService
    ) {
        return new ThirtyMinutesOfSunAnswerServiceImpl(thirtyMinutesOfSunAnswerRepository, quizService);
    }

    @Bean("tiredWhenWakeUpService")
    public TiredWhenWakeUpService tiredWhenWakeUpService(
            TiredWhenWakeUpRepository tiredWhenWakeUpRepository
    ) {
        return new TiredWhenWakeUpServiceImpl(tiredWhenWakeUpRepository);
    }

    @Bean("tiredWhenWakeUpAnswerService")
    public TiredWhenWakeUpAnswerService tiredWhenWakeUpAnswerService(
            TiredWhenWakeUpAnswerRepository tiredWhenWakeUpAnswerRepository,
            QuizService quizService
    ) {
        return new TiredWhenWakeUpAnswerServiceImpl(tiredWhenWakeUpAnswerRepository, quizService);
    }

    @Bean("trainingIntensivelyService")
    public TrainingIntensivelyService trainingIntensivelyService(
            TrainingIntensivelyRepository trainingIntensivelyRepository
    ) {
        return new TrainingIntensivelyServiceImpl(trainingIntensivelyRepository);
    }

    @Bean("trainingIntensivelyAnswerService")
    public TrainingIntensivelyAnswerService trainingIntensivelyAnswerService(
            TrainingIntensivelyAnswerRepository trainingIntensivelyAnswerRepository,
            QuizService quizService
    ) {
        return new TrainingIntensivelyAnswerServiceImpl(trainingIntensivelyAnswerRepository, quizService);
    }

    @Bean("transitionPeriodComplaintsService")
    public TransitionPeriodComplaintsService transitionPeriodComplaintsService(
            TransitionPeriodComplaintsRepository transitionPeriodComplaintsRepository
    ) {
        return new TransitionPeriodComplaintsServiceImpl(transitionPeriodComplaintsRepository);
    }

    @Bean("transitionPeriodComplaintsAnswerService")
    public TransitionPeriodComplaintsAnswerService transitionPeriodComplaintsAnswerService(
            TransitionPeriodComplaintsAnswerRepository transitionPeriodComplaintsAnswerRepository,
            QuizService quizService
    ) {
        return new TransitionPeriodComplaintsAnswerServiceImpl(transitionPeriodComplaintsAnswerRepository, quizService);
    }

    @Bean("troubleFallingAsleepService")
    public TroubleFallingAsleepService troubleFallingAsleepService(
            TroubleFallingAsleepRepository troubleFallingAsleepRepository
    ) {
        return new TroubleFallingAsleepServiceImpl(troubleFallingAsleepRepository);
    }

    @Bean("troubleFallingAsleepAnswerService")
    public TroubleFallingAsleepAnswerService troubleFallingAsleepAnswerService(
            TroubleFallingAsleepAnswerRepository troubleFallingAsleepAnswerRepository,
            QuizService quizService
    ) {
        return new TroubleFallingAsleepAnswerServiceImpl(troubleFallingAsleepAnswerRepository, quizService);
    }

    @Bean("typeOfTrainingService")
    public TypeOfTrainingService typeOfTrainingService(
            TypeOfTrainingRepository typeOfTrainingRepository
    ) {
        return new TypeOfTrainingServiceImpl(typeOfTrainingRepository);
    }

    @Bean("typeOfTrainingAnswerService")
    public TypeOfTrainingAnswerService typeOfTrainingAnswerService(
            TypeOfTrainingAnswerRepository typeOfTrainingAnswerRepository,
            QuizService quizService
    ) {
        return new TypeOfTrainingAnswerServiceImpl(typeOfTrainingAnswerRepository, quizService);
    }

    @Bean("urinaryInfectionService")
    public UrinaryInfectionService urinaryInfectionService(
            UrinaryInfectionRepository urinaryInfectionRepository
    ) {
        return new UrinaryInfectionServiceImpl(urinaryInfectionRepository);
    }

    @Bean("urinaryInfectionAnswerService")
    public UrinaryInfectionAnswerService urinaryInfectionAnswerService(
            UrinaryInfectionAnswerRepository urinaryInfectionAnswerRepository,
            QuizService quizService
    ) {
        return new UrinaryInfectionAnswerServiceImpl(urinaryInfectionAnswerRepository, quizService);
    }

    @Bean("usageGoalService")
    public UsageGoalService usageGoalService(
            UsageGoalRepository usageGoalRepository
    ) {
        return new UsageGoalServiceImpl(usageGoalRepository);
    }

    @Bean("usageGoalAnswerService")
    public UsageGoalAnswerService usageGoalAnswerService(
            UsageGoalAnswerRepository usageGoalAnswerRepository,
            QuizRepository quizRepository
    ) {
        return new UsageGoalAnswerServiceImpl(usageGoalAnswerRepository, quizRepository);
    }

    @Bean("vitaminIntakeService")
    public VitaminIntakeService vitaminIntakeService(
            VitaminIntakeRepository vitaminIntakeRepository
    ) {
        return new VitaminIntakeServiceImpl(vitaminIntakeRepository);
    }

    @Bean("vitaminIntakeAnswerService")
    public VitaminIntakeAnswerService vitaminIntakeAnswerService(
            VitaminIntakeAnswerRepository vitaminIntakeAnswerRepository,
            QuizService quizService
    ) {
        return new VitaminIntakeAnswerServiceImpl(vitaminIntakeAnswerRepository, quizService);
    }

    @Bean("vitaminOpinionService")
    public VitaminOpinionService vitaminOpinionService(
            VitaminOpinionRepository vitaminOpinionRepository
    ) {
        return new VitaminOpinionServiceImpl(vitaminOpinionRepository);
    }

    @Bean("vitaminOpinionAnswerService")
    public VitaminOpinionAnswerService vitaminOpinionAnswerService(
            VitaminOpinionAnswerRepository vitaminOpinionAnswerRepository,
            QuizService quizService
    ) {
        return new VitaminOpinionAnswerServiceImpl(vitaminOpinionAnswerRepository, quizService);
    }

    @Bean("weeklyTwelveAlcoholicDrinksService")
    public WeeklyTwelveAlcoholicDrinksService weeklyTwelveAlcoholicDrinksService(
            WeeklyTwelveAlcoholicDrinksRepository weeklyTwelveAlcoholicDrinksRepository
    ) {
        return new WeeklyTwelveAlcoholicDrinksServiceImpl(weeklyTwelveAlcoholicDrinksRepository);
    }

    @Bean("weeklyTwelveAlcoholicDrinksAnswerService")
    public WeeklyTwelveAlcoholicDrinksAnswerService weeklyTwelveAlcoholicDrinksAnswerService(
            WeeklyTwelveAlcoholicDrinksAnswerRepository weeklyTwelveAlcoholicDrinksAnswerRepository,
            QuizService quizService
    ) {
        return new WeeklyTwelveAlcoholicDrinksAnswerServiceImpl(weeklyTwelveAlcoholicDrinksAnswerRepository, quizService);
    }
}
