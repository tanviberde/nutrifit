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
@Table(name = "daily_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "progress_date"}))
public class DailyProgress extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "progress_date", nullable = false)
    private LocalDate progressDate;

    @Column(name = "total_calories")
    private Double totalCalories;

    @Column(name = "total_protein")
    private Double totalProtein;

    @Column(name = "total_carbs")
    private Double totalCarbs;

    @Column(name = "total_fat")
    private Double totalFat;

    @Column(name = "total_fibre")
    private Double totalFibre;

    @Column(name = "calories_burned")
    private Double caloriesBurned;

    @Column(name = "water_intake_ml")
    private Integer waterIntakeMl;
}