package com.tanviberde.nutrifit.controller;

import com.tanviberde.nutrifit.dto.ai.RecipeGenerationRequest;
import com.tanviberde.nutrifit.dto.common.ApiResponse;
import com.tanviberde.nutrifit.dto.common.PageResponse;
import com.tanviberde.nutrifit.dto.recipe.RecipeResponse;
import com.tanviberde.nutrifit.security.UserPrincipal;
import com.tanviberde.nutrifit.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<ApiResponse<RecipeResponse>> generateRecipe(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody(required = false) RecipeGenerationRequest request) {
        RecipeGenerationRequest safeRequest = request != null ? request : new RecipeGenerationRequest(null, null);
        RecipeResponse response = recipeService.generateRecipe(principal.getId(), safeRequest);
        return new ResponseEntity<>(
                ApiResponse.success("Recipe generated successfully", response),
                HttpStatus.CREATED);
    }

    @PostMapping("/{recipeId}/regenerate")
    public ApiResponse<RecipeResponse> regenerateRecipe(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long recipeId) {
        RecipeResponse response = recipeService.regenerateRecipe(principal.getId(), recipeId);
        return ApiResponse.success("New recipe generated successfully", response);
    }

    @GetMapping
    public ApiResponse<PageResponse<RecipeResponse>> getRecipes(
            @AuthenticationPrincipal UserPrincipal principal,
            Pageable pageable) {
        PageResponse<RecipeResponse> response =
                PageResponse.fromPage(recipeService.getRecipes(principal.getId(), pageable));
        return ApiResponse.success(response);
    }
}