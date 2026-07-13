package com.tanviberde.nutrifit.controller;

import com.tanviberde.nutrifit.dto.ai.DailyMotivationResponse;
import com.tanviberde.nutrifit.dto.ai.HabitAnalysisResponse;
import com.tanviberde.nutrifit.dto.ai.WeeklyReportResponse;
import com.tanviberde.nutrifit.dto.common.ApiResponse;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("/weekly-report")
    public ApiResponse<WeeklyReportResponse> getWeeklyReport(
            @AuthenticationPrincipal UserPrincipal principal) {
        WeeklyReportResponse response = aiService.generateWeeklyReport(principal.getId());
        return ApiResponse.success(response);
    }

    @GetMapping("/daily-motivation")
    public ApiResponse<DailyMotivationResponse> getDailyMotivation(
            @AuthenticationPrincipal UserPrincipal principal) {
        DailyMotivationResponse response = aiService.generateDailyMotivation(principal.getId());
        return ApiResponse.success(response);
    }

    @GetMapping("/habit-analysis")
    public ApiResponse<HabitAnalysisResponse> getHabitAnalysis(
            @AuthenticationPrincipal UserPrincipal principal) {
        HabitAnalysisResponse response = aiService.generateHabitAnalysis(principal.getId());
        return ApiResponse.success(response);
    }
}