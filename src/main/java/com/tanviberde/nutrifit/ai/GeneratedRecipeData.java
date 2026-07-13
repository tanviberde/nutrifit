package com.tanviberde.nutrifit.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedRecipeData {

    private String title;
    private String description;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double fibre;
    private List<GeneratedIngredient> ingredients;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneratedIngredient {
        private String name;
        private Double quantity;
        private String unit;
    }
}