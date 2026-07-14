package com.tanviberde.nutrifit.controller;

import com.tanviberde.nutrifit.dto.ai.WorkoutRoutineGenerationRequest;
import com.tanviberde.nutrifit.dto.common.ApiResponse;
import com.tanviberde.nutrifit.dto.common.PageResponse;
import com.tanviberde.nutrifit.dto.routine.WorkoutRoutineResponse;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.WorkoutRoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workout-routines")
@RequiredArgsConstructor
public class WorkoutRoutineController {

    private final WorkoutRoutineService workoutRoutineService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkoutRoutineResponse>> generateRoutine(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody(required = false) WorkoutRoutineGenerationRequest request) {
        WorkoutRoutineGenerationRequest safeRequest =
                request != null ? request : new WorkoutRoutineGenerationRequest(null, null);
        WorkoutRoutineResponse response =
                workoutRoutineService.generateRoutine(principal.getId(), safeRequest);
        return new ResponseEntity<>(
                ApiResponse.success("Workout routine generated successfully", response),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ApiResponse<PageResponse<WorkoutRoutineResponse>> getRoutines(
            @AuthenticationPrincipal UserPrincipal principal,
            Pageable pageable) {
        PageResponse<WorkoutRoutineResponse> response = PageResponse.fromPage(
                workoutRoutineService.getRoutines(principal.getId(), pageable));
        return ApiResponse.success(response);
    }
}