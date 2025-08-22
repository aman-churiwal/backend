package viteezy.domain;

import viteezy.domain.quiz.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class QuizAggregatedInformationV2 {

    private final Quiz quiz;
    private final Long blendId;

    private final Optional<AcnePlace> acnePlace;
    private final List<AllergyType> allergyTypes;
    private final Optional<AmountOfFiberConsumption> amountOfFiberConsumption;
    private final Optional<AmountOfFishConsumption> amountOfFishConsumption;
    private final Optional<AmountOfFruitConsumption> amountOfFruitConsumption;
    private final Optional<AmountOfMeatConsumption> amountOfMeatConsumption;
    private final Optional<AmountOfProteinConsumption> amountOfProteinConsumption;
    private final Optional<AmountOfVegetableConsumption> amountOfVegetableConsumption;
    private final Optional<AttentionFocus> attentionFocus;
    private final Optional<AttentionState> attentionState;
    private final Optional<AverageSleepingHours> averageSleepingHours;
    private final Optional<ChildrenWish> childrenWish;
    private final Optional<CurrentLibido> currentLibido;
    private final Optional<DailyFourCoffee> dailyFourCoffee;
    private final Optional<DailySixAlcoholicDrinks> dailySixAlcoholicDrinks;
    private final Optional<DateOfBirthAnswer> dateOfBirthAnswer;
    private final List<DietIntolerance> dietIntolerance;
    private final Optional<DietType> dietType;
    private final Optional<DigestionAmount> digestionAmount;
    private final Optional<DigestionOccurrence> digestionOccurrence;
    private final Optional<DrySkin> drySkin;
    private final Optional<EnergyState> energyState;
    private final Optional<Gender> gender;
    private final Optional<HairType> hairType;
    private final Optional<IronPrescribed> ironPrescribed;
    private final Optional<LackOfConcentration> lackOfConcentration;
    private final Optional<LibidoStressLevel> libidoStressLevel;
    private final Optional<MentalFitness> mentalFitness;
    private final Optional<MenstruationInterval> menstruationInterval;
    private final Optional<MenstruationMood> menstruationMood;
    private final Optional<MenstruationSideIssue> menstruationSideIssue;
    private final Optional<NailImprovement> nailImprovement;
    private final Optional<OftenHavingFlu> oftenHavingFlu;
    private final Optional<PregnancyState> pregnancyState;
    private final Optional<PresentAtCrowdedPlaces> presentAtCrowdedPlaces;
    private final Optional<PrimaryGoal> primaryGoal;
    private final Optional<SkinProblem> skinProblem;
    private final Optional<SkinType> skinType;
    private final Optional<SleepQuality> sleepQuality;
    private final Optional<Smoke> smoke;
    private final Optional<SportAmount> sportAmount;
    private final Optional<StressLevelAtEndOfDay> stressLevelAtEndOfDay;
    private final Optional<ThirtyMinutesOfSun> thirtyMinutesOfSun;
    private final Optional<TiredWhenWakeUp> tiredWhenWakeUp;
    private final Optional<TrainingIntensively> trainingIntensively;
    private final Optional<TransitionPeriodComplaints> transitionPeriodComplaints;
    private final Optional<TroubleFallingAsleep> troubleFallingAsleep;
    private final Optional<UrinaryInfection> urinaryInfection;
    private final List<UsageGoal> usageGoals;
    private final Optional<VitaminIntake> vitaminIntake;
    private final Optional<WeeklyTwelveAlcoholicDrinks> weeklyTwelveAlcoholicDrinks;

    public QuizAggregatedInformationV2(
            Quiz quiz, Long blendId, Optional<AcnePlace> acnePlace, List<AllergyType> allergyTypes,
            Optional<AmountOfFiberConsumption> amountOfFiberConsumption,
            Optional<AmountOfFishConsumption> amountOfFishConsumption,
            Optional<AmountOfFruitConsumption> amountOfFruitConsumption,
            Optional<AmountOfMeatConsumption> amountOfMeatConsumption,
            Optional<AmountOfProteinConsumption> amountOfProteinConsumption,
            Optional<AmountOfVegetableConsumption> amountOfVegetableConsumption,
            Optional<AttentionFocus> attentionFocus, Optional<AttentionState> attentionState,
            Optional<AverageSleepingHours> averageSleepingHours,
            Optional<ChildrenWish> childrenWish, Optional<CurrentLibido> currentLibido,
            Optional<DailyFourCoffee> dailyFourCoffee, Optional<DailySixAlcoholicDrinks> dailySixAlcoholicDrinks,
            Optional<DateOfBirthAnswer> dateOfBirthAnswer, List<DietIntolerance> dietIntolerance,
            Optional<DietType> dietType, Optional<DigestionAmount> digestionAmount,
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
        this.quiz = quiz;
        this.blendId = blendId;
        this.acnePlace = acnePlace;
        this.allergyTypes = allergyTypes;
        this.amountOfFiberConsumption = amountOfFiberConsumption;
        this.amountOfFishConsumption = amountOfFishConsumption;
        this.amountOfFruitConsumption = amountOfFruitConsumption;
        this.amountOfMeatConsumption = amountOfMeatConsumption;
        this.amountOfProteinConsumption = amountOfProteinConsumption;
        this.amountOfVegetableConsumption = amountOfVegetableConsumption;
        this.attentionFocus = attentionFocus;
        this.attentionState = attentionState;
        this.averageSleepingHours = averageSleepingHours;
        this.childrenWish = childrenWish;
        this.currentLibido = currentLibido;
        this.dailyFourCoffee = dailyFourCoffee;
        this.dailySixAlcoholicDrinks = dailySixAlcoholicDrinks;
        this.dateOfBirthAnswer = dateOfBirthAnswer;
        this.dietIntolerance = dietIntolerance;
        this.dietType = dietType;
        this.digestionAmount = digestionAmount;
        this.digestionOccurrence = digestionOccurrence;
        this.drySkin = drySkin;
        this.energyState = energyState;
        this.gender = gender;
        this.hairType = hairType;
        this.ironPrescribed = ironPrescribed;
        this.lackOfConcentration = lackOfConcentration;
        this.libidoStressLevel = libidoStressLevel;
        this.mentalFitness = mentalFitness;
        this.menstruationInterval = menstruationInterval;
        this.menstruationMood = menstruationMood;
        this.menstruationSideIssue = menstruationSideIssue;
        this.nailImprovement = nailImprovement;
        this.oftenHavingFlu = oftenHavingFlu;
        this.pregnancyState = pregnancyState;
        this.presentAtCrowdedPlaces = presentAtCrowdedPlaces;
        this.primaryGoal = primaryGoal;
        this.skinProblem = skinProblem;
        this.skinType = skinType;
        this.sleepQuality = sleepQuality;
        this.smoke = smoke;
        this.sportAmount = sportAmount;
        this.stressLevelAtEndOfDay = stressLevelAtEndOfDay;
        this.thirtyMinutesOfSun = thirtyMinutesOfSun;
        this.tiredWhenWakeUp = tiredWhenWakeUp;
        this.trainingIntensively = trainingIntensively;
        this.transitionPeriodComplaints = transitionPeriodComplaints;
        this.troubleFallingAsleep = troubleFallingAsleep;
        this.urinaryInfection = urinaryInfection;
        this.usageGoals = usageGoals;
        this.vitaminIntake = vitaminIntake;
        this.weeklyTwelveAlcoholicDrinks = weeklyTwelveAlcoholicDrinks;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public Long getBlendId() {
        return blendId;
    }

    public Optional<AcnePlace> getAcnePlace() {
        return acnePlace;
    }

    public List<AllergyType> getAllergyTypes() {
        return allergyTypes;
    }

    public Optional<AmountOfFiberConsumption> getAmountOfFiberConsumption() {
        return amountOfFiberConsumption;
    }

    public Optional<AmountOfFishConsumption> getAmountOfFishConsumption() {
        return amountOfFishConsumption;
    }

    public Optional<AmountOfFruitConsumption> getAmountOfFruitConsumption() {
        return amountOfFruitConsumption;
    }

    public Optional<AmountOfMeatConsumption> getAmountOfMeatConsumption() {
        return amountOfMeatConsumption;
    }

    public Optional<AmountOfProteinConsumption> getAmountOfProteinConsumption() {
        return amountOfProteinConsumption;
    }

    public Optional<AmountOfVegetableConsumption> getAmountOfVegetableConsumption() {
        return amountOfVegetableConsumption;
    }

    public Optional<AttentionFocus> getAttentionFocus() {
        return attentionFocus;
    }

    public Optional<AttentionState> getAttentionState() {
        return attentionState;
    }

    public Optional<AverageSleepingHours> getAverageSleepingHours() {
        return averageSleepingHours;
    }

    public Optional<ChildrenWish> getChildrenWish() {
        return childrenWish;
    }

    public Optional<CurrentLibido> getCurrentLibido() {
        return currentLibido;
    }

    public Optional<DailyFourCoffee> getDailyFourCoffee() {
        return dailyFourCoffee;
    }

    public Optional<DailySixAlcoholicDrinks> getDailySixAlcoholicDrinks() {
        return dailySixAlcoholicDrinks;
    }

    public Optional<DateOfBirthAnswer> getDateOfBirthAnswer() {
        return dateOfBirthAnswer;
    }

    public List<DietIntolerance> getDietIntolerance() {
        return dietIntolerance;
    }

    public Optional<DietType> getDietType() {
        return dietType;
    }

    public Optional<DigestionOccurrence> getDigestionOccurrence() {
        return digestionOccurrence;
    }

    public Optional<DigestionAmount> getDigestionAmount() {
        return digestionAmount;
    }

    public Optional<DrySkin> getDrySkin() {
        return drySkin;
    }

    public Optional<EnergyState> getEnergyState() {
        return energyState;
    }

    public Optional<Gender> getGender() {
        return gender;
    }

    public Optional<HairType> getHairType() {
        return hairType;
    }

    public Optional<IronPrescribed> getIronPrescribed() {
        return ironPrescribed;
    }

    public Optional<LackOfConcentration> getLackOfConcentration() {
        return lackOfConcentration;
    }

    public Optional<LibidoStressLevel> getLibidoStressLevel() {
        return libidoStressLevel;
    }

    public Optional<MentalFitness> getMentalFitness() {
        return mentalFitness;
    }

    public Optional<MenstruationInterval> getMenstruationInterval() {
        return menstruationInterval;
    }

    public Optional<MenstruationMood> getMenstruationMood() {
        return menstruationMood;
    }

    public Optional<MenstruationSideIssue> getMenstruationSideIssue() {
        return menstruationSideIssue;
    }

    public Optional<NailImprovement> getNailImprovement() {
        return nailImprovement;
    }

    public Optional<OftenHavingFlu> getOftenHavingFlu() {
        return oftenHavingFlu;
    }

    public Optional<PregnancyState> getPregnancyState() {
        return pregnancyState;
    }

    public Optional<PresentAtCrowdedPlaces> getPresentAtCrowdedPlaces() {
        return presentAtCrowdedPlaces;
    }

    public Optional<PrimaryGoal> getPrimaryGoal() {
        return primaryGoal;
    }

    public Optional<SkinProblem> getSkinProblem() {
        return skinProblem;
    }

    public Optional<SkinType> getSkinType() {
        return skinType;
    }

    public Optional<SleepQuality> getSleepQuality() {
        return sleepQuality;
    }

    public Optional<Smoke> getSmoke() {
        return smoke;
    }

    public Optional<SportAmount> getSportAmount() {
        return sportAmount;
    }

    public Optional<StressLevelAtEndOfDay> getStressLevelAtEndOfDay() {
        return stressLevelAtEndOfDay;
    }

    public Optional<ThirtyMinutesOfSun> getThirtyMinutesOfSun() {
        return thirtyMinutesOfSun;
    }

    public Optional<TiredWhenWakeUp> getTiredWhenWakeUp() {
        return tiredWhenWakeUp;
    }

    public Optional<TrainingIntensively> getTrainingIntensively() {
        return trainingIntensively;
    }

    public Optional<TransitionPeriodComplaints> getTransitionPeriodComplaints() {
        return transitionPeriodComplaints;
    }

    public Optional<TroubleFallingAsleep> getTroubleFallingAsleep() {
        return troubleFallingAsleep;
    }

    public Optional<UrinaryInfection> getUrinaryInfection() {
        return urinaryInfection;
    }

    public List<UsageGoal> getUsageGoals() {
        return usageGoals;
    }

    public Optional<VitaminIntake> getVitaminIntake() {
        return vitaminIntake;
    }

    public Optional<WeeklyTwelveAlcoholicDrinks> getWeeklyTwelveAlcoholicDrinks() {
        return weeklyTwelveAlcoholicDrinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizAggregatedInformationV2 that = (QuizAggregatedInformationV2) o;
        return Objects.equals(quiz, that.quiz) && Objects.equals(blendId, that.blendId) && Objects.equals(acnePlace, that.acnePlace) && Objects.equals(allergyTypes, that.allergyTypes) && Objects.equals(amountOfFiberConsumption, that.amountOfFiberConsumption) && Objects.equals(amountOfFishConsumption, that.amountOfFishConsumption) && Objects.equals(amountOfFruitConsumption, that.amountOfFruitConsumption) && Objects.equals(amountOfMeatConsumption, that.amountOfMeatConsumption) && Objects.equals(amountOfProteinConsumption, that.amountOfProteinConsumption) && Objects.equals(amountOfVegetableConsumption, that.amountOfVegetableConsumption) && Objects.equals(attentionFocus, that.attentionFocus) && Objects.equals(attentionState, that.attentionState) && Objects.equals(averageSleepingHours, that.averageSleepingHours) && Objects.equals(childrenWish, that.childrenWish) && Objects.equals(currentLibido, that.currentLibido) && Objects.equals(dailyFourCoffee, that.dailyFourCoffee) && Objects.equals(dailySixAlcoholicDrinks, that.dailySixAlcoholicDrinks) && Objects.equals(dateOfBirthAnswer, that.dateOfBirthAnswer) && Objects.equals(dietIntolerance, that.dietIntolerance) && Objects.equals(dietType, that.dietType) && Objects.equals(digestionAmount, that.digestionAmount) && Objects.equals(digestionOccurrence, that.digestionOccurrence) && Objects.equals(drySkin, that.drySkin) && Objects.equals(energyState, that.energyState) && Objects.equals(gender, that.gender) && Objects.equals(hairType, that.hairType) && Objects.equals(ironPrescribed, that.ironPrescribed) && Objects.equals(lackOfConcentration, that.lackOfConcentration) && Objects.equals(libidoStressLevel, that.libidoStressLevel) && Objects.equals(mentalFitness, that.mentalFitness) && Objects.equals(menstruationInterval, that.menstruationInterval) && Objects.equals(menstruationMood, that.menstruationMood) && Objects.equals(menstruationSideIssue, that.menstruationSideIssue) && Objects.equals(nailImprovement, that.nailImprovement) && Objects.equals(oftenHavingFlu, that.oftenHavingFlu) && Objects.equals(pregnancyState, that.pregnancyState) && Objects.equals(presentAtCrowdedPlaces, that.presentAtCrowdedPlaces) && Objects.equals(primaryGoal, that.primaryGoal) && Objects.equals(skinProblem, that.skinProblem) && Objects.equals(skinType, that.skinType) && Objects.equals(sleepQuality, that.sleepQuality) && Objects.equals(smoke, that.smoke) && Objects.equals(sportAmount, that.sportAmount) && Objects.equals(stressLevelAtEndOfDay, that.stressLevelAtEndOfDay) && Objects.equals(thirtyMinutesOfSun, that.thirtyMinutesOfSun) && Objects.equals(tiredWhenWakeUp, that.tiredWhenWakeUp) && Objects.equals(trainingIntensively, that.trainingIntensively) && Objects.equals(transitionPeriodComplaints, that.transitionPeriodComplaints) && Objects.equals(troubleFallingAsleep, that.troubleFallingAsleep) && Objects.equals(urinaryInfection, that.urinaryInfection) && Objects.equals(usageGoals, that.usageGoals) && Objects.equals(vitaminIntake, that.vitaminIntake) && Objects.equals(weeklyTwelveAlcoholicDrinks, that.weeklyTwelveAlcoholicDrinks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quiz, blendId, acnePlace, allergyTypes, amountOfFiberConsumption, amountOfFishConsumption, amountOfFruitConsumption, amountOfMeatConsumption, amountOfProteinConsumption, amountOfVegetableConsumption, attentionFocus, attentionState, averageSleepingHours, childrenWish, currentLibido, dailyFourCoffee, dailySixAlcoholicDrinks, dateOfBirthAnswer, dietIntolerance, dietType, digestionAmount, digestionOccurrence, drySkin, energyState, gender, hairType, ironPrescribed, lackOfConcentration, libidoStressLevel, mentalFitness, menstruationInterval, menstruationMood, menstruationSideIssue, nailImprovement, oftenHavingFlu, pregnancyState, presentAtCrowdedPlaces, primaryGoal, skinProblem, skinType, sleepQuality, smoke, sportAmount, stressLevelAtEndOfDay, thirtyMinutesOfSun, tiredWhenWakeUp, trainingIntensively, transitionPeriodComplaints, troubleFallingAsleep, urinaryInfection, usageGoals, vitaminIntake, weeklyTwelveAlcoholicDrinks);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", QuizAggregatedInformationV2.class.getSimpleName() + "[", "]")
                .add("quiz=" + quiz)
                .add("blendId=" + blendId)
                .add("acnePlace=" + acnePlace)
                .add("allergyTypes=" + allergyTypes)
                .add("amountOfFiberConsumption=" + amountOfFiberConsumption)
                .add("amountOfFishConsumption=" + amountOfFishConsumption)
                .add("amountOfFruitConsumption=" + amountOfFruitConsumption)
                .add("amountOfMeatConsumption=" + amountOfMeatConsumption)
                .add("amountOfProteinConsumption=" + amountOfProteinConsumption)
                .add("amountOfVegetableConsumption=" + amountOfVegetableConsumption)
                .add("attentionFocus=" + attentionFocus)
                .add("attentionState=" + attentionState)
                .add("averageSleepingHours=" + averageSleepingHours)
                .add("childrenWish=" + childrenWish)
                .add("currentLibido=" + currentLibido)
                .add("dailyFourCoffee=" + dailyFourCoffee)
                .add("dailySixAlcoholicDrinks=" + dailySixAlcoholicDrinks)
                .add("dateOfBirthAnswer=" + dateOfBirthAnswer)
                .add("dietIntolerance=" + dietIntolerance)
                .add("dietType=" + dietType)
                .add("digestionAmount=" + digestionAmount)
                .add("digestionOccurrence=" + digestionOccurrence)
                .add("drySkin=" + drySkin)
                .add("energyState=" + energyState)
                .add("gender=" + gender)
                .add("hairType=" + hairType)
                .add("ironPrescribed=" + ironPrescribed)
                .add("lackOfConcentration=" + lackOfConcentration)
                .add("libidoStressLevel=" + libidoStressLevel)
                .add("mentalFitness=" + mentalFitness)
                .add("menstruationInterval=" + menstruationInterval)
                .add("menstruationMood=" + menstruationMood)
                .add("menstruationSideIssue=" + menstruationSideIssue)
                .add("nailImprovement=" + nailImprovement)
                .add("oftenHavingFlu=" + oftenHavingFlu)
                .add("pregnancyState=" + pregnancyState)
                .add("presentAtCrowdedPlaces=" + presentAtCrowdedPlaces)
                .add("primaryGoal=" + primaryGoal)
                .add("skinProblem=" + skinProblem)
                .add("skinType=" + skinType)
                .add("sleepQuality=" + sleepQuality)
                .add("smoke=" + smoke)
                .add("sportAmount=" + sportAmount)
                .add("stressLevelAtEndOfDay=" + stressLevelAtEndOfDay)
                .add("thirtyMinutesOfSun=" + thirtyMinutesOfSun)
                .add("tiredWhenWakeUp=" + tiredWhenWakeUp)
                .add("trainingIntensively=" + trainingIntensively)
                .add("transitionPeriodComplaints=" + transitionPeriodComplaints)
                .add("troubleFallingAsleep=" + troubleFallingAsleep)
                .add("urinaryInfection=" + urinaryInfection)
                .add("usageGoals=" + usageGoals)
                .add("vitaminIntake=" + vitaminIntake)
                .add("weeklyTwelveAlcoholicDrinks=" + weeklyTwelveAlcoholicDrinks)
                .toString();
    }
}
