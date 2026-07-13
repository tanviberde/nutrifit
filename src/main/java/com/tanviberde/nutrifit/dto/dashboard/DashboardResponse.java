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
public class DashboardResponse {

    private LocalDate date;
    private Double totalCalories;
    private Double totalProtein;
    private Double totalCarbs;
    private Double totalFat;
    private Double totalFibre;
    private Double caloriesBurned;
    private Double remainingCalories;
    private Integer workoutCount;
    private Integer waterIntakeMl;
    private Integer currentStreak;
    private Double calorieTarget;
    private Double proteinTarget;
    private Double carbTarget;
    private Double fatTarget;
    private Double fibreTarget;
}