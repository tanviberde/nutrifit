package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.ai.AiResponseParser;
import com.tanviberde.nutrifit.ai.GeminiClient;
import com.tanviberde.nutrifit.ai.PromptBuilder;
import com.tanviberde.nutrifit.dto.ai.DailyMotivationResponse;
import com.tanviberde.nutrifit.dto.ai.HabitAnalysisResponse;
import com.tanviberde.nutrifit.dto.ai.WeeklyReportResponse;
import com.tanviberde.nutrifit.dto.dashboard.DashboardResponse;
import com.tanviberde.nutrifit.dto.dashboard.PeriodSummaryResponse;
import com.tanviberde.nutrifit.dto.weight.WeightResponse;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.entity.WeeklyReport;
import com.tanviberde.nutrifit.exception.ResourceNotFoundException;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.repository.WeeklyReportRepository;
import com.tanviberde.nutrifit.service.AiService;
import com.tanviberde.nutrifit.service.DashboardService;
import com.tanviberde.nutrifit.service.WeightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final UserRepository userRepository;
    private final DashboardService dashboardService;
    private final WeightService weightService;
    private final WeeklyReportRepository weeklyReportRepository;
    private final PromptBuilder promptBuilder;
    private final GeminiClient geminiClient;
    private final AiResponseParser aiResponseParser;

    @Override
    public WeeklyReportResponse generateWeeklyReport(Long userId) {
        User user = getUser(userId);

        LocalDate weekEnd = LocalDate.now();
        LocalDate weekStart = weekEnd.minusDays(6);

        PeriodSummaryResponse summary = dashboardService.getWeeklyDashboard(userId, weekStart);

        String prompt = promptBuilder.buildWeeklyReportPrompt(user, summary);
        String reportContent = geminiClient.generateContent(prompt);

        WeeklyReport report = weeklyReportRepository
                .findByUserIdAndWeekStartDateAndDeletedFalse(userId, weekStart)
                .orElseGet(() -> WeeklyReport.builder()
                        .user(user)
                        .weekStartDate(weekStart)
                        .weekEndDate(weekEnd)
                        .build());

        report.setReportContent(reportContent);
        WeeklyReport savedReport = weeklyReportRepository.save(report);

        return WeeklyReportResponse.fromEntity(savedReport);
    }

    @Override
    public DailyMotivationResponse generateDailyMotivation(Long userId) {
        User user = getUser(userId);
        LocalDate today = LocalDate.now();

        DashboardResponse dashboard = dashboardService.getDailyDashboard(userId, today);
        String prompt = promptBuilder.buildMotivationPrompt(user, dashboard);
        String message = geminiClient.generateContent(prompt);

        return DailyMotivationResponse.builder()
                .message(message.trim())
                .generatedForDate(today)
                .build();
    }

    @Override
    public HabitAnalysisResponse generateHabitAnalysis(Long userId) {
        User user = getUser(userId);
        LocalDate today = LocalDate.now();
        LocalDate periodStart = today.minusDays(29);

        PeriodSummaryResponse monthSummary = dashboardService.getWeeklyDashboard(userId, periodStart);

        List<WeightResponse> allWeightEntries = weightService.getWeightEntries(userId);
        List<WeightResponse> recentWeightEntries = allWeightEntries.stream()
                .filter(w -> !w.getEntryDate().isBefore(periodStart))
                .toList();

        String prompt = promptBuilder.buildHabitAnalysisPrompt(user, monthSummary, recentWeightEntries);
        String rawResponse = geminiClient.generateContent(prompt);

        return aiResponseParser.parseHabitAnalysis(rawResponse);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}