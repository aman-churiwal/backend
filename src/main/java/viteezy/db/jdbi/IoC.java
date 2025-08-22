package viteezy.db.jdbi;

import com.codahale.metrics.jdbi3.InstrumentedSqlLogger;
import io.dropwizard.core.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.db.jdbi.argument.BlendIngredientReasonCodeArgument;
import viteezy.db.jdbi.argument.UUIDArgument;
import viteezy.db.jdbi.health.DatabaseHealthCheck;
import viteezy.db.jdbi.mapper.*;
import viteezy.db.jdbi.mapper.dashboard.AuthTokenMapper;
import viteezy.db.jdbi.mapper.dashboard.NoteMapper;
import viteezy.db.jdbi.mapper.dashboard.UserMapper;
import viteezy.db.jdbi.mapper.ingredient.*;
import viteezy.db.jdbi.mapper.quiz.*;

import javax.sql.DataSource;


@Configuration("jdbiDbConfig")
@EnableTransactionManagement
public class IoC {

    private final ViteezyConfiguration viteezyConfiguration;
    private final Environment environment;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(
            ViteezyConfiguration viteezyConfiguration, @Qualifier("dropwizardEnvironment") Environment environment
    ) {
        this.viteezyConfiguration = viteezyConfiguration;
        this.environment = environment;
    }

    @Bean("dataSource")
    public DataSource dataSource() {
        return viteezyConfiguration.getDataSourceFactory()
                .build(environment.metrics(), "dataSource");
    }

    @Bean
    public DatabaseHealthCheck databaseHealthCheck(Jdbi dbi) {
        return new DatabaseHealthCheck(dbi);
    }

