package viteezy.db.quiz;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import viteezy.db.jdbi.quiz.*;


@Configuration("quizDbConfig")
@Import({viteezy.db.jdbi.IoC.class})
public class IoC {

    @Bean("acnePlaceRepository")
    public AcnePlaceRepository acnePlaceRepository(Jdbi jdbi) {
        return new AcnePlaceRepositoryImpl(jdbi);
    }

    @Bean("acnePlaceAnswerRepository")
    public AcnePlaceAnswerRepository acnePlaceAnswerRepository(Jdbi jdbi) {
        return new AcnePlaceAnswerRepositoryImpl(jdbi);
    }

    @Bean("allergyTypeRepository")
    public AllergyTypeRepository allergyTypeRepository(Jdbi jdbi) {
        return new AllergyTypeRepositoryImpl(jdbi);
    }

    @Bean("allergyTypeAnswerRepository")
    public AllergyTypeAnswerRepository allergyTypeAnswerRepository(Jdbi jdbi) {
        return new AllergyTypeAnswerRepositoryImpl(jdbi);
    }

    @Bean("amountOfDairyConsumptionRepository")
    public AmountOfDairyConsumptionRepository amountOfDairyConsumptionRepository(Jdbi jdbi) {
        return new AmountOfDairyConsumptionRepositoryImpl(jdbi);
    }

    @Bean("amountOfDairyConsumptionAnswerRepository")
    public AmountOfDairyConsumptionAnswerRepository amountOfDairyConsumptionAnswerRepository(Jdbi jdbi) {
        return new AmountOfDairyConsumptionAnswerRepositoryImpl(jdbi);
    }

    @Bean("amountOfFiberConsumptionRepository")
    public AmountOfFiberConsumptionRepository amountOfFiberConsumptionRepository(Jdbi jdbi) {
        return new AmountOfFiberConsumptionRepositoryImpl(jdbi);
    }

    @Bean("amountOfFiberConsumptionAnswerRepository")
    public AmountOfFiberConsumptionAnswerRepository amountOfFiberConsumptionAnswerRepository(Jdbi jdbi) {
        return new AmountOfFiberConsumptionAnswerRepositoryImpl(jdbi);
    }

    @Bean("amountOfFishConsumptionRepository")
    public AmountOfFishConsumptionRepository amountOfFishConsumptionRepository(Jdbi jdbi) {
        return new AmountOfFishConsumptionRepositoryImpl(jdbi);
    }

    @Bean("amountOfFishConsumptionAnswerRepository")
    public AmountOfFishConsumptionAnswerRepository amountOfFishConsumptionAnswerRepository(Jdbi jdbi) {
        return new AmountOfFishConsumptionAnswerRepositoryImpl(jdbi);
    }

    @Bean("amountOfFruitConsumptionRepository")
    public AmountOfFruitConsumptionRepository amountOfFruitConsumptionRepository(Jdbi jdbi) {
        return new AmountOfFruitConsumptionRepositoryImpl(jdbi);
    }

    @Bean("amountOfFruitConsumptionAnswerRepository")
    public AmountOfFruitConsumptionAnswerRepository amountOfFruitConsumptionAnswerRepository(Jdbi jdbi) {
        return new AmountOfFruitConsumptionAnswerRepositoryImpl(jdbi);
    }

    @Bean("amountOfMeatConsumptionRepository")
    public AmountOfMeatConsumptionRepository amountOfMeatConsumptionRepository(Jdbi jdbi) {
        return new AmountOfMeatConsumptionRepositoryImpl(jdbi);
    }

    @Bean("amountOfMeatConsumptionAnswerRepository")
    public AmountOfMeatConsumptionAnswerRepository amountOfMeatConsumptionAnswerRepository(Jdbi jdbi) {
        return new AmountOfMeatConsumptionAnswerRepositoryImpl(jdbi);
    }

    @Bean("amountOfProteinConsumptionRepository")
    public AmountOfProteinConsumptionRepository amountOfProteinConsumptionRepository(Jdbi jdbi) {
        return new AmountOfProteinConsumptionRepositoryImpl(jdbi);
    }

