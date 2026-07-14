package com.tanviberde.nutrifit.dto.routine;

import com.tanviberde.nutrifit.entity.WorkoutRoutine;
import com.tanviberde.nutrifit.entity.WorkoutRoutineExercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutRoutineResponse {

    private Long id;
    private String title;
    private String description;
    private Integer daysPerWeek;
    private List<WorkoutRoutineExerciseResponse> exercises;

    public static WorkoutRoutineResponse fromEntity(
            WorkoutRoutine routine, List<WorkoutRoutineExercise> exercises) {
        return WorkoutRoutineResponse.builder()
                .id(routine.getId())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .daysPerWeek(routine.getDaysPerWeek())
                .exercises(exercises.stream().map(WorkoutRoutineExerciseResponse::fromEntity).toList())
                .build();
    }
}