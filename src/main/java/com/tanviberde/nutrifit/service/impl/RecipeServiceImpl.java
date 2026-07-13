package com.tanviberde.nutrifit.service.impl;

import com.tanviberde.nutrifit.ai.AiResponseParser;
import com.tanviberde.nutrifit.ai.GeminiClient;
import com.tanviberde.nutrifit.ai.GeneratedRecipeData;
import com.tanviberde.nutrifit.ai.PromptBuilder;
import com.tanviberde.nutrifit.dto.ai.RecipeGenerationRequest;
import com.tanviberde.nutrifit.dto.recipe.RecipeResponse;
import com.tanviberde.nutrifit.entity.Allergy;
import com.tanviberde.nutrifit.entity.DislikedFood;
import com.tanviberde.nutrifit.entity.Recipe;
import com.tanviberde.nutrifit.entity.RecipeIngredient;
import com.tanviberde.nutrifit.entity.User;
import com.tanviberde.nutrifit.exception.ResourceNotFoundException;
import com.tanviberde.nutrifit.exception.UnauthorizedException;
import com.tanviberde.nutrifit.repository.AllergyRepository;
import com.tanviberde.nutrifit.repository.DislikedFoodRepository;
import com.tanviberde.nutrifit.repository.RecipeIngredientRepository;
import com.tanviberde.nutrifit.repository.RecipeRepository;
import com.tanviberde.nutrifit.repository.UserRepository;
import com.tanviberde.nutrifit.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final UserRepository userRepository;
    private final AllergyRepository allergyRepository;
    private final DislikedFoodRepository dislikedFoodRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final PromptBuilder promptBuilder;
    private final GeminiClient geminiClient;
    private final AiResponseParser aiResponseParser;

    @Override
    public RecipeResponse generateRecipe(Long userId, RecipeGenerationRequest request) {
        return generateInternal(userId, request, null);
    }

    @Override
    public RecipeResponse regenerateRecipe(Long userId, Long recipeId) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        if (!existingRecipe.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have permission to access this recipe");
        }

        RecipeGenerationRequest request = new RecipeGenerationRequest(
                existingRecipe.getCalories(), existingRecipe.getProtein());

        return generateInternal(userId, request, existingRecipe.getTitle());
    }

    @Override
    public Page<RecipeResponse> getRecipes(Long userId, Pageable pageable) {
        return recipeRepository.findByUserIdAndDeletedFalse(userId, pageable)
                .map(recipe -> {
                    List<RecipeIngredient> ingredients =
                            recipeIngredientRepository.findByRecipeIdAndDeletedFalse(recipe.getId());
                    return RecipeResponse.fromEntity(recipe, ingredients);
                });
    }

    private RecipeResponse generateInternal(
            Long userId, RecipeGenerationRequest request, String avoidTitle) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<String> allergyNames = allergyRepository.findByUserIdAndDeletedFalse(userId).stream()
                .map(Allergy::getName)
                .toList();

        List<String> dislikedFoodNames = dislikedFoodRepository.findByUserIdAndDeletedFalse(userId).stream()
                .map(DislikedFood::getName)
                .toList();

        double calorieTarget = request.getCalorieTarget() != null
                ? request.getCalorieTarget()
                : nz(user.getDailyCalorieTarget()) / 3;

        double proteinTarget = request.getProteinTarget() != null
                ? request.getProteinTarget()
                : nz(user.getDailyProteinTarget()) / 3;

        String prompt = promptBuilder.buildRecipeGenerationPrompt(
                user, allergyNames, dislikedFoodNames, calorieTarget, proteinTarget, avoidTitle);

        String rawResponse = geminiClient.generateContent(prompt);
        GeneratedRecipeData data = aiResponseParser.parseRecipeGeneration(rawResponse);

        Recipe recipe = Recipe.builder()
                .user(user)
                .title(data.getTitle())
                .description(data.getDescription())
                .calories(data.getCalories())
                .protein(data.getProtein())
                .carbs(data.getCarbs())
                .fat(data.getFat())
                .fibre(data.getFibre())
                .dietPreference(user.getDietPreference())
                .build();

        Recipe savedRecipe = recipeRepository.save(recipe);

        List<RecipeIngredient> ingredients = data.getIngredients() == null
                ? List.of()
                : data.getIngredients().stream()
                .map(gi -> RecipeIngredient.builder()
                        .recipe(savedRecipe)
                        .ingredientName(gi.getName())
                        .quantity(gi.getQuantity())
                        .unit(gi.getUnit())
                        .build())
                .toList();

        List<RecipeIngredient> savedIngredients = recipeIngredientRepository.saveAll(ingredients);

        return RecipeResponse.fromEntity(savedRecipe, savedIngredients);
    }

    private double nz(Double value) {
        return value == null ? 0.0 : value;
    }
}