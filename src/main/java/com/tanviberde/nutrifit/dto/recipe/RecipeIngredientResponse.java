package com.tanviberde.nutrifit.dto.recipe;

import com.tanviberde.nutrifit.entity.RecipeIngredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientResponse {

    private Long id;
    private String ingredientName;
    private Double quantity;
    private String unit;

    public static RecipeIngredientResponse fromEntity(RecipeIngredient ingredient) {
        return RecipeIngredientResponse.builder()
                .id(ingredient.getId())
                .ingredientName(ingredient.getIngredientName())
                .quantity(ingredient.getQuantity())
                .unit(ingredient.getUnit())
                .build();
    }
}