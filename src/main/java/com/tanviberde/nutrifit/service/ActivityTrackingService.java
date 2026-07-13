package com.tanviberde.nutrifit.service;

import java.time.LocalDate;

public interface ActivityTrackingService {

    void recalculateDailyProgress(Long userId, LocalDate date);

    void logMealActivity(Long userId, LocalDate date);

    void logWorkoutActivity(Long userId, LocalDate date);

    void logWeightActivity(Long userId, LocalDate date);

    int calculateCurrentStreak(Long userId);
}