    @Bean("amountOfProteinConsumptionAnswerRepository")
    public AmountOfProteinConsumptionAnswerRepository amountOfProteinConsumptionAnswerRepository(Jdbi jdbi) {
        return new AmountOfProteinConsumptionAnswerRepositoryImpl(jdbi);
    }

    @Bean("amountOfVegetableConsumptionRepository")
    public AmountOfVegetableConsumptionRepository amountOfVegetableConsumptionRepository(Jdbi jdbi) {
        return new AmountOfVegetableConsumptionRepositoryImpl(jdbi);
    }

    @Bean("amountOfVegetableConsumptionAnswerRepository")
    public AmountOfVegetableConsumptionAnswerRepository amountOfVegetableConsumptionAnswerRepository(Jdbi jdbi) {
        return new AmountOfVegetableConsumptionAnswerRepositoryImpl(jdbi);
    }

    @Bean("attentionFocusRepository")
    public AttentionFocusRepository attentionFocusRepository(Jdbi jdbi) {
        return new AttentionFocusRepositoryImpl(jdbi);
    }

    @Bean("attentionFocusAnswerRepository")
    public AttentionFocusAnswerRepository attentionFocusAnswerRepository(Jdbi jdbi) {
        return new AttentionFocusAnswerRepositoryImpl(jdbi);
    }

    @Bean("attentionStateRepository")
    public AttentionStateRepository attentionStateRepository(Jdbi jdbi) {
        return new AttentionStateRepositoryImpl(jdbi);
    }

    @Bean("attentionStateAnswerRepository")
    public AttentionStateAnswerRepository attentionStateAnswerRepository(Jdbi jdbi) {
        return new AttentionStateAnswerRepositoryImpl(jdbi);
    }

    @Bean("averageSleepingHoursRepository")
    public AverageSleepingHoursRepository averageSleepingHoursRepository(Jdbi jdbi) {
        return new AverageSleepingHoursRepositoryImpl(jdbi);
    }

    @Bean("averageSleepingHoursAnswerRepository")
    public AverageSleepingHoursAnswerRepository averageSleepingHoursAnswerRepository(Jdbi jdbi) {
        return new AverageSleepingHoursAnswerRepositoryImpl(jdbi);
    }

    @Bean("bingeEatingRepository")
    public BingeEatingRepository bingeEatingRepository(Jdbi jdbi) {
        return new BingeEatingRepositoryImpl(jdbi);
    }

    @Bean("bingeEatingAnswerRepository")
    public BingeEatingAnswerRepository bingeEatingAnswerRepository(Jdbi jdbi) {
        return new BingeEatingAnswerRepositoryImpl(jdbi);
    }

    @Bean("bingeEatingReasonRepository")
    public BingeEatingReasonRepository bingeEatingReasonRepository(Jdbi jdbi) {
        return new BingeEatingReasonRepositoryImpl(jdbi);
    }

    @Bean("bingeEatingReasonAnswerRepository")
    public BingeEatingReasonAnswerRepository bingeEatingReasonAnswerRepository(Jdbi jdbi) {
        return new BingeEatingReasonAnswerRepositoryImpl(jdbi);
    }

    @Bean("birthHealthRepository")
    public BirthHealthRepository birthHealthRepository(Jdbi jdbi) {
        return new BirthHealthRepositoryImpl(jdbi);
    }

    @Bean("birthHealthAnswerRepository")
    public BirthHealthAnswerRepository birthHealthAnswerRepository(Jdbi jdbi) {
        return new BirthHealthAnswerRepositoryImpl(jdbi);
    }

    @Bean("childrenWishRepository")
    public ChildrenWishRepository childrenWishRepository(Jdbi jdbi) {
        return new ChildrenWishRepositoryImpl(jdbi);
    }

    @Bean("childrenWishAnswerRepository")
    public ChildrenWishAnswerRepository childrenWishAnswerRepository(Jdbi jdbi) {
        return new ChildrenWishAnswerRepositoryImpl(jdbi);
    }

