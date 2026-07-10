package com.tanviberde.nutrifit.dto.user;

import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.entity.enums.ActivityLevel;
import com.tanviberde.nutrifit.entity.enums.DietPreference;
import com.tanviberde.nutrifit.entity.enums.FitnessGoal;
import com.tanviberde.nutrifit.entity.enums.Gender;
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
public class UserProfileResponse {

    private Long userId;
    private String name;
    private String email;
    private Integer age;
    private Gender gender;
    private Double heightCm;
    private Double weightKg;
    private ActivityLevel activityLevel;
    private FitnessGoal fitnessGoal;
    private DietPreference dietPreference;
    private List<String> allergies;
    private List<String> dislikedFoods;
    private Double dailyCalorieTarget;
    private Double dailyProteinTarget;
    private Double dailyCarbTarget;
    private Double dailyFatTarget;
    private Double dailyFibreTarget;

    public static UserProfileResponse fromEntity(
            User user, List<String> allergies, List<String> dislikedFoods) {
        return UserProfileResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender())
                .heightCm(user.getHeightCm())
                .weightKg(user.getWeightKg())
                .activityLevel(user.getActivityLevel())
                .fitnessGoal(user.getFitnessGoal())
                .dietPreference(user.getDietPreference())
                .allergies(allergies)
                .dislikedFoods(dislikedFoods)
                .dailyCalorieTarget(user.getDailyCalorieTarget())
                .dailyProteinTarget(user.getDailyProteinTarget())
                .dailyCarbTarget(user.getDailyCarbTarget())
                .dailyFatTarget(user.getDailyFatTarget())
                .dailyFibreTarget(user.getDailyFibreTarget())
                .build();
    }
}