    @Bean("dbi")
    public Jdbi jdbi(TransactionAwareDataSourceProxy transactionAwareDataSourceProxy) {
        Jdbi jdbi = Jdbi.create(transactionAwareDataSourceProxy);
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.registerRowMapper(new AcnePlaceAnswerMapper());
        jdbi.registerRowMapper(new AcnePlaceMapper());
        jdbi.registerRowMapper(new AllergyTypeAnswerMapper());
        jdbi.registerRowMapper(new AllergyTypeMapper());
        jdbi.registerRowMapper(new AmountOfDairyConsumptionAnswerMapper());
        jdbi.registerRowMapper(new AmountOfDairyConsumptionMapper());
        jdbi.registerRowMapper(new AmountOfFiberConsumptionAnswerMapper());
        jdbi.registerRowMapper(new AmountOfFiberConsumptionMapper());
        jdbi.registerRowMapper(new AmountOfFishConsumptionAnswerMapper());
        jdbi.registerRowMapper(new AmountOfFishConsumptionMapper());
        jdbi.registerRowMapper(new AmountOfFruitConsumptionAnswerMapper());
        jdbi.registerRowMapper(new AmountOfFruitConsumptionMapper());
        jdbi.registerRowMapper(new AmountOfMeatConsumptionAnswerMapper());
        jdbi.registerRowMapper(new AmountOfMeatConsumptionMapper());
        jdbi.registerRowMapper(new AmountOfProteinConsumptionAnswerMapper());
        jdbi.registerRowMapper(new AmountOfProteinConsumptionMapper());
        jdbi.registerRowMapper(new AmountOfVegetableConsumptionAnswerMapper());
        jdbi.registerRowMapper(new AmountOfVegetableConsumptionMapper());
        jdbi.registerRowMapper(new AttentionFocusAnswerMapper());
        jdbi.registerRowMapper(new AttentionFocusMapper());
        jdbi.registerRowMapper(new AttentionStateAnswerMapper());
        jdbi.registerRowMapper(new AttentionStateMapper());
        jdbi.registerRowMapper(new AuthTokenMapper());
        jdbi.registerRowMapper(new AverageSleepingHoursAnswerMapper());
        jdbi.registerRowMapper(new AverageSleepingHoursMapper());
        jdbi.registerRowMapper(new BingeEatingAnswerMapper());
        jdbi.registerRowMapper(new BingeEatingMapper());
        jdbi.registerRowMapper(new BingeEatingReasonAnswerMapper());
        jdbi.registerRowMapper(new BingeEatingReasonMapper());
        jdbi.registerRowMapper(new BirthHealthAnswerMapper());
        jdbi.registerRowMapper(new BirthHealthMapper());
        jdbi.registerRowMapper(new BlendMapper());
        jdbi.registerRowMapper(new BlendIngredientMapper());
        jdbi.registerRowMapper(new BlendIngredientReasonMapper());
        jdbi.registerRowMapper(new ChildrenWishAnswerMapper());
        jdbi.registerRowMapper(new ChildrenWishMapper());
        jdbi.registerRowMapper(new CouponDiscountMapper());
        jdbi.registerRowMapper(new CouponMapper());
        jdbi.registerRowMapper(new CouponUsedMapper());
        jdbi.registerRowMapper(new CurrentLibidoAnswerMapper());
        jdbi.registerRowMapper(new CurrentLibidoMapper());
        jdbi.registerRowMapper(new CustomerMapper());
        jdbi.registerRowMapper(new DailyFourCoffeeAnswerMapper());
        jdbi.registerRowMapper(new DailyFourCoffeeMapper());
        jdbi.registerRowMapper(new DailySixAlcoholicDrinksAnswerMapper());
        jdbi.registerRowMapper(new DailySixAlcoholicDrinksMapper());
        jdbi.registerRowMapper(new DateOfBirthAnswerMapper());
        jdbi.registerRowMapper(new DietIntoleranceAnswerMapper());
        jdbi.registerRowMapper(new DietIntoleranceMapper());
        jdbi.registerRowMapper(new DietTypeAnswerMapper());
        jdbi.registerRowMapper(new DietTypeMapper());
        jdbi.registerRowMapper(new DigestionAmountAnswerMapper());
        jdbi.registerRowMapper(new DigestionAmountMapper());
        jdbi.registerRowMapper(new DigestionOccurrenceAnswerMapper());
        jdbi.registerRowMapper(new DigestionOccurrenceMapper());
        jdbi.registerRowMapper(new DrySkinAnswerMapper());
        jdbi.registerRowMapper(new DrySkinMapper());
        jdbi.registerRowMapper(new EasternMedicineOpinionAnswerMapper());
        jdbi.registerRowMapper(new EasternMedicineOpinionMapper());
        jdbi.registerRowMapper(new EmailAnswerMapper());
        jdbi.registerRowMapper(new EnergyStateAnswerMapper());
        jdbi.registerRowMapper(new EnergyStateMapper());
        jdbi.registerRowMapper(new GenderAnswerMapper());
        jdbi.registerRowMapper(new GenderMapper());
        jdbi.registerRowMapper(new HairTypeAnswerMapper());
        jdbi.registerRowMapper(new HairTypeMapper());
        jdbi.registerRowMapper(new HealthComplaintsAnswerMapper());
        jdbi.registerRowMapper(new HealthComplaintsMapper());
        jdbi.registerRowMapper(new HealthyLifestyleAnswerMapper());
        jdbi.registerRowMapper(new HealthyLifestyleMapper());
        jdbi.registerRowMapper(new HelpGoalAnswerMapper());
        jdbi.registerRowMapper(new HelpGoalMapper());
        jdbi.registerRowMapper(new IncentiveMapper());
        jdbi.registerRowMapper(new IngredientComponentMapper());
        jdbi.registerRowMapper(new IngredientMapper());
        jdbi.registerRowMapper(new IngredientArticleMapper());
        jdbi.registerRowMapper(new IngredientContentMapper());
        jdbi.registerRowMapper(new IngredientPriceMapper());
        jdbi.registerRowMapper(new IngredientUnitMapper());
        jdbi.registerRowMapper(new IronPrescribedAnswerMapper());
        jdbi.registerRowMapper(new IronPrescribedMapper());
        jdbi.registerRowMapper(new LackOfConcentrationAnswerMapper());
        jdbi.registerRowMapper(new LackOfConcentrationMapper());
        jdbi.registerRowMapper(new LibidoStressLevelAnswerMapper());
        jdbi.registerRowMapper(new LibidoStressLevelMapper());
        jdbi.registerRowMapper(new LoggingMapper());
        jdbi.registerRowMapper(new LoginMapper());
        jdbi.registerRowMapper(new LoseWeightChallengeAnswerMapper());
        jdbi.registerRowMapper(new LoseWeightChallengeMapper());
        jdbi.registerRowMapper(new MenstruationIntervalAnswerMapper());
        jdbi.registerRowMapper(new MenstruationIntervalMapper());
        jdbi.registerRowMapper(new MenstruationMoodAnswerMapper());
        jdbi.registerRowMapper(new MenstruationMoodMapper());
        jdbi.registerRowMapper(new MenstruationSideIssueAnswerMapper());
        jdbi.registerRowMapper(new MenstruationSideIssueMapper());
        jdbi.registerRowMapper(new MentalFitnessAnswerMapper());
        jdbi.registerRowMapper(new MentalFitnessMapper());
        jdbi.registerRowMapper(new NailImprovementAnswerMapper());
        jdbi.registerRowMapper(new NailImprovementMapper());
        jdbi.registerRowMapper(new NameAnswerMapper());
        jdbi.registerRowMapper(new NewProductAvailableAnswerMapper());
        jdbi.registerRowMapper(new NewProductAvailableMapper());
        jdbi.registerRowMapper(new NoteMapper());
        jdbi.registerRowMapper(new OftenHavingFluAnswerMapper());
        jdbi.registerRowMapper(new OftenHavingFluMapper());
        jdbi.registerRowMapper(new PaymentMapper());
        jdbi.registerRowMapper(new PaymentPlanMapper());
        jdbi.registerRowMapper(new PharmacistOrderMapper());
        jdbi.registerRowMapper(new PregnancyStateAnswerMapper());
        jdbi.registerRowMapper(new PregnancyStateMapper());
        jdbi.registerRowMapper(new PresentAtCrowdedPlacesAnswerMapper());
        jdbi.registerRowMapper(new PresentAtCrowdedPlacesMapper());
        jdbi.registerRowMapper(new PrimaryGoalAnswerMapper());
        jdbi.registerRowMapper(new PrimaryGoalMapper());
        jdbi.registerRowMapper(new ProductIngredientMapper());
        jdbi.registerRowMapper(new ProductMapper());
        jdbi.registerRowMapper(new viteezy.db.jdbi.mapper.klaviyo.ProductMapper());
        jdbi.registerRowMapper(new QuizMapper());
        jdbi.registerRowMapper(new ReferralMapper());
        jdbi.registerRowMapper(new ReviewMapper());
        jdbi.registerRowMapper(new SkinProblemAnswerMapper());
        jdbi.registerRowMapper(new SkinProblemMapper());
        jdbi.registerRowMapper(new SkinTypeAnswerMapper());
        jdbi.registerRowMapper(new SkinTypeMapper());
        jdbi.registerRowMapper(new SleepHoursLessThanSevenAnswerMapper());
        jdbi.registerRowMapper(new SleepHoursLessThanSevenMapper());
        jdbi.registerRowMapper(new SleepQualityAnswerMapper());
        jdbi.registerRowMapper(new SleepQualityMapper());
        jdbi.registerRowMapper(new SmokeAnswerMapper());
        jdbi.registerRowMapper(new SmokeMapper());
        jdbi.registerRowMapper(new SportAmountAnswerMapper());
        jdbi.registerRowMapper(new SportAmountMapper());
        jdbi.registerRowMapper(new SportReasonAnswerMapper());
        jdbi.registerRowMapper(new SportReasonMapper());
        jdbi.registerRowMapper(new StressLevelAnswerMapper());
        jdbi.registerRowMapper(new StressLevelMapper());
        jdbi.registerRowMapper(new StressLevelConditionAnswerMapper());
        jdbi.registerRowMapper(new StressLevelConditionMapper());
        jdbi.registerRowMapper(new StressLevelAtEndOfDayAnswerMapper());
        jdbi.registerRowMapper(new StressLevelAtEndOfDayMapper());
        jdbi.registerRowMapper(new ThirtyMinutesOfSunAnswerMapper());
        jdbi.registerRowMapper(new ThirtyMinutesOfSunMapper());
        jdbi.registerRowMapper(new TiredWhenWakeUpAnswerMapper());
        jdbi.registerRowMapper(new TiredWhenWakeUpMapper());
        jdbi.registerRowMapper(new TrainingIntensivelyAnswerMapper());
        jdbi.registerRowMapper(new TrainingIntensivelyMapper());
        jdbi.registerRowMapper(new TransitionPeriodComplaintsAnswerMapper());
        jdbi.registerRowMapper(new TransitionPeriodComplaintsMapper());
        jdbi.registerRowMapper(new TroubleFallingAsleepAnswerMapper());
        jdbi.registerRowMapper(new TroubleFallingAsleepMapper());
        jdbi.registerRowMapper(new TypeOfTrainingAnswerMapper());
        jdbi.registerRowMapper(new TypeOfTrainingMapper());
        jdbi.registerRowMapper(new UrinaryInfectionAnswerMapper());
        jdbi.registerRowMapper(new UrinaryInfectionMapper());
        jdbi.registerRowMapper(new UsageGoalAnswerMapper());
        jdbi.registerRowMapper(new UsageGoalMapper());
        jdbi.registerRowMapper(new UserMapper());
        jdbi.registerRowMapper(new VitaminIntakeAnswerMapper());
        jdbi.registerRowMapper(new VitaminIntakeMapper());
        jdbi.registerRowMapper(new VitaminOpinionAnswerMapper());
        jdbi.registerRowMapper(new VitaminOpinionMapper());
        jdbi.registerRowMapper(new WeeklyTwelveAlcoholicDrinksAnswerMapper());
        jdbi.registerRowMapper(new WeeklyTwelveAlcoholicDrinksMapper());
        jdbi.registerRowMapper(new FacebookAccessTokenMapper());
        jdbi.registerRowMapper(new OrderMapper());
        jdbi.registerRowMapper(new WebsiteContentMapper());
        jdbi.registerArgument(new BlendIngredientReasonCodeArgument());
        jdbi.registerArgument(new UUIDArgument());
        jdbi.setSqlLogger(new InstrumentedSqlLogger(environment.metrics()));
        return jdbi;
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSourceProxy(@Qualifier("dataSource") DataSource dataSource) {
        return new TransactionAwareDataSourceProxy(dataSource);
    }

    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
