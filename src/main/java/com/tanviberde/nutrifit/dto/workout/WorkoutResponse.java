package com.tanviberde.nutrifit.dto.workout;

import com.tanviberde.nutrifit.entity.Workout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutResponse {

    private Long id;
    private String workoutName;
    private Integer durationMinutes;
    private Double caloriesBurned;
    private LocalDate workoutDate;

    public static WorkoutResponse fromEntity(Workout workout) {
        return WorkoutResponse.builder()
                .id(workout.getId())
                .workoutName(workout.getWorkoutName())
                .durationMinutes(workout.getDurationMinutes())
                .caloriesBurned(workout.getCaloriesBurned())
                .workoutDate(workout.getWorkoutDate())
                .build();
    }
}