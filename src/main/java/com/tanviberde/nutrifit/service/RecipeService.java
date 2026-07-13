package com.tanviberde.nutrifit.service;

import com.tanviberde.nutrifit.dto.ai.RecipeGenerationRequest;
import com.tanviberde.nutrifit.dto.recipe.RecipeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeService {

    RecipeResponse generateRecipe(Long userId, RecipeGenerationRequest request);

    RecipeResponse regenerateRecipe(Long userId, Long recipeId);

    Page<RecipeResponse> getRecipes(Long userId, Pageable pageable);
}