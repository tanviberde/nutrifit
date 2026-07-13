package com.tanviberde.nutrifit.controller;

import com.tanviberde.nutrifit.dto.common.ApiResponse;
import com.tanviberde.nutrifit.dto.common.PageResponse;
import com.tanviberde.nutrifit.dto.history.HistoryFilterRequest;
import com.tanviberde.nutrifit.dto.meal.MealResponse;
import com.tanviberde.nutrifit.dto.weight.WeightResponse;
import com.tanviberde.nutrifit.dto.workout.WorkoutResponse;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/meals")
    public ApiResponse<PageResponse<MealResponse>> getMealHistory(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        HistoryFilterRequest filter = HistoryFilterRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .searchKeyword(keyword)
                .build();

        PageResponse<MealResponse> response =
                PageResponse.fromPage(historyService.getMealHistory(principal.getId(), filter, pageable));
        return ApiResponse.success(response);
    }

    @GetMapping("/workouts")
    public ApiResponse<PageResponse<WorkoutResponse>> getWorkoutHistory(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        HistoryFilterRequest filter = HistoryFilterRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .searchKeyword(keyword)
                .build();

        PageResponse<WorkoutResponse> response =
                PageResponse.fromPage(historyService.getWorkoutHistory(principal.getId(), filter, pageable));
        return ApiResponse.success(response);
    }

    @GetMapping("/weight")
    public ApiResponse<PageResponse<WeightResponse>> getWeightHistory(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {
        HistoryFilterRequest filter = HistoryFilterRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        PageResponse<WeightResponse> response =
                PageResponse.fromPage(historyService.getWeightHistory(principal.getId(), filter, pageable));
        return ApiResponse.success(response);
    }
}