    @Bean("currentLibidoRepository")
    public CurrentLibidoRepository currentLibidoRepository(Jdbi jdbi) {
        return new CurrentLibidoRepositoryImpl(jdbi);
    }

    @Bean("currentLibidoAnswerRepository")
    public CurrentLibidoAnswerRepository currentLibidoAnswerRepository(Jdbi jdbi) {
        return new CurrentLibidoAnswerRepositoryImpl(jdbi);
    }

    @Bean("dateOfBirthAnswerRepository")
    public DateOfBirthAnswerRepository dateOfBirthAnswerRepository(Jdbi jdbi) {
        return new DateOfBirthAnswerRepositoryImpl(jdbi);
    }

    @Bean("dailyFourCoffeeRepository")
    public DailyFourCoffeeRepository dailyFourCoffeeRepository(Jdbi jdbi) {
        return new DailyFourCoffeeRepositoryImpl(jdbi);
    }

    @Bean("dailyFourCoffeeAnswerRepository")
    public DailyFourCoffeeAnswerRepository dailyFourCoffeeAnswerRepository(Jdbi jdbi) {
        return new DailyFourCoffeeAnswerRepositoryImpl(jdbi);
    }

    @Bean("dailySixAlcoholicDrinksRepository")
    public DailySixAlcoholicDrinksRepository dailySixAlcoholicDrinksRepository(Jdbi jdbi) {
        return new DailySixAlcoholicDrinksRepositoryImpl(jdbi);
    }

    @Bean("dailySixAlcoholicDrinksAnswerRepository")
    public DailySixAlcoholicDrinksAnswerRepository dailySixAlcoholicDrinksAnswerRepository(Jdbi jdbi) {
        return new DailySixAlcoholicDrinksAnswerRepositoryImpl(jdbi);
    }

    @Bean("dietIntoleranceRepository")
    public DietIntoleranceRepository dietIntoleranceRepository(Jdbi jdbi) {
        return new DietIntoleranceRepositoryImpl(jdbi);
    }

    @Bean("dietIntoleranceAnswerRepository")
    public DietIntoleranceAnswerRepository dietIntoleranceAnswerRepository(Jdbi jdbi) {
        return new DietIntoleranceAnswerRepositoryImpl(jdbi);
    }

    @Bean("dietTypeRepository")
    public DietTypeRepository dietTypeRepository(Jdbi jdbi) {
        return new DietTypeRepositoryImpl(jdbi);
    }

    @Bean("dietTypeAnswerRepository")
    public DietTypeAnswerRepository dietTypeAnswerRepository(Jdbi jdbi) {
        return new DietTypeAnswerRepositoryImpl(jdbi);
    }

    @Bean("digestionAmountRepository")
    public DigestionAmountRepository digestionAmountRepository(Jdbi jdbi) {
        return new DigestionAmountRepositoryImpl(jdbi);
    }

    @Bean("digestionAmountAnswerRepository")
    public DigestionAmountAnswerRepository digestionAmountAnswerRepository(Jdbi jdbi) {
        return new DigestionAmountAnswerRepositoryImpl(jdbi);
    }

    @Bean("digestionOccurrenceRepository")
    public DigestionOccurrenceRepository digestionOccurrenceRepository(Jdbi jdbi) {
        return new DigestionOccurrenceRepositoryImpl(jdbi);
    }

    @Bean("digestionOccurrenceAnswerRepository")
    public DigestionOccurrenceAnswerRepository digestionOccurrenceAnswerRepository(Jdbi jdbi) {
        return new DigestionOccurrenceAnswerRepositoryImpl(jdbi);
    }

    @Bean("drySkinRepository")
    public DrySkinRepository drySkinRepository(Jdbi jdbi) {
        return new DrySkinRepositoryImpl(jdbi);
    }

    @Bean("drySkinAnswerRepository")
    public DrySkinAnswerRepository drySkinAnswerRepository(Jdbi jdbi) {
        return new DrySkinAnswerRepositoryImpl(jdbi);
    }

