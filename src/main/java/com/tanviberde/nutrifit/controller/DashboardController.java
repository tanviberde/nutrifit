package com.tanviberde.nutrifit.controller;

import com.tanviberde.nutrifit.dto.common.ApiResponse;
import com.tanviberde.nutrifit.dto.dashboard.DashboardResponse;
import com.tanviberde.nutrifit.dto.dashboard.PeriodSummaryResponse;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/daily")
    public ApiResponse<DashboardResponse> getDailyDashboard(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        DashboardResponse response = dashboardService.getDailyDashboard(principal.getId(), targetDate);
        return ApiResponse.success(response);
    }

    @GetMapping("/weekly")
    public ApiResponse<PeriodSummaryResponse> getWeeklyDashboard(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        LocalDate targetWeekStart = weekStart != null ? weekStart : LocalDate.now().minusDays(6);
        PeriodSummaryResponse response =
                dashboardService.getWeeklyDashboard(principal.getId(), targetWeekStart);
        return ApiResponse.success(response);
    }

    @GetMapping("/monthly")
    public ApiResponse<PeriodSummaryResponse> getMonthlyDashboard(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        LocalDate now = LocalDate.now();
        int targetYear = year != null ? year : now.getYear();
        int targetMonth = month != null ? month : now.getMonthValue();

        PeriodSummaryResponse response =
                dashboardService.getMonthlyDashboard(principal.getId(), targetYear, targetMonth);
        return ApiResponse.success(response);
    }
}