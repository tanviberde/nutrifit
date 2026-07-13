package com.tanviberde.nutrifit.dto.recipe;

import com.tanviberde.nutrifit.entity.Recipe;
import com.tanviberde.nutrifit.entity.RecipeIngredient;
import com.tanviberde.nutrifit.entity.enums.DietPreference;
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
public class RecipeResponse {

    private Long id;
    private String title;
    private String description;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double fibre;
    private DietPreference dietPreference;
    private List<RecipeIngredientResponse> ingredients;

    public static RecipeResponse fromEntity(Recipe recipe, List<RecipeIngredient> ingredients) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .calories(recipe.getCalories())
                .protein(recipe.getProtein())
                .carbs(recipe.getCarbs())
                .fat(recipe.getFat())
                .fibre(recipe.getFibre())
                .dietPreference(recipe.getDietPreference())
                .ingredients(ingredients.stream().map(RecipeIngredientResponse::fromEntity).toList())
                .build();
    }
}