    @Bean("easternMedicineOpinionRepository")
    public EasternMedicineOpinionRepository easternMedicineOpinionRepository(Jdbi jdbi) {
        return new EasternMedicineOpinionRepositoryImpl(jdbi);
    }

    @Bean("easternMedicineOpinionAnswerRepository")
    public EasternMedicineOpinionAnswerRepository easternMedicineOpinionAnswerRepository(Jdbi jdbi) {
        return new EasternMedicineOpinionAnswerRepositoryImpl(jdbi);
    }

    @Bean("energyStateRepository")
    public EnergyStateRepository energyStateRepository(Jdbi jdbi) {
        return new EnergyStateRepositoryImpl(jdbi);
    }

    @Bean("energyStateAnswerRepository")
    public EnergyStateAnswerRepository energyStateAnswerRepository(Jdbi jdbi) {
        return new EnergyStateAnswerRepositoryImpl(jdbi);
    }

    @Bean("genderRepository")
    public GenderRepository genderRepository(Jdbi jdbi) {
        return new GenderRepositoryImpl(jdbi);
    }

    @Bean("genderAnswerRepository")
    public GenderAnswerRepository genderAnswerRepository(Jdbi jdbi) {
        return new GenderAnswerRepositoryImpl(jdbi);
    }

    @Bean("hairTypeRepository")
    public HairTypeRepository hairTypeRepository(Jdbi jdbi) {
        return new HairTypeRepositoryImpl(jdbi);
    }

    @Bean("hairTypeAnswerRepository")
    public HairTypeAnswerRepository hairTypeAnswerRepository(Jdbi jdbi) {
        return new HairTypeAnswerRepositoryImpl(jdbi);
    }

    @Bean("healthComplaintsRepository")
    public HealthComplaintsRepository healthComplaintsRepository(Jdbi jdbi) {
        return new HealthComplaintsRepositoryImpl(jdbi);
    }

    @Bean("healthComplaintsAnswerRepository")
    public HealthComplaintsAnswerRepository healthComplaintsAnswerRepository(Jdbi jdbi) {
        return new HealthComplaintsAnswerRepositoryImpl(jdbi);
    }

    @Bean("healthyLifestyleRepository")
    public HealthyLifestyleRepository healthyLifestyleRepository(Jdbi jdbi) {
        return new HealthyLifestyleRepositoryImpl(jdbi);
    }

    @Bean("healthyLifestyleAnswerRepository")
    public HealthyLifestyleAnswerRepository healthyLifestyleAnswerRepository(Jdbi jdbi) {
        return new HealthyLifestyleAnswerRepositoryImpl(jdbi);
    }

    @Bean("helpGoalRepository")
    public HelpGoalRepository helpGoalRepository(Jdbi jdbi) {
        return new HelpGoalRepositoryImpl(jdbi);
    }

    @Bean("helpGoalAnswerRepository")
    public HelpGoalAnswerRepository helpGoalAnswerRepository(Jdbi jdbi) {
        return new HelpGoalAnswerRepositoryImpl(jdbi);
    }

    @Bean("ironPrescribedRepository")
    public IronPrescribedRepository ironPrescribedRepository(Jdbi jdbi) {
        return new IronPrescribedRepositoryImpl(jdbi);
    }

    @Bean("ironPrescribedAnswerRepository")
    public IronPrescribedAnswerRepository ironPrescribedAnswerRepository(Jdbi jdbi) {
        return new IronPrescribedAnswerRepositoryImpl(jdbi);
    }

    @Bean("lackOfConcentrationRepository")
    public LackOfConcentrationRepository lackOfConcentrationRepository(Jdbi jdbi) {
        return new LackOfConcentrationRepositoryImpl(jdbi);
    }

    @Bean("lackOfConcentrationAnswerRepository")
    public LackOfConcentrationAnswerRepository lackOfConcentrationAnswerRepository(Jdbi jdbi) {
        return new LackOfConcentrationAnswerRepositoryImpl(jdbi);
    }

