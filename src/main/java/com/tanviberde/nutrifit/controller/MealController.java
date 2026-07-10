package com.tanviberde.nutrifit.controller;

import com.tanviberde.nutrifit.dto.common.ApiResponse;
import com.tanviberde.nutrifit.dto.common.PageResponse;
import com.tanviberde.nutrifit.dto.meal.MealRequest;
import com.tanviberde.nutrifit.dto.meal.MealResponse;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ResponseEntity<ApiResponse<MealResponse>> createMeal(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody MealRequest request) {
        MealResponse response = mealService.createMeal(principal.getId(), request);
        return new ResponseEntity<>(
                ApiResponse.success("Meal logged successfully", response),
                HttpStatus.CREATED);
    }

    @PutMapping("/{mealId}")
    public ApiResponse<MealResponse> updateMeal(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long mealId,
            @Valid @RequestBody MealRequest request) {
        MealResponse response = mealService.updateMeal(principal.getId(), mealId, request);
        return ApiResponse.success("Meal updated successfully", response);
    }

    @DeleteMapping("/{mealId}")
    public ApiResponse<Void> deleteMeal(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long mealId) {
        mealService.deleteMeal(principal.getId(), mealId);
        return ApiResponse.success("Meal deleted successfully", null);
    }

    @GetMapping
    public ApiResponse<PageResponse<MealResponse>> getMeals(
            @AuthenticationPrincipal UserPrincipal principal,
            Pageable pageable) {
        PageResponse<MealResponse> response =
                PageResponse.fromPage(mealService.getMeals(principal.getId(), pageable));
        return ApiResponse.success(response);
    }
}