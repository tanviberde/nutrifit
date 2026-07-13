package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.dto.dashboard.DashboardResponse;
import com.tanviberde.nutrifit.dto.dashboard.PeriodSummaryResponse;
import com.tanviberde.nutrifit.entity.DailyProgress;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.exception.ResourceNotFoundException;
import com.tanviberde.nutrifit.repository.DailyProgressRepository;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.repository.WorkoutRepository;
import com.tanviberde.nutrifit.service.ActivityTrackingService;
import com.tanviberde.nutrifit.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DailyProgressRepository dailyProgressRepository;
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ActivityTrackingService activityTrackingService;

    @Override
    public DashboardResponse getDailyDashboard(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        DailyProgress progress = dailyProgressRepository
                .findByUserIdAndProgressDateAndDeletedFalse(userId, date)
                .orElse(null);

        double totalCalories = progress != null ? nz(progress.getTotalCalories()) : 0.0;
        double totalProtein = progress != null ? nz(progress.getTotalProtein()) : 0.0;
        double totalCarbs = progress != null ? nz(progress.getTotalCarbs()) : 0.0;
        double totalFat = progress != null ? nz(progress.getTotalFat()) : 0.0;
        double totalFibre = progress != null ? nz(progress.getTotalFibre()) : 0.0;
        double caloriesBurned = progress != null ? nz(progress.getCaloriesBurned()) : 0.0;
        int waterIntakeMl = progress != null && progress.getWaterIntakeMl() != null
                ? progress.getWaterIntakeMl() : 0;

        double calorieTarget = nz(user.getDailyCalorieTarget());
        double remainingCalories = calorieTarget - totalCalories + caloriesBurned;

        int workoutCount = workoutRepository
                .findByUserIdAndWorkoutDateAndDeletedFalse(userId, date)
                .size();

        int currentStreak = activityTrackingService.calculateCurrentStreak(userId);

        return DashboardResponse.builder()
                .date(date)
                .totalCalories(totalCalories)
                .totalProtein(totalProtein)
                .totalCarbs(totalCarbs)
                .totalFat(totalFat)
                .totalFibre(totalFibre)
                .caloriesBurned(caloriesBurned)
                .remainingCalories(remainingCalories)
                .workoutCount(workoutCount)
                .waterIntakeMl(waterIntakeMl)
                .currentStreak(currentStreak)
                .calorieTarget(user.getDailyCalorieTarget())
                .proteinTarget(user.getDailyProteinTarget())
                .carbTarget(user.getDailyCarbTarget())
                .fatTarget(user.getDailyFatTarget())
                .fibreTarget(user.getDailyFibreTarget())
                .build();
    }

    @Override
    public PeriodSummaryResponse getWeeklyDashboard(Long userId, LocalDate weekStartDate) {
        LocalDate weekEndDate = weekStartDate.plusDays(6);
        return buildPeriodSummary(userId, weekStartDate, weekEndDate);
    }

    @Override
    public PeriodSummaryResponse getMonthlyDashboard(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return buildPeriodSummary(userId, startDate, endDate);
    }

    private PeriodSummaryResponse buildPeriodSummary(Long userId, LocalDate startDate, LocalDate endDate) {
        List<DailyProgress> progressList = dailyProgressRepository
                .findByUserIdAndProgressDateBetweenAndDeletedFalse(userId, startDate, endDate);

        double totalCalories = progressList.stream().mapToDouble(p -> nz(p.getTotalCalories())).sum();
        double totalProtein = progressList.stream().mapToDouble(p -> nz(p.getTotalProtein())).sum();
        double totalCarbs = progressList.stream().mapToDouble(p -> nz(p.getTotalCarbs())).sum();
        double totalFat = progressList.stream().mapToDouble(p -> nz(p.getTotalFat())).sum();
        double totalFibre = progressList.stream().mapToDouble(p -> nz(p.getTotalFibre())).sum();
        double totalCaloriesBurned = progressList.stream().mapToDouble(p -> nz(p.getCaloriesBurned())).sum();

        long totalDaysInRange = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double avgDailyCalories = totalDaysInRange > 0 ? totalCalories / totalDaysInRange : 0.0;

        int workoutCount = workoutRepository
                .findByUserIdAndWorkoutDateBetweenAndDeletedFalse(userId, startDate, endDate)
                .size();

        int currentStreak = activityTrackingService.calculateCurrentStreak(userId);

        return PeriodSummaryResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalCalories(totalCalories)
                .avgDailyCalories(round(avgDailyCalories))
                .totalProtein(totalProtein)
                .totalCarbs(totalCarbs)
                .totalFat(totalFat)
                .totalFibre(totalFibre)
                .totalCaloriesBurned(totalCaloriesBurned)
                .workoutCount(workoutCount)
                .daysLogged(progressList.size())
                .currentStreak(currentStreak)
                .build();
    }

    private double nz(Double value) {
        return value == null ? 0.0 : value;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}