    @Bean("libidoStressLevelRepository")
    public LibidoStressLevelRepository libidoStressLevelRepository(Jdbi jdbi) {
        return new LibidoStressLevelRepositoryImpl(jdbi);
    }

    @Bean("libidoStressLevelAnswerRepository")
    public LibidoStressLevelAnswerRepository libidoStressLevelAnswerRepository(Jdbi jdbi) {
        return new LibidoStressLevelAnswerRepositoryImpl(jdbi);
    }

    @Bean("loseWeightChallengeRepository")
    public LoseWeightChallengeRepository loseWeightChallengeRepository(Jdbi jdbi) {
        return new LoseWeightChallengeRepositoryImpl(jdbi);
    }

    @Bean("loseWeightChallengeAnswerRepository")
    public LoseWeightChallengeAnswerRepository loseWeightChallengeAnswerRepository(Jdbi jdbi) {
        return new LoseWeightChallengeAnswerRepositoryImpl(jdbi);
    }

    @Bean("menstruationIntervalRepository")
    public MenstruationIntervalRepository menstruationIntervalRepository(Jdbi jdbi) {
        return new MenstruationIntervalRepositoryImpl(jdbi);
    }

    @Bean("menstruationIntervalAnswerRepository")
    public MenstruationIntervalAnswerRepository menstruationIntervalAnswerRepository(Jdbi jdbi) {
        return new MenstruationIntervalAnswerRepositoryImpl(jdbi);
    }

    @Bean("menstruationMoodRepository")
    public MenstruationMoodRepository menstruationMoodRepository(Jdbi jdbi) {
        return new MenstruationMoodRepositoryImpl(jdbi);
    }

    @Bean("menstruationMoodAnswerRepository")
    public MenstruationMoodAnswerRepository menstruationMoodAnswerRepository(Jdbi jdbi) {
        return new MenstruationMoodAnswerRepositoryImpl(jdbi);
    }

    @Bean("menstruationSideIssueRepository")
    public MenstruationSideIssueRepository menstruationSideIssueRepository(Jdbi jdbi) {
        return new MenstruationSideIssueRepositoryImpl(jdbi);
    }

    @Bean("menstruationSideIssueAnswerRepository")
    public MenstruationSideIssueAnswerRepository menstruationSideIssueAnswerRepository(Jdbi jdbi) {
        return new MenstruationSideIssueAnswerRepositoryImpl(jdbi);
    }

    @Bean("mentalFitnessRepository")
    public MentalFitnessRepository mentalFitnessRepository(Jdbi jdbi) {
        return new MentalFitnessRepositoryImpl(jdbi);
    }

    @Bean("mentalFitnessAnswerRepository")
    public MentalFitnessAnswerRepository mentalFitnessAnswerRepository(Jdbi jdbi) {
        return new MentalFitnessAnswerRepositoryImpl(jdbi);
    }

    @Bean("nailImprovementRepository")
    public NailImprovementRepository nailImprovementRepository(Jdbi jdbi) {
        return new NailImprovementRepositoryImpl(jdbi);
    }

    @Bean("nailImprovementAnswerRepository")
    public NailImprovementAnswerRepository nailImprovementAnswerRepository(Jdbi jdbi) {
        return new NailImprovementAnswerRepositoryImpl(jdbi);
    }

    @Bean("nameAnswerRepository")
    public NameAnswerRepository nameAnswerRepository(Jdbi jdbi) {
        return new NameAnswerRepositoryImpl(jdbi);
    }

    @Bean("newProductAvailableRepository")
    public NewProductAvailableRepository newProductAvailableRepository(Jdbi jdbi) {
        return new NewProductAvailableRepositoryImpl(jdbi);
    }

    @Bean("newProductAvailableAnswerRepository")
    public NewProductAvailableAnswerRepository newProductAvailableAnswerRepository(Jdbi jdbi) {
        return new NewProductAvailableAnswerRepositoryImpl(jdbi);
    }

    @Bean("oftenHavingFluRepository")
    public OftenHavingFluRepository oftenHavingFluRepository(Jdbi jdbi) {
        return new OftenHavingFluRepositoryImpl(jdbi);
    }

