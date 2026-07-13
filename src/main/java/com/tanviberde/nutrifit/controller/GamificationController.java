package com.tanviberde.nutrifit.controller;

import com.tanviberde.nutrifit.dto.common.ApiResponse;
import com.tanviberde.nutrifit.dto.gamification.AchievementResponse;
import com.tanviberde.nutrifit.dto.gamification.StreakSummaryResponse;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gamification")
@RequiredArgsConstructor
public class GamificationController {

    private final GamificationService gamificationService;

    @GetMapping("/achievements")
    public ApiResponse<List<AchievementResponse>> getAchievements(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<AchievementResponse> response = gamificationService.getAchievements(principal.getId());
        return ApiResponse.success(response);
    }

    @GetMapping("/streaks")
    public ApiResponse<StreakSummaryResponse> getStreakSummary(
            @AuthenticationPrincipal UserPrincipal principal) {
        StreakSummaryResponse response = gamificationService.getStreakSummary(principal.getId());
        return ApiResponse.success(response);
    }
}