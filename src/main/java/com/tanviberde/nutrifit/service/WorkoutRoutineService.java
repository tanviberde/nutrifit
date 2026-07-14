package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.ai.WorkoutRoutineGenerationRequest;
import com.tanviberde.nutrifit.dto.routine.WorkoutRoutineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkoutRoutineService {

    WorkoutRoutineResponse generateRoutine(Long userId, WorkoutRoutineGenerationRequest request);

    Page<WorkoutRoutineResponse> getRoutines(Long userId, Pageable pageable);
}