    @Bean("oftenHavingFluAnswerRepository")
    public OftenHavingFluAnswerRepository oftenHavingFluAnswerRepository(Jdbi jdbi) {
        return new OftenHavingFluAnswerRepositoryImpl(jdbi);
    }

    @Bean("pregnancyStateRepository")
    public PregnancyStateRepository pregnancyStateRepository(Jdbi jdbi) {
        return new PregnancyStateRepositoryImpl(jdbi);
    }

    @Bean("pregnancyStateAnswerRepository")
    public PregnancyStateAnswerRepository pregnancyStateAnswerRepository(Jdbi jdbi) {
        return new PregnancyStateAnswerRepositoryImpl(jdbi);
    }

    @Bean("presentAtCrowdedPlacesRepository")
    public PresentAtCrowdedPlacesRepository presentAtCrowdedPlacesRepository(Jdbi jdbi) {
        return new PresentAtCrowdedPlacesRepositoryImpl(jdbi);
    }

    @Bean("presentAtCrowdedPlacesAnswerRepository")
    public PresentAtCrowdedPlacesAnswerRepository presentAtCrowdedPlacesAnswerRepository(Jdbi jdbi) {
        return new PresentAtCrowdedPlacesAnswerRepositoryImpl(jdbi);
    }

    @Bean("primaryGoalRepository")
    public PrimaryGoalRepository primaryGoalRepository(Jdbi jdbi) {
        return new PrimaryGoalRepositoryImpl(jdbi);
    }

    @Bean("primaryGoalAnswerRepository")
    public PrimaryGoalAnswerRepository primaryGoalAnswerRepository(Jdbi jdbi) {
        return new PrimaryGoalAnswerRepositoryImpl(jdbi);
    }

    @Bean("quizRepository")
    public QuizRepository quizRepository(Jdbi jdbi) {
        return new QuizRepositoryImpl(jdbi);
    }

    @Bean("skinProblemRepository")
    public SkinProblemRepository skinProblemRepository(Jdbi jdbi) {
        return new SkinProblemRepositoryImpl(jdbi);
    }

    @Bean("skinProblemAnswerRepository")
    public SkinProblemAnswerRepository skinProblemAnswerRepository(Jdbi jdbi) {
        return new SkinProblemAnswerRepositoryImpl(jdbi);
    }

    @Bean("skinTypeRepository")
    public SkinTypeRepository skinTypeRepository(Jdbi jdbi) {
        return new SkinTypeRepositoryImpl(jdbi);
    }

    @Bean("skinTypeAnswerRepository")
    public SkinTypeAnswerRepository skinTypeAnswerRepository(Jdbi jdbi) {
        return new SkinTypeAnswerRepositoryImpl(jdbi);
    }

    @Bean("sleepHoursLessThanSevenRepository")
    public SleepHoursLessThanSevenRepository sleepHoursLessThanSevenRepository(Jdbi jdbi) {
        return new SleepHoursLessThanSevenRepositoryImpl(jdbi);
    }

    @Bean("sleepHoursLessThanSevenAnswerRepository")
    public SleepHoursLessThanSevenAnswerRepository sleepHoursLessThanSevenAnswerRepository(Jdbi jdbi) {
        return new SleepHoursLessThanSevenAnswerRepositoryImpl(jdbi);
    }

    @Bean("sleepQualityRepository")
    public SleepQualityRepository sleepQualityRepository(Jdbi jdbi) {
        return new SleepQualityRepositoryImpl(jdbi);
    }

    @Bean("sleepQualityAnswerRepository")
    public SleepQualityAnswerRepository sleepQualityAnswerRepository(Jdbi jdbi) {
        return new SleepQualityAnswerRepositoryImpl(jdbi);
    }

    @Bean("smokeRepository")
    public SmokeRepository smokeRepository(Jdbi jdbi) {
        return new SmokeRepositoryImpl(jdbi);
    }

