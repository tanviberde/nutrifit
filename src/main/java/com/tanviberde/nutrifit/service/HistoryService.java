package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.history.HistoryFilterRequest;
import com.tanviberde.nutrifit.dto.meal.MealResponse;
import com.tanviberde.nutrifit.dto.weight.WeightResponse;
import com.tanviberde.nutrifit.dto.workout.WorkoutResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryService {

    Page<MealResponse> getMealHistory(Long userId, HistoryFilterRequest filter, Pageable pageable);

    Page<WorkoutResponse> getWorkoutHistory(Long userId, HistoryFilterRequest filter, Pageable pageable);

    Page<WeightResponse> getWeightHistory(Long userId, HistoryFilterRequest filter, Pageable pageable);
}