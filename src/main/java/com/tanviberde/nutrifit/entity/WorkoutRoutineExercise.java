package com.tanviberde.nutrifit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "workout_routine_exercises")
public class WorkoutRoutineExercise extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private WorkoutRoutine routine;

    @Column(name = "day_label", nullable = false)
    private String dayLabel;

    @Column(name = "exercise_name", nullable = false)
    private String exerciseName;

    private Integer sets;
    private String reps;

    @Column(name = "rest_seconds")
    private Integer restSeconds;

    @Column(name = "order_index")
    private Integer orderIndex;
}