    @Bean("smokeAnswerRepository")
    public SmokeAnswerRepository smokeAnswerRepository(Jdbi jdbi) {
        return new SmokeAnswerRepositoryImpl(jdbi);
    }

    @Bean("sportAmountRepository")
    public SportAmountRepository sportAmountRepository(Jdbi jdbi) {
        return new SportAmountRepositoryImpl(jdbi);
    }

    @Bean("sportAmountAnswerRepository")
    public SportAmountAnswerRepository sportAmountAnswerRepository(Jdbi jdbi) {
        return new SportAmountAnswerRepositoryImpl(jdbi);
    }

    @Bean("sportReasonRepository")
    public SportReasonRepository sportReasonRepository(Jdbi jdbi) {
        return new SportReasonRepositoryImpl(jdbi);
    }

    @Bean("sportReasonAnswerRepository")
    public SportReasonAnswerRepository sportReasonAnswerRepository(Jdbi jdbi) {
        return new SportReasonAnswerRepositoryImpl(jdbi);
    }

    @Bean("stressLevelRepository")
    public StressLevelRepository stressLevelRepository(Jdbi jdbi) {
        return new StressLevelRepositoryImpl(jdbi);
    }

    @Bean("stressLevelAnswerRepository")
    public StressLevelAnswerRepository stressLevelAnswerRepository(Jdbi jdbi) {
        return new StressLevelAnswerRepositoryImpl(jdbi);
    }

    @Bean("stressLevelConditionRepository")
    public StressLevelConditionRepository stressLevelConditionRepository(Jdbi jdbi) {
        return new StressLevelConditionRepositoryImpl(jdbi);
    }

    @Bean("stressLevelConditionAnswerRepository")
    public StressLevelConditionAnswerRepository stressLevelConditionAnswerRepository(Jdbi jdbi) {
        return new StressLevelConditionAnswerRepositoryImpl(jdbi);
    }

    @Bean("stressLevelAtEndOfDayRepository")
    public StressLevelAtEndOfDayRepository stressLevelAtEndOfDayRepository(Jdbi jdbi) {
        return new StressLevelAtEndOfDayRepositoryImpl(jdbi);
    }

    @Bean("stressLevelAtEndOfDayAnswerRepository")
    public StressLevelAtEndOfDayAnswerRepository stressLevelAtEndOfDayAnswerRepository(Jdbi jdbi) {
        return new StressLevelAtEndOfDayAnswerRepositoryImpl(jdbi);
    }

    @Bean("thirtyMinutesOfSunRepository")
    public ThirtyMinutesOfSunRepository thirtyMinutesOfSunRepository(Jdbi jdbi) {
        return new ThirtyMinutesOfSunRepositoryImpl(jdbi);
    }

    @Bean("thirtyMinutesOfSunAnswerRepository")
    public ThirtyMinutesOfSunAnswerRepository thirtyMinutesOfSunAnswerRepository(Jdbi jdbi) {
        return new ThirtyMinutesOfSunAnswerRepositoryImpl(jdbi);
    }

    @Bean("tiredWhenWakeUpRepository")
    public TiredWhenWakeUpRepository tiredWhenWakeUpRepository(Jdbi jdbi) {
        return new TiredWhenWakeUpRepositoryImpl(jdbi);
    }

    @Bean("tiredWhenWakeUpAnswerRepository")
    public TiredWhenWakeUpAnswerRepository tiredWhenWakeUpAnswerRepository(Jdbi jdbi) {
        return new TiredWhenWakeUpAnswerRepositoryImpl(jdbi);
    }

    @Bean("trainingIntensivelyRepository")
    public TrainingIntensivelyRepository trainingIntensivelyRepository(Jdbi jdbi) {
        return new TrainingIntensivelyRepositoryImpl(jdbi);
    }

    @Bean("trainingIntensivelyAnswerRepository")
    public TrainingIntensivelyAnswerRepository trainingIntensivelyAnswerRepository(Jdbi jdbi) {
        return new TrainingIntensivelyAnswerRepositoryImpl(jdbi);
    }

