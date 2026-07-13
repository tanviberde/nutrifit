package com.tanviberde.nutrifit.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodSummaryResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalCalories;
    private Double avgDailyCalories;
    private Double totalProtein;
    private Double totalCarbs;
    private Double totalFat;
    private Double totalFibre;
    private Double totalCaloriesBurned;
    private Integer workoutCount;
    private Integer daysLogged;
    private Integer currentStreak;
}