package com.tanviberde.nutrifit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "workout_routines")
public class WorkoutRoutine extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(name = "days_per_week")
    private Integer daysPerWeek;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WorkoutRoutineExercise> exercises = new ArrayList<>();
}