package com.tanviberde.nutrifit.controller;

import com.tanviberde.nutrifit.dto.common.ApiResponse;
import com.tanviberde.nutrifit.dto.common.PageResponse;
import com.tanviberde.nutrifit.dto.workout.WorkoutRequest;
import com.tanviberde.nutrifit.dto.workout.WorkoutResponse;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.WorkoutService;
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
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkoutResponse>> createWorkout(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody WorkoutRequest request) {
        WorkoutResponse response = workoutService.createWorkout(principal.getId(), request);
        return new ResponseEntity<>(
                ApiResponse.success("Workout logged successfully", response),
                HttpStatus.CREATED);
    }

    @PutMapping("/{workoutId}")
    public ApiResponse<WorkoutResponse> updateWorkout(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long workoutId,
            @Valid @RequestBody WorkoutRequest request) {
        WorkoutResponse response = workoutService.updateWorkout(principal.getId(), workoutId, request);
        return ApiResponse.success("Workout updated successfully", response);
    }

    @DeleteMapping("/{workoutId}")
    public ApiResponse<Void> deleteWorkout(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long workoutId) {
        workoutService.deleteWorkout(principal.getId(), workoutId);
        return ApiResponse.success("Workout deleted successfully", null);
    }

    @GetMapping
    public ApiResponse<PageResponse<WorkoutResponse>> getWorkouts(
            @AuthenticationPrincipal UserPrincipal principal,
            Pageable pageable) {
        PageResponse<WorkoutResponse> response =
                PageResponse.fromPage(workoutService.getWorkouts(principal.getId(), pageable));
        return ApiResponse.success(response);
    }
}