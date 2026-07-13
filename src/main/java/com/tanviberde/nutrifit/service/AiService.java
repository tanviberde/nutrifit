package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.ai.DailyMotivationResponse;
import com.tanviberde.nutrifit.dto.ai.HabitAnalysisResponse;
import com.tanviberde.nutrifit.dto.ai.WeeklyReportResponse;

public interface AiService {

    WeeklyReportResponse generateWeeklyReport(Long userId);

    DailyMotivationResponse generateDailyMotivation(Long userId);

    HabitAnalysisResponse generateHabitAnalysis(Long userId);
}