package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.workout.WorkoutRequest;
import com.tanviberde.nutrifit.dto.workout.WorkoutResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkoutService {

    WorkoutResponse createWorkout(Long userId, WorkoutRequest request);

    WorkoutResponse updateWorkout(Long userId, Long workoutId, WorkoutRequest request);

    void deleteWorkout(Long userId, Long workoutId);

    Page<WorkoutResponse> getWorkouts(Long userId, Pageable pageable);
}