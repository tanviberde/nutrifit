package com.tanviberde.nutrifit.entity;

import com.tanviberde.nutrifit.entity.enums.ActivityLevel;
import com.tanviberde.nutrifit.entity.enums.DietPreference;
import com.tanviberde.nutrifit.entity.enums.FitnessGoal;
import com.tanviberde.nutrifit.entity.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    private Double heightCm;

    private Double weightKg;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level", length = 30)
    private ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "fitness_goal", length = 30)
    private FitnessGoal fitnessGoal;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet_preference", length = 30)
    private DietPreference dietPreference;

    @Column(name = "daily_calorie_target")
    private Double dailyCalorieTarget;

    @Column(name = "daily_protein_target")
    private Double dailyProteinTarget;

    @Column(name = "daily_carb_target")
    private Double dailyCarbTarget;

    @Column(name = "daily_fat_target")
    private Double dailyFatTarget;

    @Column(name = "daily_fibre_target")
    private Double dailyFibreTarget;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Allergy> allergies = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DislikedFood> dislikedFoods = new ArrayList<>();
}