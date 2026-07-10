package com.tanviberde.nutrifit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "activity_logs",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "activity_date"}))
public class ActivityLog extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    @Column(name = "logged_meal", nullable = false)
    @Builder.Default
    private boolean loggedMeal = false;

    @Column(name = "logged_workout", nullable = false)
    @Builder.Default
    private boolean loggedWorkout = false;

    @Column(name = "logged_weight", nullable = false)
    @Builder.Default
    private boolean loggedWeight = false;
}