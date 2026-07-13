package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.gamification.AchievementResponse;
import com.tanviberde.nutrifit.dto.gamification.StreakSummaryResponse;

import java.util.List;

public interface GamificationService {

    void checkAndAwardAchievements(Long userId);

    List<AchievementResponse> getAchievements(Long userId);

    StreakSummaryResponse getStreakSummary(Long userId);
}