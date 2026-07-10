package com.tanviberde.nutrifit.dto.meal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealRequest {

    @NotBlank(message = "Meal name is required")
    private String mealName;

    @NotNull(message = "Calories are required")
    @DecimalMin(value = "0.0", message = "Calories cannot be negative")
    private Double calories;

    @DecimalMin(value = "0.0", message = "Protein cannot be negative")
    private Double protein;

    @DecimalMin(value = "0.0", message = "Carbs cannot be negative")
    private Double carbs;

    @DecimalMin(value = "0.0", message = "Fat cannot be negative")
    private Double fat;

    @DecimalMin(value = "0.0", message = "Fibre cannot be negative")
    private Double fibre;

    @NotNull(message = "Meal date is required")
    private LocalDate mealDate;
}