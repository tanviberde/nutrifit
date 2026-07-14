package com.tanviberde.nutrifit.dto.routine;

import com.tanviberde.nutrifit.entity.WorkoutRoutineExercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutRoutineExerciseResponse {

    private Long id;
    private String dayLabel;
    private String exerciseName;
    private Integer sets;
    private String reps;
    private Integer restSeconds;

    public static WorkoutRoutineExerciseResponse fromEntity(WorkoutRoutineExercise exercise) {
        return WorkoutRoutineExerciseResponse.builder()
                .id(exercise.getId())
                .dayLabel(exercise.getDayLabel())
                .exerciseName(exercise.getExerciseName())
                .sets(exercise.getSets())
                .reps(exercise.getReps())
                .restSeconds(exercise.getRestSeconds())
                .build();
    }
}