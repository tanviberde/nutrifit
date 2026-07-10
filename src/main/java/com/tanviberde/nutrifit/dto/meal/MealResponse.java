package com.tanviberde.nutrifit.dto.meal;

import com.tanviberde.nutrifit.entity.Meal;
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
public class MealResponse {

    private Long id;
    private String mealName;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double fibre;
    private LocalDate mealDate;

    public static MealResponse fromEntity(Meal meal) {
        return MealResponse.builder()
                .id(meal.getId())
                .mealName(meal.getMealName())
                .calories(meal.getCalories())
                .protein(meal.getProtein())
                .carbs(meal.getCarbs())
                .fat(meal.getFat())
                .fibre(meal.getFibre())
                .mealDate(meal.getMealDate())
                .build();
    }
}