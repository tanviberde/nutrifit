package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.entity.enums.ActivityLevel;
import com.tanviberde.nutrifit.entity.enums.FitnessGoal;
import com.tanviberde.nutrifit.entity.enums.Gender;
import com.tanviberde.nutrifit.service.NutritionCalculatorService;
import org.springframework.stereotype.Service;

@Service
public class NutritionCalculatorServiceImpl implements NutritionCalculatorService {

    @Override
    public void calculateAndSetTargets(User user) {
        double bmr = calculateBmr(user);
        double tdee = bmr * getActivityMultiplier(user.getActivityLevel());
        double calorieTarget = applyGoalAdjustment(tdee, user.getFitnessGoal());

        double proteinGramsPerKg = getProteinGramsPerKg(user.getFitnessGoal());
        double proteinTarget = proteinGramsPerKg * user.getWeightKg();
        double proteinCalories = proteinTarget * 4;

        double fatCalories = calorieTarget * 0.25;
        double fatTarget = fatCalories / 9;

        double remainingCalories = calorieTarget - proteinCalories - fatCalories;
        double carbTarget = Math.max(remainingCalories, 0) / 4;

        double fibreTarget = (calorieTarget / 1000.0) * 14;

        user.setDailyCalorieTarget(round(calorieTarget));
        user.setDailyProteinTarget(round(proteinTarget));
        user.setDailyCarbTarget(round(carbTarget));
        user.setDailyFatTarget(round(fatTarget));
        user.setDailyFibreTarget(round(fibreTarget));
    }

    private double calculateBmr(User user) {
        double weight = user.getWeightKg();
        double height = user.getHeightCm();
        int age = user.getAge();
        Gender gender = user.getGender();

        double base = (10 * weight) + (6.25 * height) - (5 * age);

        if (gender == Gender.MALE) {
            return base + 5;
        } else if (gender == Gender.FEMALE) {
            return base - 161;
        } else {
            return base - 78;
        }
    }

    private double getActivityMultiplier(ActivityLevel activityLevel) {
        return switch (activityLevel) {
            case SEDENTARY -> 1.2;
            case LIGHTLY_ACTIVE -> 1.375;
            case MODERATELY_ACTIVE -> 1.55;
            case VERY_ACTIVE -> 1.725;
            case EXTRA_ACTIVE -> 1.9;
        };
    }

    private double applyGoalAdjustment(double tdee, FitnessGoal fitnessGoal) {
        return switch (fitnessGoal) {
            case WEIGHT_LOSS -> tdee - 500;
            case WEIGHT_GAIN -> tdee + 500;
            case MUSCLE_GAIN -> tdee + 300;
            case MAINTENANCE -> tdee;
        };
    }

    private double getProteinGramsPerKg(FitnessGoal fitnessGoal) {
        return switch (fitnessGoal) {
            case WEIGHT_LOSS -> 1.8;
            case MUSCLE_GAIN -> 2.0;
            case WEIGHT_GAIN, MAINTENANCE -> 1.6;
        };
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}