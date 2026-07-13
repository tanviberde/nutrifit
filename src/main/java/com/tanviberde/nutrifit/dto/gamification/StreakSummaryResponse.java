package com.tanviberde.nutrifit.dto.gamification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StreakSummaryResponse {

    private int dailyStreak;
    private int workoutStreak;
    private int nutritionStreak;
}