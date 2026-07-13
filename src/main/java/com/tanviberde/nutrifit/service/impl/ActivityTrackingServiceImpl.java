package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.entity.ActivityLog;
import com.tanviberde.nutrifit.entity.DailyProgress;
import com.tanviberde.nutrifit.entity.Meal;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.entity.Workout;
import com.tanviberde.nutrifit.repository.ActivityLogRepository;
import com.tanviberde.nutrifit.repository.DailyProgressRepository;
import com.tanviberde.nutrifit.repository.MealRepository;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.repository.WorkoutRepository;
import com.tanviberde.nutrifit.service.ActivityTrackingService;
import com.tanviberde.nutrifit.util.StreakCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityTrackingServiceImpl implements ActivityTrackingService {

    private static final int STREAK_LOOKBACK_DAYS = 365;

    private final MealRepository mealRepository;
    private final WorkoutRepository workoutRepository;
    private final DailyProgressRepository dailyProgressRepository;
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;

    @Override
    public void recalculateDailyProgress(Long userId, LocalDate date) {
        List<Meal> meals = mealRepository.findByUserIdAndMealDateAndDeletedFalse(userId, date);
        List<Workout> workouts = workoutRepository.findByUserIdAndWorkoutDateAndDeletedFalse(userId, date);

        double totalCalories = meals.stream().mapToDouble(m -> nz(m.getCalories())).sum();
        double totalProtein = meals.stream().mapToDouble(m -> nz(m.getProtein())).sum();
        double totalCarbs = meals.stream().mapToDouble(m -> nz(m.getCarbs())).sum();
        double totalFat = meals.stream().mapToDouble(m -> nz(m.getFat())).sum();
        double totalFibre = meals.stream().mapToDouble(m -> nz(m.getFibre())).sum();
        double caloriesBurned = workouts.stream().mapToDouble(w -> nz(w.getCaloriesBurned())).sum();

        DailyProgress progress = dailyProgressRepository
                .findByUserIdAndProgressDateAndDeletedFalse(userId, date)
                .orElseGet(() -> DailyProgress.builder()
                        .user(userRepository.getReferenceById(userId))
                        .progressDate(date)
                        .waterIntakeMl(0)
                        .build());

        progress.setTotalCalories(totalCalories);
        progress.setTotalProtein(totalProtein);
        progress.setTotalCarbs(totalCarbs);
        progress.setTotalFat(totalFat);
        progress.setTotalFibre(totalFibre);
        progress.setCaloriesBurned(caloriesBurned);

        dailyProgressRepository.save(progress);
    }

    @Override
    public void logMealActivity(Long userId, LocalDate date) {
        upsertActivityLog(userId, date, log -> log.setLoggedMeal(true));
    }

    @Override
    public void logWorkoutActivity(Long userId, LocalDate date) {
        upsertActivityLog(userId, date, log -> log.setLoggedWorkout(true));
    }

    @Override
    public void logWeightActivity(Long userId, LocalDate date) {
        upsertActivityLog(userId, date, log -> log.setLoggedWeight(true));
    }

    @Override
    public int calculateCurrentStreak(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate rangeStart = today.minusDays(STREAK_LOOKBACK_DAYS);

        List<ActivityLog> logs = activityLogRepository
                .findByUserIdAndActivityDateBetweenAndDeletedFalseOrderByActivityDateDesc(
                        userId, rangeStart, today);

        Set<LocalDate> activeDates = logs.stream()
                .filter(log -> log.isLoggedMeal() || log.isLoggedWorkout())
                .map(ActivityLog::getActivityDate)
                .collect(Collectors.toSet());

        return StreakCalculator.calculateStreak(activeDates, today);
    }

    private void upsertActivityLog(Long userId, LocalDate date, Consumer<ActivityLog> mutator) {
        ActivityLog log = activityLogRepository
                .findByUserIdAndActivityDateAndDeletedFalse(userId, date)
                .orElseGet(() -> ActivityLog.builder()
                        .user(userRepository.getReferenceById(userId))
                        .activityDate(date)
                        .build());

        mutator.accept(log);
        activityLogRepository.save(log);
    }

    private double nz(Double value) {
        return value == null ? 0.0 : value;
    }
}