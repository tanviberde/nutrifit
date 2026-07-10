package com.tanviberde.nutrifit.dto.user;

import com.tanviberde.nutrifit.entity.enums.ActivityLevel;
import com.tanviberde.nutrifit.entity.enums.DietPreference;
import com.tanviberde.nutrifit.entity.enums.FitnessGoal;
import com.tanviberde.nutrifit.entity.enums.Gender;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {

    @NotNull(message = "Age is required")
    @Min(value = 10, message = "Age must be at least 10")
    private Integer age;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Height is required")
    @DecimalMin(value = "50.0", message = "Height must be realistic")
    private Double heightCm;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "20.0", message = "Weight must be realistic")
    private Double weightKg;

    @NotNull(message = "Activity level is required")
    private ActivityLevel activityLevel;

    @NotNull(message = "Fitness goal is required")
    private FitnessGoal fitnessGoal;

    @NotNull(message = "Diet preference is required")
    private DietPreference dietPreference;

    private List<String> allergies;

    private List<String> dislikedFoods;
}