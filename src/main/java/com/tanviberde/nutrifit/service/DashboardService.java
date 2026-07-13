package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.dashboard.DashboardResponse;
import com.tanviberde.nutrifit.dto.dashboard.PeriodSummaryResponse;

import java.time.LocalDate;

public interface DashboardService {

    DashboardResponse getDailyDashboard(Long userId, LocalDate date);

    PeriodSummaryResponse getWeeklyDashboard(Long userId, LocalDate weekStartDate);

    PeriodSummaryResponse getMonthlyDashboard(Long userId, int year, int month);
}