    @Bean("transitionPeriodComplaintsRepository")
    public TransitionPeriodComplaintsRepository transitionPeriodComplaintsRepository(Jdbi jdbi) {
        return new TransitionPeriodComplaintsRepositoryImpl(jdbi);
    }

    @Bean("transitionPeriodComplaintsAnswerRepository")
    public TransitionPeriodComplaintsAnswerRepository transitionPeriodComplaintsAnswerRepository(Jdbi jdbi) {
        return new TransitionPeriodComplaintsAnswerRepositoryImpl(jdbi);
    }

    @Bean("troubleFallingAsleepRepository")
    public TroubleFallingAsleepRepository troubleFallingAsleepRepository(Jdbi jdbi) {
        return new TroubleFallingAsleepRepositoryImpl(jdbi);
    }

    @Bean("troubleFallingAsleepAnswerRepository")
    public TroubleFallingAsleepAnswerRepository troubleFallingAsleepAnswerRepository(Jdbi jdbi) {
        return new TroubleFallingAsleepAnswerRepositoryImpl(jdbi);
    }

    @Bean("typeOfTrainingRepository")
    public TypeOfTrainingRepository typeOfTrainingRepository(Jdbi jdbi) {
        return new TypeOfTrainingRepositoryImpl(jdbi);
    }

    @Bean("typeOfTrainingAnswerRepository")
    public TypeOfTrainingAnswerRepository typeOfTrainingAnswerRepository(Jdbi jdbi) {
        return new TypeOfTrainingAnswerRepositoryImpl(jdbi);
    }

    @Bean("urinaryInfectionRepository")
    public UrinaryInfectionRepository urinaryInfectionRepository(Jdbi jdbi) {
        return new UrinaryInfectionRepositoryImpl(jdbi);
    }

    @Bean("urinaryInfectionAnswerRepository")
    public UrinaryInfectionAnswerRepository urinaryInfectionAnswerRepository(Jdbi jdbi) {
        return new UrinaryInfectionAnswerRepositoryImpl(jdbi);
    }

    @Bean("usageGoalRepository")
    public UsageGoalRepository usageGoalRepository(Jdbi jdbi) {
        return new UsageGoalRepositoryImpl(jdbi);
    }

    @Bean("usageGoalAnswerRepository")
    public UsageGoalAnswerRepository usageGoalAnswerRepository(Jdbi jdbi) {
        return new UsageGoalAnswerRepositoryImpl(jdbi);
    }

    @Bean("vitaminIntakeRepository")
    public VitaminIntakeRepository vitaminIntakeRepository(Jdbi jdbi) {
        return new VitaminIntakeRepositoryImpl(jdbi);
    }

    @Bean("vitaminIntakeAnswerRepository")
    public VitaminIntakeAnswerRepository vitaminIntakeAnswerRepository(Jdbi jdbi) {
        return new VitaminIntakeAnswerRepositoryImpl(jdbi);
    }

    @Bean("vitaminOpinionRepository")
    public VitaminOpinionRepository vitaminOpinionRepository(Jdbi jdbi) {
        return new VitaminOpinionRepositoryImpl(jdbi);
    }

    @Bean("vitaminOpinionAnswerRepository")
    public VitaminOpinionAnswerRepository vitaminOpinionAnswerRepository(Jdbi jdbi) {
        return new VitaminOpinionAnswerRepositoryImpl(jdbi);
    }

    @Bean("weeklyTwelveAlcoholicDrinksRepository")
    public WeeklyTwelveAlcoholicDrinksRepository weeklyTwelveAlcoholicDrinksRepository(Jdbi jdbi) {
        return new WeeklyTwelveAlcoholicDrinksRepositoryImpl(jdbi);
    }

    @Bean("weeklyTwelveAlcoholicDrinksAnswerRepository")
    public WeeklyTwelveAlcoholicDrinksAnswerRepository weeklyTwelveAlcoholicDrinksAnswerRepository(Jdbi jdbi) {
        return new WeeklyTwelveAlcoholicDrinksAnswerRepositoryImpl(jdbi);
    }
}